package com.safitech.homeworx.mapper;

import com.safitech.homeworx.model.ReqTeacher;
import com.safitech.homeworx.model.RespTeacher;
import com.safitech.homeworx.entity.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeacherMapper {
    TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);

    Teacher toTeacher(ReqTeacher reqTeacher);

    ReqTeacher toReqTeacher(Teacher teacher);

    RespTeacher toRespTeacher(Teacher teacher);
}
