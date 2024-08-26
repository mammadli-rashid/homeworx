package com.safitech.homeworx.repository;

import com.safitech.homeworx.entity.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends MongoRepository<Teacher, String> {
    Optional<Teacher> findByTeacherIdAndActiveStatus(String teacherId, int activeStatus);
    List<Teacher> findAllByActiveStatus(int activeStatus);
    List<Teacher> findAllByActiveStatusAndTeacherIdNot(int activeStatus, String teacherId);
}
