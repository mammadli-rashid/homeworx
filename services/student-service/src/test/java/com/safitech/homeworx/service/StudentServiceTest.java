package com.safitech.homeworx.service;

import com.safitech.homeworx.constant.ActiveStatus;
import com.safitech.homeworx.constant.ResponseStatus;
import com.safitech.homeworx.dto.ReqSpecPaginationSorting;
import com.safitech.homeworx.dto.ReqStudent;
import com.safitech.homeworx.dto.RespStudent;
import com.safitech.homeworx.dto.Response;
import com.safitech.homeworx.entity.Student;
import com.safitech.homeworx.exception.DuplicateEmailException;
import com.safitech.homeworx.exception.DuplicatePrimaryMobileNumberException;
import com.safitech.homeworx.exception.StudentNotFoundException;
import com.safitech.homeworx.mapper.StudentMapper;
import com.safitech.homeworx.repository.StudentRepository;
import com.safitech.homeworx.specification.StudentSpecification;
import com.safitech.homeworx.utility.PageableUtils;
import com.safitech.homeworx.utility.SpecificationUtils;
import com.safitech.homeworx.validation.validator.StudentRequestValidator;
import com.safitech.homeworx.validation.validator.StudentUniquenessValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;


@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private StudentRequestValidator studentRequestValidator;
    @Mock
    private StudentSpecification studentSpecification;
    @Mock
    private PageableUtils pageableUtils;
    @Mock
    private SpecificationUtils specificationUtils;
    @Mock
    private StudentMapper studentMapper;
    @Mock
    private StudentUniquenessValidator studentUniquenessValidator;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private StudentService studentService;


    @Test
    void testGetAllStudents_Success() {
        // Arrange
        ReqSpecPaginationSorting specPaginationSorting = new ReqSpecPaginationSorting();
        specPaginationSorting.setOffset(0);
        specPaginationSorting.setPageSize(10);
        specPaginationSorting.setSortFields(List.of("fullName"));
        specPaginationSorting.setSortDirections(List.of("ASC"));

        Student student = new Student(); // Mock Student entity

        Page<Student> studentPage = new PageImpl<>(List.of(student)); // Mocked page result

        // Mocking behavior
        doNothing().when(studentRequestValidator).validateSortFields(any());
        when(studentRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(studentPage);
        // Act
        Response<List<RespStudent>> response = studentService.getAllStudents(specPaginationSorting);

        // Assert
        assertEquals(1, response.getData().size());
        assertEquals(ResponseStatus.OPERATION_SUCCESS, response.getStatus());
        verify(studentRequestValidator, times(1)).validateSortFields(any());
        verify(studentRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void testGetStudentById_CacheHit() {
        long studentId = 1L;
        RespStudent cachedStudent = new RespStudent();
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(String.valueOf(studentId))).thenReturn(cachedStudent);

        Response<RespStudent> response = studentService.getStudentById(studentId);

        assertThat(response).isNotNull();
        assertThat(response.getData()).isEqualTo(cachedStudent);
        assertThat(response.getStatus()).isEqualTo(ResponseStatus.OPERATION_SUCCESS);
        verifyNoInteractions(studentRepository); // Verify that the repository is not called
    }

    @Test
    void testGetStudentById_CacheMiss() {
        long studentId = 1L;
        Student student = new Student();
        RespStudent respStudent = new RespStudent();
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // Stub the `get` method of the mock `ValueOperations`
        when(valueOperations.get(String.valueOf(studentId))).thenReturn(null);

        // Stub the `findByStudentIdAndActiveStatus` method of the mock `StudentRepository`
        when(studentRepository.findByStudentIdAndActiveStatus(studentId, ActiveStatus.ACTIVE.getValue()))
                .thenReturn(Optional.of(student));

        // Call the method to be tested
        Response<RespStudent> response = studentService.getStudentById(studentId);

        // Verify the results
        assertThat(response).isNotNull();
        assertThat(response.getData()).isEqualTo(respStudent);
        assertThat(response.getStatus()).isEqualTo(ResponseStatus.OPERATION_SUCCESS);

        // Verify interactions with mocks
        verify(valueOperations, times(1)).get(String.valueOf(studentId));
        verify(valueOperations, times(1)).set(String.valueOf(studentId), respStudent, 10, TimeUnit.MINUTES);
        verify(studentRepository, times(1)).findByStudentIdAndActiveStatus(studentId, ActiveStatus.ACTIVE.getValue());
    }

    @Test
    void testGetStudentById_StudentNotFound() {
        long studentId = 1L;
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(String.valueOf(studentId))).thenReturn(null);
        when(studentRepository.findByStudentIdAndActiveStatus(studentId, ActiveStatus.ACTIVE.getValue()))
                .thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.getStudentById(studentId));

        verify(valueOperations, times(1)).get(String.valueOf(studentId));
        verify(studentRepository, times(1)).findByStudentIdAndActiveStatus(studentId, ActiveStatus.ACTIVE.getValue());
    }

    @Test
    public void testCreateStudent_CreateSuccess() {
        // Arrange
        ReqStudent reqStudent = new ReqStudent();
        reqStudent.setEmail("TestEmail@domain.com");
        reqStudent.setPrimaryMobileNumber("1234567890");

        Student student = new Student();
        student.setEmail("testemail@domain.com");  // Converted to lowercase
        student.setPrimaryMobileNumber("1234567890");

        RespStudent respStudent = new RespStudent();
        respStudent.setStudentId(1L);

        // Mock the StudentMapper calls (if you're not using a static mapper)
        when(studentRepository.save(student)).thenReturn(student);

        // Mock RedisTemplate opsForValue() and the subsequent set() method
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);  // Return the mocked valueOperations

        // Mock caching behavior with correct arguments
        doNothing().when(valueOperations).set(anyString(), any(RespStudent.class), eq(10L), eq(TimeUnit.MINUTES));  // Allow any string and RespStudent

        // Act
        Response<RespStudent> response = studentService.addStudent(reqStudent);

        // Assert
        verify(studentUniquenessValidator).checkStudentEmailIsUnique(student);
        verify(studentUniquenessValidator).checkStudentPrimaryMobileIsUnique(student);
        verify(studentRepository).save(student);
        verify(redisTemplate).opsForValue();  // Ensure the opsForValue was called
        verify(valueOperations).set(anyString(), any(RespStudent.class), eq(10L), eq(TimeUnit.MINUTES));  // Verify with flexible matching
        assertNotNull(response);
        assertEquals(ResponseStatus.ENTITY_CREATED, response.getStatus());
        assertEquals("testemail@domain.com", student.getEmail());  // Ensure email was converted to lowercase
    }

    @Test
    public void testCreateStudent_PrimaryMobileAlreadyExists() {
        // Arrange
        ReqStudent reqStudent = new ReqStudent();
        reqStudent.setEmail("newemail@domain.com");
        reqStudent.setPrimaryMobileNumber("1234567890");

        Student student = new Student();
        student.setEmail("newemail@domain.com");
        student.setPrimaryMobileNumber("1234567890");

        doThrow(new DuplicatePrimaryMobileNumberException("Mobile number already exists"))
                .when(studentUniquenessValidator).checkStudentPrimaryMobileIsUnique(student);


        // Act & Assert
        assertThrows(DuplicatePrimaryMobileNumberException.class, () -> {
            studentService.addStudent(reqStudent);
        });

        verify(studentUniquenessValidator).checkStudentEmailIsUnique(student);
        verify(studentUniquenessValidator).checkStudentPrimaryMobileIsUnique(student);
        verify(studentRepository, never()).save(any());
        verify(redisTemplate, never()).opsForValue();
    }

    @Test
    public void testCreateStudent_EmailAlreadyExists() {
        // Arrange
        ReqStudent reqStudent = new ReqStudent();
        reqStudent.setEmail("existingemail@domain.com");

        Student student = new Student();
        student.setEmail("existingemail@domain.com");

        doThrow(new DuplicateEmailException("Email already exists"))
                .when(studentUniquenessValidator).checkStudentEmailIsUnique(student);


        // Act & Assert
        assertThrows(DuplicateEmailException.class, () -> {
            studentService.addStudent(reqStudent);
        });

        verify(studentUniquenessValidator).checkStudentEmailIsUnique(student);
        verify(studentRepository, never()).save(any());
        verify(redisTemplate, never()).opsForValue();
    }

    @Test
    void testUpdateStudent_Changes() {
        long studentId = 1L;

        // Mock input request with email
        ReqStudent reqStudent = new ReqStudent();
        reqStudent.setEmail("newemail@example.com"); // Ensure email is non-null

        // Create an existing student
        Student existingStudent = new Student();
        existingStudent.setStudentId(studentId);
        existingStudent.setEmail("oldemail@example.com"); // Ensure email is non-null

        // Mock the repository call
        when(studentRepository.findByStudentIdAndActiveStatus(studentId, ActiveStatus.ACTIVE.getValue()))
                .thenReturn(Optional.of(existingStudent));

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        // Mock save method
        when(studentRepository.save(any(Student.class))).thenReturn(existingStudent);

        // Mock Redis interactions
        when(redisTemplate.opsForValue().get(String.valueOf(studentId))).thenReturn(null); // Simulate cache miss

        // Ensure that the Redis get operation returns a non-null value
        RespStudent cachedStudent = new RespStudent();  // This is just to simulate that the cache has an existing value
        when(valueOperations.get(String.valueOf(studentId))).thenReturn(cachedStudent); // Simulate cache hit
        // Call the method under test
        Response<RespStudent> response = studentService.updateStudent(studentId, reqStudent);

        // Verify that Redis delete is called because the cache was not empty
        verify(redisTemplate, times(1)).delete(String.valueOf(studentId)); // Check that delete was invoked
        // Assert the response
        assertThat(response).isNotNull();
        // Verify repository interaction
        verify(studentRepository, times(1)).findByStudentIdAndActiveStatus(studentId, ActiveStatus.ACTIVE.getValue());
        verify(studentRepository, times(1)).save(any(Student.class)); // Verifying save was called
    }


    @Test
    void testUpdateStudent_NoChanges() {
        long studentId = 1L;
        ReqStudent reqStudent = new ReqStudent();
        Student currentStudent = new Student();
        RespStudent respStudent = new RespStudent();

        when(studentRepository.findByStudentIdAndActiveStatus(studentId, ActiveStatus.ACTIVE.getValue()))
                .thenReturn(Optional.of(currentStudent));

        Response<RespStudent> response = studentService.updateStudent(studentId, reqStudent);

        assertThat(response).isNotNull();
        assertThat(response.getData()).isEqualTo(respStudent);
        assertThat(response.getStatus()).isEqualTo(ResponseStatus.NOT_MODIFIED);

        verify(studentRepository, times(1)).findByStudentIdAndActiveStatus(studentId, ActiveStatus.ACTIVE.getValue());
        verifyNoInteractions(studentUniquenessValidator);
        verifyNoInteractions(redisTemplate);
    }

    @Test
    void testUpdateStudent_ThrowStudentNotFoundException() {
        long studentId = 1L;
        ReqStudent reqStudent = new ReqStudent();
        when(studentRepository.findByStudentIdAndActiveStatus(studentId, ActiveStatus.ACTIVE.getValue()))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(StudentNotFoundException.class)
                .isThrownBy(() -> studentService.updateStudent(studentId, reqStudent))
                .withMessage("Student not found with id: " + studentId);

        verify(studentRepository, times(1)).findByStudentIdAndActiveStatus(studentId, ActiveStatus.ACTIVE.getValue());
        verifyNoInteractions(redisTemplate);
    }


    @Test
    void testDeleteStudent_DeleteSuccess() {
        // Arrange
        long studentId = 1L;
        Student student = new Student();
        student.setStudentId(studentId);
        student.setActiveStatus(ActiveStatus.ACTIVE.getValue());
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        when(studentRepository.findByStudentIdAndActiveStatus(studentId, ActiveStatus.ACTIVE.getValue()))
                .thenReturn(Optional.of(student));

        when(redisTemplate.opsForValue().get(String.valueOf(studentId)))
                .thenReturn(new Object()); // Simulate the student being in cache

        // Act
        Response<?> response = studentService.deleteStudent(studentId);

        // Assert
        verify(studentRepository).save(student);
        assertEquals(ActiveStatus.DELETED.getValue(), student.getActiveStatus());
        verify(redisTemplate).delete(String.valueOf(studentId));
        assertEquals(ResponseStatus.NO_CONTENT, response.getStatus());
    }

    @Test
    void testDeleteStudent_NotInCache() {
        long studentId = 1L;
        Student student = new Student();
        student.setStudentId(studentId);
        student.setActiveStatus(ActiveStatus.ACTIVE.getValue());
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(studentRepository.findByStudentIdAndActiveStatus(studentId, ActiveStatus.ACTIVE.getValue()))
                .thenReturn(Optional.of(student));

        when(redisTemplate.opsForValue().get(String.valueOf(studentId)))
                .thenReturn(null); // Simulate the student NOT being in cache

        // Act
        Response<?> response = studentService.deleteStudent(studentId);

        // Assert
        verify(studentRepository).save(student);
        verify(redisTemplate, never()).delete(String.valueOf(studentId)); // No deletion from cache
        assertEquals(ResponseStatus.NO_CONTENT, response.getStatus());
    }

    @Test
    public void testDeleteStudent_StudentNotFound() {
        // Arrange
        long studentId = 1L;

        when(studentRepository.findByStudentIdAndActiveStatus(studentId, ActiveStatus.ACTIVE.getValue()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(StudentNotFoundException.class, () -> {
            studentService.deleteStudent(studentId);
        });
        verify(studentRepository, never()).save(any());
        verify(redisTemplate, never()).delete(anyString());
    }
}
