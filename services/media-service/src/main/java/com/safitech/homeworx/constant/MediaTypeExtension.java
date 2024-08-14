package com.safitech.homeworx.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MediaTypeExtension {
    JPG("image/jpeg", ".jpg"),
    PNG("image/png", ".png"),
    PDF("application/pdf", ".pdf"),
    DOC("application/msword", ".doc"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx"),
    XLS("application/vnd.ms-excel", ".xls"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx"),
    MP3("audio/mpeg", ".mp3"),
    MP4("video/mp4", ".mp4"),
    AVI("video/x-msvideo", ".avi"),
    MOV("video/quicktime", ".mov"),
    WAV("audio/wav", ".wav"),
    JSON("application/json", ".json"),
    XML("application/xml", ".xml"),
    ZIP("application/zip", ".zip"),
    RAR("application/x-rar-compressed", ".rar"),
    HTML("text/html", ".html");

    private final String fileType;
    private final String extension;
}
