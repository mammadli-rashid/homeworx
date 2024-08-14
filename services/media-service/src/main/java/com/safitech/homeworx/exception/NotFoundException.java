package com.safitech.homeworx.exception;

import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
public abstract class NotFoundException extends CustomException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}
