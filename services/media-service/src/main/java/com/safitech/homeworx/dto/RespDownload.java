package com.safitech.homeworx.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RespDownload {
    String fileName;
    byte[] data;
    MediaType mediaType;
}
