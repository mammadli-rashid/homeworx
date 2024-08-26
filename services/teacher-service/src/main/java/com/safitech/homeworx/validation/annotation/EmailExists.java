package com.safitech.homeworx.validation.annotation;

import com.safitech.homeworx.validation.validator.EmailExistsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailExistsValidator.class)
@Documented
public @interface EmailExists {
    String message() default "Email is not exists in contact details!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
