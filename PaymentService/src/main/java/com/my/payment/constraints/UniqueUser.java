package com.my.payment.constraints;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target( {METHOD,FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueUserValidator.class)
@Documented
public @interface UniqueUser  {
	public static final String MESSAGE = "user exists";
	String message() default MESSAGE;
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
