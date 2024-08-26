package com.safitech.homeworx.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortField {
    TEACHER_ID("teacherId", "teacherId"),
    FULL_NAME("fullName", "fullName"),
    BIRTH_DATE("birthDate", "birthDate"),
    EMAIL("email", "contacts.EMAIL"),
    MOBILE("mobile", "contacts.MOBILE"),
    TEACHING_SUBJECT("teachingSubjects", "teachingSubjects"),
    WORKPLACE("workplace", "workHistory.workplace"),
    OCCUPATION("occupation", "workHistory.occupation"),
    INSTITUTION("institution", "educationHistory.institution"),
    FACULTY("faculty", "educationHistory.faculty"),
    SPECIALIZATION("specialization", "educationHistory.specialization"),
    ;

    private final String lowerRequestValue;
    private final String mappedValue;
}
