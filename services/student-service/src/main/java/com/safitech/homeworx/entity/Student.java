package com.safitech.homeworx.entity;

import com.safitech.homeworx.entity.listener.StudentListener;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@EntityListeners({AuditingEntityListener.class, StudentListener.class})
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long studentId;
    @Column(nullable = false)
    String fullName;
    LocalDate dateOfBirth;
    @Column(nullable = false)
    String email;
    @Column(nullable = false)
    String primaryMobileNumber;
    String secondaryMobileNumber;
    String address;
    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(insertable = false)
    LocalDateTime lastModifiedAt;
    @Builder.Default
    @Column(columnDefinition = "int default 1")
    int activeStatus = 1;
}
