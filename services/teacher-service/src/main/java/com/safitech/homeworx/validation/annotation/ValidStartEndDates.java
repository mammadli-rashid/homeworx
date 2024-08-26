package com.safitech.homeworx.validation.annotation;

import com.safitech.homeworx.validation.validator.ValidStartEndDatesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidStartEndDatesValidator.class)
@Documented
public @interface ValidStartEndDates {
    String message() default "Start date must be earlier than end date!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
