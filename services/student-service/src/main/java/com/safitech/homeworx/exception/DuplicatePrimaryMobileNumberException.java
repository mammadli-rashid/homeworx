package com.safitech.homeworx.exception;

import org.springframework.http.HttpStatus;

public class DuplicatePrimaryMobileNumberException extends CustomException {
    public DuplicatePrimaryMobileNumberException(String mobileNumber) {
        super("There is already user registered this mobile number as his/her primary mobile number: " + mobileNumber, HttpStatus.CONFLICT);
    }
}
