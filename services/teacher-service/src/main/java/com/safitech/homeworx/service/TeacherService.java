package com.safitech.homeworx.service;

import com.safitech.homeworx.constant.ActiveStatus;
import com.safitech.homeworx.constant.ResponseStatus;
import com.safitech.homeworx.criteria.TeacherCriteria;
import com.safitech.homeworx.entity.Teacher;
import com.safitech.homeworx.exception.TeacherNotFoundException;
import com.safitech.homeworx.mapper.TeacherMapper;
import com.safitech.homeworx.mapper.TeacherSortingMapper;
import com.safitech.homeworx.model.ReqTeacher;
import com.safitech.homeworx.model.RespTeacher;
import com.safitech.homeworx.model.Response;
import com.safitech.homeworx.model.nonvalidated.TeacherModel;
import com.safitech.homeworx.repository.TeacherRepository;
import com.safitech.homeworx.utility.SortingAndPaginationUtils;
import com.safitech.homeworx.criteria.TeacherCriteriaBuilder;
import com.safitech.homeworx.validation.validator.TeacherUniquenessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final TeacherCriteria teacherCriteria;
    private final TeacherUniquenessValidator teacherUniquenessValidator;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MongoTemplate mongoTemplate;

    public Response<List<RespTeacher>> searchTeachers(int page, int size,
                                                      List<String> sortFields,
                                                      List<String> sortDirections,
                                                      TeacherModel teacherModel) {
        // Log the method entry with parameters
        log.info("Starting search for teachers with parameters: page={}, size={}, sortFields={}, sortDirections={}, teacherModel={}",
                page, size, sortFields, sortDirections, teacherModel);

        // Map sort fields and log the mapped values
        List<String> mappedSortFields = TeacherSortingMapper.mapSortFields(sortFields);
        log.debug("Mapped sort fields: {}", mappedSortFields);

        // Build criteria
        TeacherCriteriaBuilder criteriaBuilder = new TeacherCriteriaBuilder(teacherCriteria);
        Criteria criteria = criteriaBuilder.build(teacherModel);
        log.debug("Built criteria: {}", criteria);

        // Create query and log the query details
        Query query = new Query().addCriteria(criteria);
        log.debug("Created query: {}", query);

        // Create pageable object and log the details
        Pageable pageable = SortingAndPaginationUtils.createPageRequest(page, size, mappedSortFields, sortDirections);
        query.with(pageable);
        log.debug("Created pageable: {}", pageable);

        // Execute query and log the result size
        List<RespTeacher> results = mongoTemplate.find(query, Teacher.class).stream()
                .map(TeacherMapper.INSTANCE::toRespTeacher)
                .collect(Collectors.toList());

        log.info("Search completed with {} results", results.size());

        return new Response<>(results, ResponseStatus.OPERATION_SUCCESS);
    }

    public Response<RespTeacher> getTeacherById(String teacherId) {
        log.info("Fetching teacher by ID: {}", teacherId);
        RespTeacher cachedTeacher = (RespTeacher) redisTemplate.opsForValue().get(teacherId);
        if (cachedTeacher != null) {
            log.debug("Fetched teacher from cache with id: {}", teacherId);
            return new Response<>(cachedTeacher, ResponseStatus.OPERATION_SUCCESS);
        }
        Teacher teacher = teacherRepository.findByTeacherIdAndActiveStatus(teacherId, ActiveStatus.ACTIVE.getValue()).orElseThrow(() -> new TeacherNotFoundException(teacherId));
        RespTeacher respTeacher = TeacherMapper.INSTANCE.toRespTeacher(teacher);
        log.info("Fetched teacher details: {}", respTeacher);
        redisTemplate.opsForValue().set(respTeacher.getTeacherId(), respTeacher, 10, TimeUnit.MINUTES);
        log.debug("Teacher cached with id: {}", teacherId);
        return new Response<>(respTeacher, ResponseStatus.OPERATION_SUCCESS);
    }

    public Response<RespTeacher> addTeacher(ReqTeacher reqTeacher) {
        log.info("Creating teacher with data: {}", reqTeacher);
        Teacher teacher = TeacherMapper.INSTANCE.toTeacher(reqTeacher);
        teacherUniquenessValidator.checkContactDetailsUniqueness(teacher);
        RespTeacher respTeacher = TeacherMapper.INSTANCE.toRespTeacher(teacherRepository.save(teacher));
        String teacherId = respTeacher.getTeacherId();
        log.info("Teacher persisted in database successfully with id: {}", teacherId);
        redisTemplate.opsForValue().set(respTeacher.getTeacherId(), respTeacher, 10, TimeUnit.MINUTES);
        log.debug("Teacher cached with id: {}", teacherId);
        return new Response<>(respTeacher, ResponseStatus.ENTITY_CREATED);
    }

    public Response<RespTeacher> updateTeacher(String id, ReqTeacher reqTeacher) {
        Teacher currentTeacher = teacherRepository.findByTeacherIdAndActiveStatus(id, ActiveStatus.ACTIVE.getValue()).orElseThrow(() -> new TeacherNotFoundException(id));
        if (reqTeacher.equals(TeacherMapper.INSTANCE.toReqTeacher(currentTeacher))) {
            log.info("Teacher is not updated due to same details with current details and operation successfully ended.");
            return new Response<>(TeacherMapper.INSTANCE.toRespTeacher(currentTeacher), ResponseStatus.NOT_MODIFIED);
        }
        log.info("Teacher is updating with id: {} and data: {}", id, reqTeacher);
        Teacher updatedTeacher = TeacherMapper.INSTANCE.toTeacher(reqTeacher);
        updatedTeacher.setTeacherId(id);
        teacherUniquenessValidator.checkContactDetailsUniqueness(updatedTeacher);
        RespTeacher respTeacher = TeacherMapper.INSTANCE.toRespTeacher(teacherRepository.save(updatedTeacher));
        log.info("Teacher has been updated with id: {}", id);
        if (redisTemplate.opsForValue().get(id) != null) {
            redisTemplate.delete(id);
            log.debug("Teacher has been deleted from cache with id: {} due to update", id);
        }
        redisTemplate.opsForValue().set(id, respTeacher, 10, TimeUnit.HOURS);
        log.debug("Teacher cached with id: {}", id);
        return new Response<>(respTeacher, ResponseStatus.OPERATION_SUCCESS);
    }

    public Response<RespTeacher> deleteTeacher(String id) {
        Teacher teacher = teacherRepository.findByTeacherIdAndActiveStatus(id, ActiveStatus.ACTIVE.getValue()).orElseThrow(() -> new TeacherNotFoundException(id));
        log.info("Deleting teacher with id: {}", id);
        teacher.setActiveStatus(ActiveStatus.DELETED.getValue());
        teacherRepository.save(teacher);
        log.info("Teacher has been deleted with id: {}", id);
        if (redisTemplate.opsForValue().get(id) != null) {
            redisTemplate.delete(id);
            log.debug("Teacher has been deleted from cache with id: {}", id);
        }
        return new Response<>(null, ResponseStatus.NO_CONTENT);
    }

}
