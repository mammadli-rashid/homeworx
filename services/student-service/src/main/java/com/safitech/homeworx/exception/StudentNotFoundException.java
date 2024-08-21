package com.safitech.homeworx.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Getter
public class StudentNotFoundException extends CustomException {
    public StudentNotFoundException(long studentId) {
        super("Student not found with id: " + studentId, HttpStatus.NOT_FOUND);
    }
}
