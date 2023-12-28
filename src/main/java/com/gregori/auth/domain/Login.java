package com.gregori.auth.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Login {

	@AliasFor("authority")
	Authority[] value() default { Authority.GENERAL_MEMBER };

	@AliasFor("value")
	Authority[] authority() default { Authority.GENERAL_MEMBER };
}
