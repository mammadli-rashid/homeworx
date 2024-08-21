package com.safitech.homeworx.entity.listener;

import com.safitech.homeworx.constant.ActiveStatus;
import com.safitech.homeworx.entity.Student;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class StudentListener {

    @PrePersist
    public void prePersist(Student student) {
        student.setActiveStatus(ActiveStatus.ACTIVE.getValue());
        student.setCreatedAt(LocalDateTime.now());
        log.debug("Preparing to persist a new Student entity with details: {}", student);
    }

    @PostPersist
    public void postPersist(Student student) {
        log.debug("Successfully persisted a new Student entity with ID: {}", student.getStudentId());
    }

    @PreUpdate
    public void preUpdate(Student student) {
        student.setLastModifiedAt(LocalDateTime.now());
        log.debug("Preparing to update an existing Student entity with ID: {}", student.getStudentId());
    }

    @PostUpdate
    public void postUpdate(Student student) {
        log.debug("Successfully updated Student entity with ID: {}", student.getStudentId());
    }

    @PreRemove
    public void preRemove(Student student) {
        log.debug("Preparing to delete Student entity with ID: {}", student.getStudentId());
    }

    @PostRemove
    public void postRemove(Student student) {
        log.debug("Successfully deleted Student entity with ID: {}", student.getStudentId());
    }

    @PostLoad
    public void postLoad(Student student) {
        log.debug("Loaded Student entity with details: {}", student);
    }
}

