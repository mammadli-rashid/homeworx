package com.safitech.homeworx.validation.validator;

import com.safitech.homeworx.validation.annotation.UniqueStringCollectionIgnoreCase;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueStringCollectionIgnoreCaseValidator implements ConstraintValidator<UniqueStringCollectionIgnoreCase, List<String>> {

    @Override
    public void initialize(UniqueStringCollectionIgnoreCase constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Set<String> uniqueStrings = new HashSet<>();
        for (String s : value) {
            if (!uniqueStrings.add(s.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
}