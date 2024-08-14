package com.safitech.homeworx.entity.listener;

import com.safitech.homeworx.entity.Media;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class MediaEntityListener extends AbstractMongoEventListener<Media> {
    private final HttpServletRequest request;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Media> event) {
        Media media = event.getSource();
        if (media.getUploadDate() == null) {
            media.setUploadDate(LocalDateTime.now());
            media.setUploaderUserId(getCurrentUserId());
        } else {
            media.setModifiedDate(LocalDateTime.now());
        }
    }

    private String getCurrentUserId() {
        return request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous";
    }
}
