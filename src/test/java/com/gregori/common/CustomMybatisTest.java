package com.gregori.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.context.annotation.Import;

import com.gregori.config.MyBatisConfig;

@MybatisTest
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({ MyBatisConfig.class })
public @interface CustomMybatisTest {
}
