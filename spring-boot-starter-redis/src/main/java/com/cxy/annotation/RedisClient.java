package com.cxy.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Autowired
public @interface RedisClient {
	String value() default "";

	String prefix() default "spring.redis";
}
