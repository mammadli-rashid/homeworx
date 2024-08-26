package com.safitech.homeworx.validation.validator;

import com.safitech.homeworx.constant.ActiveStatus;
import com.safitech.homeworx.constant.ContactType;
import com.safitech.homeworx.entity.Teacher;
import com.safitech.homeworx.exception.DuplicateContactException;
import com.safitech.homeworx.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeacherUniquenessValidator {
    private final TeacherRepository teacherRepository;

    public void checkContactDetailsUniqueness(Teacher teacher) {
        String teacherId = teacher.getTeacherId();
        List<Teacher> allTeachers = teacherId == null ? teacherRepository.findAllByActiveStatus(ActiveStatus.ACTIVE.getValue())
                : teacherRepository.findAllByActiveStatusAndTeacherIdNot(ActiveStatus.ACTIVE.getValue(), teacherId);
        for (Teacher currentTeacher : allTeachers) {
            for (ContactType contactType : ContactType.values()) {
                if (contactType == ContactType.OTHER) {
                    continue;
                }
                String contactValue = teacher.getContacts().get(contactType);
                if (contactValue != null) {
                    if (currentTeacher.getContacts().get(contactType)
                            .equalsIgnoreCase(contactValue)) {
                        throw new DuplicateContactException(contactType, contactValue);
                    }
                }
            }
        }
    }

}
