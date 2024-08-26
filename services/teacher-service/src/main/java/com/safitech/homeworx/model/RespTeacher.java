package com.safitech.homeworx.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonPropertyOrder({"teacherId", "fullName", "contacts", "educationHistory", "workHistory"})
public class RespTeacher extends ReqTeacher {
    String teacherId;
}
