package com.safitech.homeworx.validation.annotation;

import com.safitech.homeworx.validation.validator.MobileNumberExistsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MobileNumberExistsValidator.class)
@Documented
public @interface MobileNumberExists {
    String message() default "Mobile number is not exists in contact details!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
