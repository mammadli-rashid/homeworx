package com.safitech.homeworx.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RespMedia {
    long mediaId;
    String fileName;
    String fileType;
    Long fileSize;
    String filePath;
}
