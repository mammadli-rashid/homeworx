package com.safitech.homeworx.criteria;

import com.safitech.homeworx.constant.ContactType;
import com.safitech.homeworx.model.nonvalidated.CareerDetail;
import com.safitech.homeworx.model.nonvalidated.EducationDetail;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Component
public class TeacherCriteria {

    public Criteria fullNameLike(String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return new Criteria();
        }
        return Criteria.where("fullName").regex(".*" + fullName + ".*", "i");
    }

    public Criteria contactsContains(Map<ContactType, String> contacts) {
        if (contacts == null || contacts.isEmpty()) {
            return new Criteria();
        }
        Criteria[] criteriaArray = contacts.entrySet().stream()
                .filter(entry -> StringUtils.isNotBlank(entry.getValue()))
                .map(entry -> Criteria.where("contacts." + entry.getKey().name())
                        .regex(".*" + entry.getValue() + ".*", "i"))
                .toArray(Criteria[]::new);
        return criteriaArray.length > 0 ? new Criteria().andOperator(criteriaArray) : new Criteria();
    }

    public Criteria birthDateEquals(LocalDate birthDate) {
        if (birthDate == null) {
            return new Criteria();
        }
        return Criteria.where("birthDate").is(birthDate);
    }

    public Criteria teachingSubjectsIn(Set<String> teachingSubjects) {
        if (teachingSubjects == null || teachingSubjects.isEmpty()) {
            return new Criteria();
        }
        Criteria[] criteriaArray = teachingSubjects.stream()
                .filter(StringUtils::isNotBlank)
                .map(subject -> Criteria.where("teachingSubjects").regex(".*" + subject + ".*", "i"))
                .toArray(Criteria[]::new);
        return criteriaArray.length > 0 ? new Criteria().orOperator(criteriaArray) : new Criteria();
    }

    public Criteria educationHistoryEquals(Set<EducationDetail> educationHistory) {
        if (educationHistory == null || educationHistory.isEmpty()) {
            return new Criteria();
        }

        Criteria[] criteriaArray = educationHistory.stream()
                .map(educationDetail -> {
                    Criteria criteria = new Criteria();
                    if (StringUtils.isNotBlank(educationDetail.getInstitution())) {
                        criteria.and("institution").regex(".*" + educationDetail.getInstitution() + ".*", "i");
                    }
                    if (StringUtils.isNotBlank(educationDetail.getFaculty())) {
                        criteria.and("faculty").regex(".*" + educationDetail.getFaculty() + ".*", "i");
                    }
                    if (StringUtils.isNotBlank(educationDetail.getSpecialization())) {
                        criteria.and("specialization").regex(".*" + educationDetail.getSpecialization() + ".*", "i");
                    }
                    if (educationDetail.getEducationLevel() != null) {
                        criteria.and("educationLevel").is(educationDetail.getEducationLevel());
                    }
                    if (educationDetail.getStartDate() != null) {
                        criteria.and("startDate").is(educationDetail.getStartDate());
                    }
                    if (educationDetail.getEndDate() != null) {
                        criteria.and("endDate").is(educationDetail.getEndDate());
                    }
                    return Criteria.where("educationHistory").elemMatch(criteria);
                })
                .toArray(Criteria[]::new);

        return criteriaArray.length > 0 ? new Criteria().andOperator(criteriaArray) : new Criteria();
    }

    public Criteria workHistoryEquals(Set<CareerDetail> workHistory) {
        if (workHistory == null || workHistory.isEmpty()) {
            return new Criteria();
        }

        Criteria[] criteriaArray = workHistory.stream()
                .map(careerDetail -> {
                    Criteria criteria = new Criteria();
                    if (StringUtils.isNotBlank(careerDetail.getWorkplace())) {
                        criteria.and("workplace").regex(".*" + careerDetail.getWorkplace() + ".*", "i");
                    }
                    if (StringUtils.isNotBlank(careerDetail.getOccupation())) {
                        criteria.and("occupation").regex(".*" + careerDetail.getOccupation() + ".*", "i");
                    }
                    if (careerDetail.getStartDate() != null) {
                        criteria.and("startDate").is(careerDetail.getStartDate());
                    }
                    if (careerDetail.getEndDate() != null) {
                        criteria.and("endDate").is(careerDetail.getEndDate());
                    }
                    return Criteria.where("workHistory").elemMatch(criteria);
                })
                .toArray(Criteria[]::new);

        return criteriaArray.length > 0 ? new Criteria().andOperator(criteriaArray) : new Criteria();
    }

    public Criteria activeStatusEquals(int activeStatus) {
        return Criteria.where("activeStatus").is(activeStatus);
    }

}
