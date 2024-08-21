package com.safitech.homeworx.validation.validator;

import com.safitech.homeworx.dto.ReqSpecPaginationSorting;
import com.safitech.homeworx.validation.annotation.ValidSortCriteria;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class SortCriteriaValidator implements ConstraintValidator<ValidSortCriteria, ReqSpecPaginationSorting> {

    @Override
    public void initialize(ValidSortCriteria constraintAnnotation) {
    }

    @Override
    public boolean isValid(ReqSpecPaginationSorting value, ConstraintValidatorContext context) {
        List<String> sortFields = value.getSortFields();
        List<String> sortDirections = value.getSortDirections();
        if (sortFields == null || sortDirections == null) {
            return true;
        }
        if (sortFields.isEmpty()) {
            return true;
        }
        for (String sortDirection : sortDirections) {
            if (!(sortDirection.equals("asc") || sortDirection.equals("desc"))) {
                return false;
            }
        }
        return value.getSortFields().size() == value.getSortDirections().size();
    }

}