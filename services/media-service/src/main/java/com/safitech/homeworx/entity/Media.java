package com.safitech.homeworx.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "media")
public class Media {
    @Id
    long mediaId;
    String fileName;
    String fileType;
    long fileSize;
    String filePath;
    LocalDateTime uploadDate;
    LocalDateTime modifiedDate;
    String uploaderUserId;
    @Builder.Default
    int activeStatus = 1;
}
