package com.safitech.homeworx.validation.validator;

import com.safitech.homeworx.dto.RespStudent;
import com.safitech.homeworx.exception.InvalidSortFieldException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StudentRequestValidator {
    private static final Set<String> requestFields = Arrays
            .stream(RespStudent.class.getDeclaredFields())
            .map(Field::getName)
            .collect(Collectors.toSet());

    public void validateSortFields(List<String> sortFields) {
        for (String sortField : sortFields) {
            if (!requestFields.contains(sortField)) {
                throw new InvalidSortFieldException(sortField);
            }
        }
    }
}