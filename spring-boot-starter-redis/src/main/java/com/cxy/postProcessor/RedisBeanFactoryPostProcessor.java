package com.cxy.postProcessor;

import com.cxy.annotation.RedisClient;
import com.google.common.collect.Lists;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Component
public class RedisBeanFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor {

	private static List<String> excludeNames = Lists.newArrayList("redisTemplate","stringRedisTemplate");

	//No bean named 'autoConfigurationReport' available todo 报错
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
//		beanDefinitionRegistry.removeBeanDefinition("redisReferenceResolver");
//		excludeNames.stream().forEach(e->{
//			if(beanDefinitionRegistry.containsBeanDefinition(e)){
//				beanDefinitionRegistry.removeBeanDefinition(e);
//			}
//		});
		ConfigurableListableBeanFactory beanFactory = (ConfigurableListableBeanFactory)beanDefinitionRegistry;
		Iterator<String> beanNamesIterator = Arrays.stream(beanDefinitionRegistry.getBeanDefinitionNames()).iterator();
		while (beanNamesIterator.hasNext()){
			String beanName = beanNamesIterator.next();
			try{
				BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);
				String beanClassName = beanDefinition.getBeanClassName();
//				System.out.println(beanName);
				if(StringUtils.isEmpty(beanClassName)){
					continue;
				}
				Class clazz = Class.forName(beanClassName);
				Field[] fields = getAllFields(clazz);
				Arrays.stream(fields).forEach(e->{
					RedisClient annotation = e.getAnnotation(RedisClient.class);
					if(annotation != null){
						Qualifier qualifier = annotation.getClass().getAnnotation(Qualifier.class);
						Class fieldType = e.getType();
						String name = !"".equals(annotation.value()) ? annotation.value() : e.getName();
						//该字段类型现有beanName
						String[] beanNames = beanFactory.getBeanNamesForType(fieldType);
						List<String> beanNameList = Lists.newArrayList(beanNames);
						beanNameList.removeAll(excludeNames);
						if(beanNameList.size()>0){//已存在该class对应的beanDefinition
							BeanDefinition existBeanDefinition = beanFactory.getBeanDefinition(beanNameList.get(0));
							//注册别名
							beanDefinitionRegistry.registerAlias(beanNames[0],name);
						}else {
							//注册新beanDefinition
							RootBeanDefinition rootBeanDefinition = buildBeanDefinition(fieldType, annotation, qualifier);
							beanDefinitionRegistry.registerBeanDefinition(name, rootBeanDefinition);
						}
					}
				});
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	public RootBeanDefinition buildBeanDefinition(Class fieldType,RedisClient annotation, Qualifier qualifier){
		RootBeanDefinition definition = new RootBeanDefinition(fieldType);
		definition.setTargetType(fieldType);
		//设置该beanDefinition的beanFactory
		definition.setFactoryBeanName(RedisClientFactory.FACTORY_NAME);
		//设置该beanDefinition的methodName
		definition.setFactoryMethodName("build");
		//设置该beanDefinition的注入方式
		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_NO);
//		definition.setPrimary(true);
		if(qualifier != null){
			definition.addQualifier(new AutowireCandidateQualifier(Qualifier.class, qualifier.value()));
		}
		//设置构建参数，传递给beanFactory的build方法
		ConstructorArgumentValues args = new ConstructorArgumentValues();
		args.addIndexedArgumentValue(0,fieldType);
		args.addIndexedArgumentValue(1,annotation);
		definition.setConstructorArgumentValues(args);
		return definition;
	}

	/**
	 * 获取本类及其父类的字段属性
	 * @param clazz 当前类对象
	 * @return 字段数组
	 */
	public static Field[] getAllFields(Class<?> clazz) {
		List<Field> fieldList = new ArrayList<>();
		while (clazz != null){
			fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
			clazz = clazz.getSuperclass();
		}
		Field[] fields = new Field[fieldList.size()];
		return fieldList.toArray(fields);
	}


	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

	}
}
