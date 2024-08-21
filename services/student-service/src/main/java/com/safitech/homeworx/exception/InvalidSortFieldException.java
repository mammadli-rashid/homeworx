package com.safitech.homeworx.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Getter
public class InvalidSortFieldException extends CustomException {
    public InvalidSortFieldException(String fieldName) {
        super("Invalid sort field: " + fieldName, HttpStatus.BAD_REQUEST);
    }
}
