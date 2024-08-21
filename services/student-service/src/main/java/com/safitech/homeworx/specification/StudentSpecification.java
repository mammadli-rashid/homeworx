package com.safitech.homeworx.specification;

import com.safitech.homeworx.constant.ActiveStatus;
import com.safitech.homeworx.entity.Student;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class StudentSpecification {

    public Specification<Student> fullNameLike(String fullName) {
        return (Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            String pattern = "%" + fullName.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("fullName")), pattern);
        };
    }

    public Specification<Student> emailLike(String email) {
        return (Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            String pattern = "%" + email.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("email")), pattern);
        };
    }

    public Specification<Student> primaryMobileNumberLike(String primaryMobileNumber) {
        return (Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            String pattern = "%" + primaryMobileNumber.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("primaryMobileNumber")), pattern);
        };
    }

    public Specification<Student> secondaryMobileNumberLike(String secondaryMobileNumber) {
        return (Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            String pattern = "%" + secondaryMobileNumber.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("secondaryMobileNumber")), pattern);
        };
    }

    public Specification<Student> addressLike(String address) {
        return (Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            String pattern = "%" + address.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("address")), pattern);
        };
    }

    public Specification<Student> dateOfBirthEquals(LocalDate dateOfBirth) {
        return (Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
                cb.equal(root.get("dateOfBirth"), dateOfBirth);
    }

    public Specification<Student> activeStatusEquals(ActiveStatus activeStatus) {
        return (Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
                cb.equal(root.get("activeStatus"), activeStatus.getValue());
    }

}
