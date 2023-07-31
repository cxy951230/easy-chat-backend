package com.cxy.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
	@Autowired
	private RedisProperties redisProperties;


//	@Bean
	public JedisPool jedisPool() {
		JedisPoolConfig config = new JedisPoolConfig();
//		config.setMaxIdle(redisProperties.getJedis().getPool().getMaxIdle());
		JedisPool jedisPool;
		if("".equals(redisProperties.getPassword())){
			jedisPool = new JedisPool(config, redisProperties.getHost(), redisProperties.getPort());
		}else{
			jedisPool = new JedisPool(config, redisProperties.getHost(), redisProperties.getPort(),2000, redisProperties.getPassword());
		}
		return jedisPool;
	}

//	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		// 泛型改成 String Object，方便我们的使用
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		// Json序列化配置
		// 使用 json解析对象
		Jackson2JsonRedisSerializer objectJackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();
		// 通过 ObjectMapper进行转义
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		objectJackson2JsonRedisSerializer.setObjectMapper(om);
		// String的序列化
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		// key采用 String的序列化方式
		template.setKeySerializer(stringRedisSerializer);
		// hash的 key也采用 String的序列化方式
		template.setHashKeySerializer(stringRedisSerializer);
		// Value的序列化方式采用 jackSon
		template.setValueSerializer(objectJackson2JsonRedisSerializer);
		// hash的 value序列化也采用 jackSon
		template.setHashValueSerializer(objectJackson2JsonRedisSerializer);
		template.setConnectionFactory(redisConnectionFactory);
		template.afterPropertiesSet();
		return template;
	}
}
