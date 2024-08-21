package com.safitech.homeworx.mapper;

import com.safitech.homeworx.dto.ReqStudent;
import com.safitech.homeworx.dto.RespStudent;
import com.safitech.homeworx.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    Student toStudent(ReqStudent reqStudent);

    RespStudent toRespStudent(Student student);

    ReqStudent toReqStudent(Student student);

}
