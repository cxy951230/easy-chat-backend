package com.cxy.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;
import java.sql.JDBCType;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Field {

	String value() default "";

	boolean primary() default false;

	JDBCType type() default JDBCType.VARCHAR;

	String len() default "";

	boolean nullable() default true;

}
