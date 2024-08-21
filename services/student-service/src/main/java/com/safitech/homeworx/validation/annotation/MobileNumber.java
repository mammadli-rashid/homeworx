package com.safitech.homeworx.validation.annotation;

import com.safitech.homeworx.validation.validator.MobileNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MobileNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MobileNumber {
    String message() default "Invalid mobile number format!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}