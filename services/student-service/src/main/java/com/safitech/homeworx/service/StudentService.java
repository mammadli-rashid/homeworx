package com.safitech.homeworx.service;

import com.safitech.homeworx.constant.ActiveStatus;
import com.safitech.homeworx.constant.ResponseStatus;
import com.safitech.homeworx.dto.ReqSpecPaginationSorting;
import com.safitech.homeworx.dto.ReqStudent;
import com.safitech.homeworx.dto.RespStudent;
import com.safitech.homeworx.dto.Response;
import com.safitech.homeworx.entity.Student;
import com.safitech.homeworx.exception.StudentNotFoundException;
import com.safitech.homeworx.mapper.StudentMapper;
import com.safitech.homeworx.repository.StudentRepository;
import com.safitech.homeworx.specification.StudentSpecification;
import com.safitech.homeworx.utility.PageableUtils;
import com.safitech.homeworx.utility.SpecificationUtils;
import com.safitech.homeworx.validation.validator.StudentRequestValidator;
import com.safitech.homeworx.validation.validator.StudentUniquenessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentSpecification studentSpecification;
    private final StudentRequestValidator studentRequestValidator;
    private final StudentUniquenessValidator studentUniquenessValidator;
    private final RedisTemplate<String, Object> redisTemplate;

    public Response<List<RespStudent>> getAllStudents(ReqSpecPaginationSorting specPaginationSorting) {
        log.info("Fetching all regions matching the given conditions...");
        studentRequestValidator.validateSortFields(specPaginationSorting.getSortFields()); // Delegate to the validator
        Pageable pageable = PageableUtils.buildStudentPageable(
                specPaginationSorting.getOffset(),
                specPaginationSorting.getPageSize(),
                specPaginationSorting.getSortFields(),
                specPaginationSorting.getSortDirections());
        Specification<Student> specification = SpecificationUtils.buildStudentSpecification(
                specPaginationSorting.getFullName(),
                specPaginationSorting.getEmail(),
                specPaginationSorting.getPrimaryMobileNumber(),
                specPaginationSorting.getSecondaryMobileNumber(),
                specPaginationSorting.getAddress(),
                specPaginationSorting.getDateOfBirth(),
                studentSpecification);
        List<RespStudent> students = studentRepository.findAll(specification, pageable)
                .stream()
                .map(StudentMapper.INSTANCE::toRespStudent)
                .toList();
        log.debug("Fetched {} active students matching with given conditions", students.size());
        return new Response<>(students, ResponseStatus.OPERATION_SUCCESS);
    }

    public Response<RespStudent> getStudentById(long id) {
        log.info("Fetching student by ID: {}", id);
        RespStudent cachedStudent = (RespStudent) redisTemplate.opsForValue().get(String.valueOf(id));
        if (cachedStudent != null) {
            log.debug("Fetched student from cache with id: {}", id);
            return new Response<>(cachedStudent, ResponseStatus.OPERATION_SUCCESS);
        }
        Student student = studentRepository
                .findByStudentIdAndActiveStatus(id, ActiveStatus.ACTIVE.getValue())
                .orElseThrow(() -> new StudentNotFoundException(id));
        RespStudent respStudent = StudentMapper.INSTANCE.toRespStudent(student);
        log.debug("Fetched student details: {}", respStudent);
        redisTemplate.opsForValue().set(String.valueOf(id), respStudent, 10, TimeUnit.MINUTES);
        log.debug("Student cached with id: {}", id);
        return new Response<>(respStudent, ResponseStatus.OPERATION_SUCCESS);
    }

    public Response<RespStudent> addStudent(ReqStudent reqStudent) {
        log.info("Creating student with data: {}", reqStudent);
        Student student = StudentMapper.INSTANCE.toStudent(reqStudent);
        student.setEmail(student.getEmail().toLowerCase());
        studentUniquenessValidator.checkStudentEmailIsUnique(student);
        studentUniquenessValidator.checkStudentPrimaryMobileIsUnique(student);
        RespStudent respStudent = StudentMapper.INSTANCE.toRespStudent(studentRepository.save(student));
        long studentId = respStudent.getStudentId();
        log.info("Student saved with id: {}", studentId);
        redisTemplate.opsForValue().set(String.valueOf(studentId), respStudent, 10, TimeUnit.MINUTES);
        log.debug("Student cached with id: {}", studentId);
        return new Response<>(respStudent, ResponseStatus.ENTITY_CREATED);
    }

    public Response<RespStudent> updateStudent(long id, ReqStudent reqStudent) {
        Student currentStudent = studentRepository.findByStudentIdAndActiveStatus(id, ActiveStatus.ACTIVE.getValue())
                .orElseThrow(() -> new StudentNotFoundException(id));
        if (reqStudent.equals(StudentMapper.INSTANCE.toReqStudent(currentStudent))) {
            return new Response<>(StudentMapper.INSTANCE.toRespStudent(currentStudent), ResponseStatus.NOT_MODIFIED);
        }
        log.info("Student is updating with id: {} and data: {}", id, reqStudent);
        Student updatedStudent = StudentMapper.INSTANCE.toStudent(reqStudent);
        updatedStudent.setStudentId(id);
        updatedStudent.setEmail(updatedStudent.getEmail().toLowerCase());
        studentUniquenessValidator.checkStudentEmailIsUnique(updatedStudent);
        studentUniquenessValidator.checkStudentPrimaryMobileIsUnique(updatedStudent);
        RespStudent respStudent = StudentMapper.INSTANCE.toRespStudent(studentRepository.save(updatedStudent));
        log.info("Student has been updated with id: {}", id);
        if (redisTemplate.opsForValue().get(String.valueOf(id)) != null) {
            redisTemplate.delete(String.valueOf(id));
            log.debug("Student has been deleted from cache with id: {} due to update", id);
        }
        redisTemplate.opsForValue().set(String.valueOf(id), respStudent, 10, TimeUnit.HOURS);
        log.debug("Student cached with id: {}", id);
        return new Response<>(respStudent, ResponseStatus.OPERATION_SUCCESS);
    }

    public Response<?> deleteStudent(long id) {
        Student student = studentRepository.findByStudentIdAndActiveStatus(id, ActiveStatus.ACTIVE.getValue())
                .orElseThrow(() -> new StudentNotFoundException(id));
        student.setActiveStatus(ActiveStatus.DELETED.getValue());
        studentRepository.save(student);
        log.info("Student has been deleted with id: {}", id);
        if (redisTemplate.opsForValue().get(String.valueOf(id)) != null) {
            redisTemplate.delete(String.valueOf(id));
            log.debug("Student has been deleted from cache with id: {}", id);
        }
        return new Response<>(null, ResponseStatus.NO_CONTENT);
    }

}
