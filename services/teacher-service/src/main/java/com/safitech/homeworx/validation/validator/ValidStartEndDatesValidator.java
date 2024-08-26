package com.safitech.homeworx.validation.validator;

import com.safitech.homeworx.validation.annotation.ValidStartEndDates;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class ValidStartEndDatesValidator implements ConstraintValidator<ValidStartEndDates, Object> {

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            Field startDateField = object.getClass().getDeclaredField("startDate");
            Field endDateField = object.getClass().getDeclaredField("endDate");

            startDateField.setAccessible(true);
            endDateField.setAccessible(true);

            LocalDate startDate = (LocalDate) startDateField.get(object);
            LocalDate endDate = (LocalDate) endDateField.get(object);

            if (startDate == null || endDate == null) {
                return true;
            }

            return startDate.isBefore(endDate);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}