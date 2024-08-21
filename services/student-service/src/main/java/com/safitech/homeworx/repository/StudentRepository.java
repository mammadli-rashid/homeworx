package com.safitech.homeworx.repository;

import com.safitech.homeworx.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer>, JpaSpecificationExecutor<Student> {
    Optional<Student> findByStudentIdAndActiveStatus(long id, int activeStatus);

    List<Student> findByActiveStatus(int activeStatus);
}
