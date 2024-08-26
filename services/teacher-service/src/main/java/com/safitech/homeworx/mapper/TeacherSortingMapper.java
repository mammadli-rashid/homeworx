package com.safitech.homeworx.mapper;

import com.safitech.homeworx.constant.SortField;
import com.safitech.homeworx.exception.InvalidSortFieldException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherSortingMapper {
    private static final Map<String, String> sortFieldMap = new HashMap<>();

    static {
        for (SortField field : SortField.values()) {
            sortFieldMap.put(field.getLowerRequestValue(), field.getMappedValue());
        }
    }

    public static List<String> mapSortFields(List<String> reqSortFields) {
        List<String> mappedSortFields = new ArrayList<>();
        for (String reqSortField : reqSortFields) {
            if (sortFieldMap.containsKey(reqSortField.trim())) {
                mappedSortFields.add(sortFieldMap.get(reqSortField));
            } else {
                throw new InvalidSortFieldException(reqSortField);
            }
        }
        return mappedSortFields;
    }

}