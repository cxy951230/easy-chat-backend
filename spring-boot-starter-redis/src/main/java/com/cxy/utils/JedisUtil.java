package com.cxy.utils;

import com.alibaba.fastjson.JSONObject;
import com.cxy.annotation.RedisClient;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class JedisUtil {
	public static final String CLAZZ_FLAG = "@clazz";
	@RedisClient("jedisPool")
	public JedisPool jedisPool;

	public Jedis jedis(){
		Jedis jedis = jedisPool.getResource();
		return jedis;
	}

	public void set(String key, Object value){
		Jedis jedis = jedis();
		jedis.set(key, obj2string(value));
		jedis.close();
	}

	public void set(String key, Object value, Integer expire){
		set(key,value);
		expire(key, expire);
	}

	public Object get(String key){
		Jedis jedis = jedis();
		String value = jedis.get(key);
		jedis.close();
		return string2obj(value);
	}

	public void hset(String key, String field, Object value){
		Jedis jedis = jedis();
		jedis.hset(key, field, obj2string(value));
		jedis.close();
	}

	public void hset(String key, String field, Object value, Integer expire){
		hset(key, field, value, expire);
		expire(key, expire);
	}

	public Object hget(String key, String field){
		Jedis jedis = jedis();
		String value = jedis.hget(key, field);
		jedis.close();
		return string2obj(value);
	}

	public void lpush(String key, Object value){
		Jedis jedis = jedis();
		jedis.lpush(key, obj2string(value));
		jedis.close();
	}

	public void lpush(String key, Object value, Integer expire){
		lpush(key, value);
		expire(key, expire);
	}

	public Object lpop(String key){
		Jedis jedis = jedis();
		Object obj = string2obj(jedis.lpop(key));
		jedis.close();
		return obj;
	}

	public void rpush(String key, Object value){
		Jedis jedis = jedis();
		jedis.rpush(key, obj2string(value));
		jedis.close();
	}

	public void rpush(String key, Object value, Integer expire){
		rpush(key, value);
		expire(key, expire);
	}

	public Object rpop(String key){
		Jedis jedis = jedis();
		Object obj = string2obj(jedis.rpop(key));
		jedis.close();
		return obj;
	}

	public void expire(String key, Integer expire){
		Jedis jedis = jedis();
		jedis.expire(key, expire);
	}

	public void lua(){
		Jedis jedis = jedis();
		System.out.println(jedis.eval("return redis.call('get','foo')"));;
		jedis.close();
	}

	public String obj2string(Object value){
		Class<?> clazz = value.getClass();
		String clazzName = clazz.getName();
		if(!clazz.isArray() && !clazzName.startsWith("java.lang")){
			JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(value));
			jsonObject.put(CLAZZ_FLAG,clazzName);
			return jsonObject.toJSONString();
		}else {
			return JSONObject.toJSONString(value);
		}
	}

	public Object string2obj(String value){
		if(value == null){
			return null;
		}
		Object obj = JSONObject.parse(value);
		if(obj instanceof JSONObject){
			JSONObject jsonObject = (JSONObject) obj;
			String clazzName = jsonObject.getString(CLAZZ_FLAG);
			Class<?> clazz = null;
			try {
				clazz = Class.forName(clazzName);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			obj = JSONObject.parseObject(value, clazz);
		}
		return obj;
	}

}
