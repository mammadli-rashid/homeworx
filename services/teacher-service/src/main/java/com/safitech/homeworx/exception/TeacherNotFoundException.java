package com.safitech.homeworx.exception;

import org.springframework.http.HttpStatus;

public class TeacherNotFoundException extends CustomException {
    public TeacherNotFoundException(String teacherId) {
        super("Teacher not found with id: " + teacherId, HttpStatus.NOT_FOUND);
    }
}