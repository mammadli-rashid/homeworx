package com.safitech.homeworx.criteria;

import com.safitech.homeworx.constant.ActiveStatus;
import com.safitech.homeworx.model.nonvalidated.TeacherModel;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;

public class TeacherCriteriaBuilder {

    private final TeacherCriteria teacherCriteria;

    public TeacherCriteriaBuilder(TeacherCriteria teacherCriteria) {
        this.teacherCriteria = teacherCriteria;
    }

    public Criteria build(TeacherModel teacherModel) {
        List<Criteria> criteriaList = new ArrayList<>();

        addIfNotEmpty(criteriaList, teacherCriteria.fullNameLike(teacherModel.getFullName()));
        addIfNotEmpty(criteriaList, teacherCriteria.contactsContains(teacherModel.getContacts()));
        addIfNotEmpty(criteriaList, teacherCriteria.birthDateEquals(teacherModel.getBirthdate()));
        addIfNotEmpty(criteriaList, teacherCriteria.teachingSubjectsIn(teacherModel.getTeachingSubjects()));
        addIfNotEmpty(criteriaList, teacherCriteria.educationHistoryEquals(teacherModel.getEducationHistory()));
        addIfNotEmpty(criteriaList, teacherCriteria.workHistoryEquals(teacherModel.getWorkHistory()));
        criteriaList.add(teacherCriteria.activeStatusEquals(ActiveStatus.ACTIVE.getValue()));

        return new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
    }

    private void addIfNotEmpty(List<Criteria> criteriaList, Criteria criteria) {
        if (!criteria.getCriteriaObject().isEmpty()) {
            criteriaList.add(criteria);
        }
    }
}

