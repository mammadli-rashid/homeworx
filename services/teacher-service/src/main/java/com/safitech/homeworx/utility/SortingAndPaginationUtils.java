package com.safitech.homeworx.utility;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class SortingAndPaginationUtils {
    public static Pageable createPageRequest(int page, int size, List<String> sortFields, List<String> sortDirections) {
        List<Sort.Order> orders = new ArrayList<>();
        for (int i = 0; i < sortFields.size(); i++) {
            String field = sortFields.get(i);
            String direction = (i < sortDirections.size()) ? sortDirections.get(i) : "asc";
            orders.add(new Sort.Order(Sort.Direction.fromString(direction), field));
        }
        return PageRequest.of(page, size, Sort.by(orders));
    }
}

