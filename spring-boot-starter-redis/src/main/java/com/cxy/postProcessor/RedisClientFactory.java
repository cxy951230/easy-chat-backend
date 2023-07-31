package com.cxy.postProcessor;

import com.cxy.annotation.RedisClient;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

@Component(RedisClientFactory.FACTORY_NAME)
public class RedisClientFactory {

	@Autowired
	private Environment environment;

	@Autowired
	private JedisConnectionFactory factory;

	public static final String FACTORY_NAME = "redisClientFactory";

	public <T> T build(Class<T> fieldType, RedisClient annotation){
		String prefix = annotation.prefix();
		//获取redis配置
		RedisProperties properties = buildRedisProperties(prefix);
		if (properties.getHost() == null) {
			throw new IllegalStateException("redis config is empty, prefix = " + prefix);
		}

		if(fieldType == StringRedisTemplate.class) {
			StringRedisTemplate template = new StringRedisTemplate();
			template.setConnectionFactory(factory);
			return (T) template;
		} else if(fieldType == RedisTemplate.class) {
			RedisTemplate<String, Object> template = new RedisTemplate<>();
			// Json序列化配置
			GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
			// String的序列化
			StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
			// key采用 String的序列化方式
			template.setKeySerializer(stringRedisSerializer);
			// hash的 key也采用 String的序列化方式
			template.setHashKeySerializer(stringRedisSerializer);
			// Value的序列化方式采用 jackSon
			template.setValueSerializer(genericJackson2JsonRedisSerializer);
			// hash的 value序列化也采用 jackSon
			template.setHashValueSerializer(genericJackson2JsonRedisSerializer);
			template.setConnectionFactory(factory);
			template.afterPropertiesSet();
			return (T) template;
		} else if(fieldType.getName().equals("redis.clients.jedis.JedisPool")){
			factory.afterPropertiesSet();
			Field field = ReflectionUtils.findField(JedisConnectionFactory.class, "pool");
			ReflectionUtils.makeAccessible(field);
			return (T) ReflectionUtils.getField(field, factory);
		}

		return null;
	}

	private RedisProperties buildRedisProperties(String prefix){
		RedisProperties properties = new RedisProperties();
		properties.setHost(null);                               //清空默认值，必须通过env注入，后续有校验
		bind(prefix, properties);
		RedisProperties.Pool pool = new RedisProperties.Pool();
		bind(prefix + ".pool", pool);
		properties.getJedis().setPool(pool);
		return properties;
	}

	private void bind(String prefix, Object target){
		Binder binder = new Binder(ConfigurationPropertySources.from(((ConfigurableEnvironment) environment).getPropertySources()));
		try {
			binder.bind(prefix, Bindable.ofInstance(target));
		}
		catch (Exception ex) {
			String targetClass = ClassUtils.getShortName(target.getClass());
			throw new BeanCreationException("Could not bind properties to " + targetClass + " (" + prefix + ")", ex);
		}
	}
}
