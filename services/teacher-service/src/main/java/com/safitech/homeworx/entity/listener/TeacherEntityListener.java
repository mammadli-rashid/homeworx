package com.safitech.homeworx.entity.listener;

import com.safitech.homeworx.entity.Teacher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TeacherEntityListener extends AbstractMongoEventListener<Teacher> {
    private final HttpServletRequest request;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Teacher> event) {
        Teacher teacher = event.getSource();
        if (teacher.getCreatedAt() == null) {
            teacher.setCreatedAt(LocalDateTime.now());
        } else {
            teacher.setLastModifiedAt(LocalDateTime.now());
        }
    }
}