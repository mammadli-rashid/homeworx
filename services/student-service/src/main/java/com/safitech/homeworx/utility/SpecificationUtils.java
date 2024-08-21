package com.safitech.homeworx.utility;

import com.safitech.homeworx.constant.ActiveStatus;
import com.safitech.homeworx.entity.Student;
import com.safitech.homeworx.specification.StudentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SpecificationUtils {

    public static Specification<Student> buildStudentSpecification(String fullName, String email,
                                                                   String primaryMobileNumber, String secondaryMobileNumber,
                                                                   String address, LocalDate dateOfBirth,
                                                                   StudentSpecification studentSpecification) {
        Specification<Student> spec = Specification.where(null);

        if (fullName != null && !fullName.isBlank()) {
            spec = spec.and(studentSpecification.fullNameLike(fullName));
        }
        if (email != null && !email.isBlank()) {
            spec = spec.and(studentSpecification.emailLike(email));
        }
        if (primaryMobileNumber != null && !primaryMobileNumber.isBlank()) {
            spec = spec.and(studentSpecification.primaryMobileNumberLike(primaryMobileNumber));
        }
        if (secondaryMobileNumber != null && !secondaryMobileNumber.isBlank()) {
            spec = spec.and(studentSpecification.secondaryMobileNumberLike(secondaryMobileNumber));
        }
        if (address != null && !address.isBlank()) {
            spec = spec.and(studentSpecification.addressLike(address));
        }
        if (dateOfBirth != null) {
            spec = spec.and(studentSpecification.dateOfBirthEquals(dateOfBirth));
        }
        spec = spec.and(studentSpecification.activeStatusEquals(ActiveStatus.ACTIVE));

        return spec;
    }
}
