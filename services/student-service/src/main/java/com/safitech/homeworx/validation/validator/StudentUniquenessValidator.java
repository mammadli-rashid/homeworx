package com.safitech.homeworx.validation.validator;

import com.safitech.homeworx.constant.ActiveStatus;
import com.safitech.homeworx.entity.Student;
import com.safitech.homeworx.exception.DuplicateEmailException;
import com.safitech.homeworx.exception.DuplicatePrimaryMobileNumberException;
import com.safitech.homeworx.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentUniquenessValidator {
    private final StudentRepository studentRepository;

    public void checkStudentEmailIsUnique(Student checkedStudent) {
        String email = checkedStudent.getEmail();
        Long studentId = checkedStudent.getStudentId();

        boolean emailExists = studentRepository.findByActiveStatus(ActiveStatus.ACTIVE.getValue())
                .stream()
                .anyMatch(student -> student.getEmail().equalsIgnoreCase(email) && !student.getStudentId().equals(studentId));

        if (emailExists) {
            throw new DuplicateEmailException(email);
        }
    }

    public void checkStudentPrimaryMobileIsUnique(Student checkedStudent) {
        String primaryMobileNumber = checkedStudent.getPrimaryMobileNumber();
        Long studentId = checkedStudent.getStudentId();

        boolean primaryMobileNumberExists = studentRepository.findByActiveStatus(ActiveStatus.ACTIVE.getValue())
                .stream()
                .anyMatch(student -> student.getPrimaryMobileNumber().equalsIgnoreCase(primaryMobileNumber)
                        && !student.getStudentId().equals(studentId));

        if (primaryMobileNumberExists) {
            throw new DuplicatePrimaryMobileNumberException(primaryMobileNumber);
        }
    }
}
