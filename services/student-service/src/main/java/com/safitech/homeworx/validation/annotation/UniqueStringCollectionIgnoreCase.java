package com.safitech.homeworx.validation.annotation;

import com.safitech.homeworx.validation.validator.UniqueStringCollectionIgnoreCaseValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueStringCollectionIgnoreCaseValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueStringCollectionIgnoreCase {
    String message() default "Collection must contain unique strings, ignoring case differences.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}