package com.safitech.homeworx.validation.annotation;

import com.safitech.homeworx.validation.validator.SortCriteriaValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SortCriteriaValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSortCriteria {
    String message() default "Invalid sort criteria!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}