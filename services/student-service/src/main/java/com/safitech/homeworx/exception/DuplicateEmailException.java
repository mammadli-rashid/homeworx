package com.safitech.homeworx.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Getter
public class DuplicateEmailException extends CustomException {
    public DuplicateEmailException(String email) {
        super("There is already user registered with this email: " + email, HttpStatus.CONFLICT);
    }
}
