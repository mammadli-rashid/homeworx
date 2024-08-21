package com.safitech.homeworx.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RespStudent {
    long studentId;
    String fullName;
    LocalDate dateOfBirth;
    String email;
    String primaryMobileNumber;
    String secondaryMobileNumber;
    String address;
}
