package com.cxy.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.apache.ibatis.logging.log4j.Log4jImpl;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:jdbc.properties")
@MapperScan(basePackages = "com.cxy.mapper")//优先级高于MybatisAutoConfiguration（多数据源不生效）只过滤包，不过滤@Mapper注解
@Data
public class DataSourceConfig {
	@Value("${jdbc.driver}")
	private String driverClassName;
	@Value("${jdbc.url}")
	private String url;
	@Value("${jdbc.username}")
	private String username;
	@Value("${jdbc.password}")
	private String password;

	//配置数据源，并交由spring统一管理
	@Bean//相当于spring配置文件中的bean节点
	public DataSource dataSource() {
		//创建DruidDataSource
		DruidDataSource druidDataSource = new DruidDataSource();
		//给属性赋值
		druidDataSource.setDriverClassName(driverClassName);
		druidDataSource.setUrl(url);
		druidDataSource.setUsername(username);
		druidDataSource.setPassword(password);
		//返回druidDataSource
		return druidDataSource;
	}

	//配置MyBatis核心工厂，并交由Spring统一管理
//	@Bean
	public SqlSessionFactoryBean sqlSessionFactoryBean() throws Exception {
		//创建SqlSessionFactoryBean
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		//配置数据源
		sqlSessionFactoryBean.setDataSource(dataSource());
		//设置实体类别名
		sqlSessionFactoryBean.setTypeAliasesPackage("com.cxy.entity");
		//设置mapper映射文件的路径
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/*.xml"));
		//xml配置configuration
//		sqlSessionFactoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/mybatis-config.xml"));
		//编码方式设置configuration
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setLogImpl(Log4jImpl.class);
		sqlSessionFactoryBean.setConfiguration(configuration);
		//返回sqlSessionFactoryBean
		return sqlSessionFactoryBean;
	}

}
