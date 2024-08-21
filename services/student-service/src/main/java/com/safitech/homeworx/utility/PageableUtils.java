package com.safitech.homeworx.utility;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PageableUtils {
    public static Pageable buildStudentPageable(int offset, int pageSize,
                                                List<String> sortFields, List<String> sortDirs) {
        int page = offset / pageSize;
        List<Sort.Order> orders = new ArrayList<>();

        if (sortFields != null && sortDirs != null && sortFields.size() == sortDirs.size()) {
            for (int i = 0; i < sortFields.size(); i++) {
                orders.add(new Sort.Order(Sort.Direction.fromString(sortDirs.get(i)), sortFields.get(i)));
            }
        }

        return PageRequest.of(page, pageSize, Sort.by(orders));
    }
}
