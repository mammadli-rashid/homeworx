package com.safitech.homeworx.controller;

import com.safitech.homeworx.mapper.ResponseMapper;
import com.safitech.homeworx.model.ReqTeacher;
import com.safitech.homeworx.model.RespTeacher;
import com.safitech.homeworx.model.Response;
import com.safitech.homeworx.model.nonvalidated.TeacherModel;
import com.safitech.homeworx.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {
    private final TeacherService teacherService;
    private final ResponseMapper<RespTeacher> respTeacherMapper;
    private final ResponseMapper<List<RespTeacher>> listRespTeacherMapper;

    @PostMapping("/search")
    public ResponseEntity<List<RespTeacher>> searchTeachers(@RequestParam(name = "page", required = false, defaultValue = "0") int page, @RequestParam(name = "size", required = false, defaultValue = "10") int size, @RequestParam(name = "sortField", required = false, defaultValue = "teacherId") List<String> sortFields, @RequestParam(name = "sortDirection", required = false, defaultValue = "asc") List<String> sortDirections, @RequestBody TeacherModel teacherModel) {
        log.info("Received request to search teachers with page={}, size={}, sortFields={}, sortDirections={}, teacherModel={}", page, size, sortFields, sortDirections, teacherModel);
        Response<List<RespTeacher>> response = teacherService.searchTeachers(page, size, sortFields, sortDirections, teacherModel);
        log.info("Returned {} search results: ", response.getData().size());
        return listRespTeacherMapper.toResponseEntity(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<RespTeacher> getTeacherById(@PathVariable String id) {
        log.info("Received request to get teacher by ID: {}", id);
        Response<RespTeacher> response = teacherService.getTeacherById(id);
        log.info("Returned details for teacher id: {}", id);
        return respTeacherMapper.toResponseEntity(response);
    }

    @PostMapping
    public ResponseEntity<RespTeacher> addTeacher(@RequestBody @Valid ReqTeacher reqTeacher) {
        log.info("Received request to create a new teacher: {}", reqTeacher);
        Response<RespTeacher> response = teacherService.addTeacher(reqTeacher);
        log.info("Created new teacher with id: {}", response.getData().getTeacherId());
        return respTeacherMapper.toResponseEntity(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<RespTeacher> updateTeacher(@PathVariable String id, @RequestBody @Valid ReqTeacher reqTeacher) {
        log.info("Received request to update teacher by ID: {}", id);
        Response<RespTeacher> response = teacherService.updateTeacher(id, reqTeacher);
        log.info("Updated teacher with id: {}", response.getData().getTeacherId());
        return respTeacherMapper.toResponseEntity(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<RespTeacher> deleteStudent(@PathVariable String id) {
        log.info("Received request for deleting teacher with id: {}", id);
        Response<RespTeacher> response = teacherService.deleteTeacher(id);
        log.info("Teacher deleted successfully with id: {}", id);
        return respTeacherMapper.toResponseEntity(response);
    }

}
