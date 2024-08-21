package com.safitech.homeworx.controller;

import com.safitech.homeworx.dto.ReqSpecPaginationSorting;
import com.safitech.homeworx.dto.ReqStudent;
import com.safitech.homeworx.dto.RespStudent;
import com.safitech.homeworx.dto.Response;
import com.safitech.homeworx.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<RespStudent>> getAllStudents(
            @RequestParam(name = "fullName", required = false) String fullName,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "primaryMobileNumber", required = false) String primaryMobileNumber,
            @RequestParam(name = "secondaryMobileNumber", required = false) String secondaryMobileNumber,
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "dateOfBirth", required = false) LocalDate dateOfBirth,
            @RequestParam(name = "offset", defaultValue = "0", required = false) Integer offset,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(name = "sortField", defaultValue = "studentId", required = false) List<String> sortFields,
            @RequestParam(name = "sortDirection", defaultValue = "asc", required = false) List<String> sortDirections
    ) {
        log.info("Received request to get all students matching entered conditions");
        ReqSpecPaginationSorting specPaginationSorting = ReqSpecPaginationSorting.builder()
                .fullName(fullName)
                .email(email)
                .primaryMobileNumber(primaryMobileNumber)
                .secondaryMobileNumber(secondaryMobileNumber)
                .address(address)
                .dateOfBirth(dateOfBirth)
                .offset(offset)
                .pageSize(pageSize)
                .sortFields(sortFields)
                .sortDirections(sortDirections)
                .build();
        log.debug("Pagination and sorting parameters: {}", specPaginationSorting);
        Response<List<RespStudent>> response = studentService.getAllStudents(specPaginationSorting);
        log.info("Returned {} students", response.getData().size());
        return new ResponseEntity<>(response.getData(), HttpStatus.valueOf(response.getStatus().getHttpCode()));
    }

    @GetMapping("{id}")
    public ResponseEntity<RespStudent> getStudentById(@PathVariable int id) {
        log.info("Received request to get student by ID: {}", id);
        Response<RespStudent> response = studentService.getStudentById(id);
        log.info("Returned details for student id: {}", id);
        return new ResponseEntity<>(response.getData(), HttpStatus.valueOf(response.getStatus().getHttpCode()));
    }

    @PostMapping
    public ResponseEntity<RespStudent> addStudent(@RequestBody ReqStudent reqStudent) {
        log.info("Received request to create a new student: {}", reqStudent);
        Response<RespStudent> response = studentService.addStudent(reqStudent);
        log.info("Created new student with id: {}", response.getData().getStudentId());
        return new ResponseEntity<>(response.getData(), HttpStatus.valueOf(response.getStatus().getHttpCode()));
    }

    @PutMapping("{id}")
    public ResponseEntity<RespStudent> updateStudent(@PathVariable long id, @RequestBody ReqStudent reqStudent) {
        log.info("Received request to update student with id: {} and data: {}", id, reqStudent);
        Response<RespStudent> response = studentService.updateStudent(id, reqStudent);
        log.info("Student updated with id: {}", id);
        return new ResponseEntity<>(response.getData(), HttpStatus.valueOf(response.getStatus().getHttpCode()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<RespStudent> deleteStudent(@PathVariable long id) {
        log.info("Received request for deleting student with id: {}", id);
        Response<?> response = studentService.deleteStudent(id);
        log.info("Student deleted successfully.");
        return new ResponseEntity<>(null, HttpStatus.valueOf(response.getStatus().getHttpCode()));
    }

}
