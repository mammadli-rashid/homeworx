package com.safitech.homeworx.entity;

import com.safitech.homeworx.constant.ActiveStatus;
import com.safitech.homeworx.constant.ContactType;
import com.safitech.homeworx.model.nonvalidated.CareerDetail;
import com.safitech.homeworx.model.nonvalidated.EducationDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "teachers")
public class Teacher {
    @Id
    private String teacherId;
    private String fullName;
    private Map<ContactType, String> contacts;
    private Set<String> teachingSubjects;
    private LocalDateTime birthDate;
    private Set<EducationDetail> educationHistory;
    private Set<CareerDetail> workHistory;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    @Builder.Default
    private int activeStatus = ActiveStatus.ACTIVE.getValue();
}