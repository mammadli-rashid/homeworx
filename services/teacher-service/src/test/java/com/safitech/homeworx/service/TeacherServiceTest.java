package com.safitech.homeworx.service;

import com.safitech.homeworx.constant.ActiveStatus;
import com.safitech.homeworx.constant.ResponseStatus;
import com.safitech.homeworx.criteria.TeacherCriteria;
import com.safitech.homeworx.criteria.TeacherCriteriaBuilder;
import com.safitech.homeworx.entity.Teacher;
import com.safitech.homeworx.exception.InvalidSortFieldException;
import com.safitech.homeworx.exception.TeacherNotFoundException;
import com.safitech.homeworx.mapper.TeacherMapper;
import com.safitech.homeworx.model.ReqTeacher;
import com.safitech.homeworx.model.RespTeacher;
import com.safitech.homeworx.model.Response;
import com.safitech.homeworx.model.nonvalidated.TeacherModel;
import com.safitech.homeworx.repository.TeacherRepository;
import com.safitech.homeworx.validation.validator.TeacherUniquenessValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {
    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private TeacherUniquenessValidator teacherUniquenessValidator;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;


    @InjectMocks
    private TeacherService teacherService;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private TeacherMapper teacherMapper;


    @Test
    public void testAddTeacher_Success() {
        // Arrange
        ReqTeacher reqTeacher = new ReqTeacher();
        Teacher teacher = new Teacher();
        RespTeacher respTeacher = new RespTeacher();


        when(teacherRepository.save(teacher)).thenReturn(teacher);


        // Mock RedisTemplate and ValueOperations
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // Act
        Response<RespTeacher> response = teacherService.addTeacher(reqTeacher);

        // Assert
        assertNotNull(response);
        assertEquals(ResponseStatus.ENTITY_CREATED, response.getStatus());
        assertEquals(respTeacher, response.getData());

        // Verify interactions
        verify(teacherUniquenessValidator, times(1)).checkContactDetailsUniqueness(teacher);
        verify(teacherRepository, times(1)).save(teacher);
        verify(redisTemplate, times(1)).opsForValue();
    }


    @Test
    public void testGetTeacherById_CacheHit() {
        String teacherId = "123";
        RespTeacher cachedTeacher = new RespTeacher();
        cachedTeacher.setTeacherId(teacherId);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        when(redisTemplate.opsForValue().get(anyString())).thenReturn(cachedTeacher);

        Response<RespTeacher> response = teacherService.getTeacherById(teacherId);

        assertNotNull(response);
        assertEquals(ResponseStatus.OPERATION_SUCCESS, response.getStatus());
        assertEquals(teacherId, response.getData().getTeacherId());
        verify(teacherRepository, times(0)).findByTeacherIdAndActiveStatus(anyString(), anyInt());
    }

    @Test
    public void testGetTeacherById_CacheMiss() {
        String teacherId = "123";
        Teacher teacher = new Teacher();
        teacher.setTeacherId(teacherId);
        RespTeacher respTeacher = new RespTeacher();
        respTeacher.setTeacherId(teacherId);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);


        when(teacherRepository.findByTeacherIdAndActiveStatus(anyString(), anyInt())).thenReturn(Optional.of(teacher));

        Response<RespTeacher> response = teacherService.getTeacherById(teacherId);

        assertNotNull(response);
        assertEquals(ResponseStatus.OPERATION_SUCCESS, response.getStatus());
        assertEquals(teacherId, response.getData().getTeacherId());
        verify(teacherRepository, times(1)).findByTeacherIdAndActiveStatus(teacherId, ActiveStatus.ACTIVE.getValue());
    }

    @Test
    public void testGetTeacherById_TeacherNotFound() {
        String teacherId = "123";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        when(redisTemplate.opsForValue().get(anyString())).thenReturn(null);
        when(teacherRepository.findByTeacherIdAndActiveStatus(teacherId, ActiveStatus.ACTIVE.getValue())).thenReturn(Optional.empty());

        assertThrows(TeacherNotFoundException.class, () -> teacherService.getTeacherById(teacherId));

        verify(valueOperations, times(1)).get(String.valueOf(teacherId));
        verify(teacherRepository, times(1)).findByTeacherIdAndActiveStatus(teacherId, ActiveStatus.ACTIVE.getValue());
    }

    @Test
    public void testUpdateTeacher_Success() {
        // Arrange
        String id = "12345";

        // Create different details
        ReqTeacher reqTeacher = new ReqTeacher();
        reqTeacher.setFullName("newValue"); // Ensure this is different from current details

        Teacher currentTeacher = new Teacher();
        currentTeacher.setFullName("oldValue"); // Original details

        RespTeacher respTeacher = new RespTeacher();

        // Configure mocks
        when(teacherRepository.findByTeacherIdAndActiveStatus(id, ActiveStatus.ACTIVE.getValue()))
                .thenReturn(Optional.of(currentTeacher));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(currentTeacher); // Use any(Teacher.class) for more flexibility

        when(valueOperations.get(id)).thenReturn(respTeacher); // Ensure this returns a non-null value to trigger the cache update
        when(redisTemplate.delete(id)).thenReturn(true);
        // Mock Redis operations
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // Act
        Response<RespTeacher> response = teacherService.updateTeacher(id, reqTeacher);

        // Assert
        assertNotNull(response);
        assertEquals(ResponseStatus.OPERATION_SUCCESS, response.getStatus());

        // Verify interactions
        verify(teacherRepository).findByTeacherIdAndActiveStatus(id, ActiveStatus.ACTIVE.getValue());
        verify(teacherRepository).save(any(Teacher.class));
        verify(valueOperations).get(id);
        verify(redisTemplate).delete(id);// Ensure get is called
    }


    @Test
    public void testUpdateTeacher_DetailsUnchanged() {
        // Arrange
        String id = "12345";
        ReqTeacher reqTeacher = new ReqTeacher();
        Teacher currentTeacher = new Teacher();

        when(teacherRepository.findByTeacherIdAndActiveStatus(id, ActiveStatus.ACTIVE.getValue()))
                .thenReturn(Optional.of(currentTeacher));

        // Act
        Response<RespTeacher> response = teacherService.updateTeacher(id, reqTeacher);

        // Assert
        assertNotNull(response);
        assertEquals(ResponseStatus.NOT_MODIFIED, response.getStatus());

        verify(teacherRepository, times(1)).findByTeacherIdAndActiveStatus(id, ActiveStatus.ACTIVE.getValue());
        verifyNoMoreInteractions(teacherRepository);
        verifyNoMoreInteractions(teacherMapper);

    }

    @Test
    public void testUpdateTeacher_NotFound() {
        // Arrange
        String id = "12345";
        ReqTeacher reqTeacher = new ReqTeacher();

        when(teacherRepository.findByTeacherIdAndActiveStatus(id, ActiveStatus.ACTIVE.getValue()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TeacherNotFoundException.class, () -> teacherService.updateTeacher(id, reqTeacher));

        verify(teacherRepository).findByTeacherIdAndActiveStatus(id, ActiveStatus.ACTIVE.getValue());
        verifyNoMoreInteractions(teacherRepository);
        // Ensure delete is called
    }

    @Test
    public void testDeleteTeacher_Success() {
        // Arrange
        String id = "12345";
        Teacher teacher = new Teacher();
        RespTeacher respTeacher = new RespTeacher();
        teacher.setActiveStatus(ActiveStatus.ACTIVE.getValue());

        when(teacherRepository.findByTeacherIdAndActiveStatus(id, ActiveStatus.ACTIVE.getValue()))
                .thenReturn(Optional.of(teacher));
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(id)).thenReturn(respTeacher); // Ensure this returns a non-null value to trigger the cache update
        when(redisTemplate.delete(id)).thenReturn(true);

        // Act
        Response<RespTeacher> response = teacherService.deleteTeacher(id);

        // Assert
        assertNotNull(response);
        assertEquals(ResponseStatus.NO_CONTENT, response.getStatus());

        // Verify interactions
        verify(teacherRepository).findByTeacherIdAndActiveStatus(id, ActiveStatus.ACTIVE.getValue());
        verify(teacherRepository).save(teacher);
        verify(redisTemplate.opsForValue()).get(id);
        verify(redisTemplate).delete(id);
    }

    @Test
    public void testDeleteTeacher_NotFound() {
        // Arrange
        String id = "12345";

        when(teacherRepository.findByTeacherIdAndActiveStatus(id, ActiveStatus.ACTIVE.getValue()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TeacherNotFoundException.class, () -> teacherService.deleteTeacher(id));

        // Verify interactions
        verify(teacherRepository).findByTeacherIdAndActiveStatus(id, ActiveStatus.ACTIVE.getValue());
        verifyNoMoreInteractions(teacherRepository);
        verifyNoInteractions(redisTemplate);
    }


}