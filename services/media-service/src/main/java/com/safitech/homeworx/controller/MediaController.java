package com.safitech.homeworx.controller;

import com.safitech.homeworx.dto.RespDownload;
import com.safitech.homeworx.dto.RespMedia;
import com.safitech.homeworx.service.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("media")
public class MediaController {
    private final MediaService mediaService;

    @PostMapping
    public ResponseEntity<RespMedia> uploadMedia(
            @RequestParam(value = "file") MultipartFile file) throws IOException {
        log.info("Received request for uploading media... Size: {} bytes", file.getSize());
        RespMedia respMedia = mediaService.upload(file);
        log.info("Media uploaded successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(respMedia);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<byte[]> downloadMedia(@PathVariable("id") long mediaId) throws IOException {
        log.info("Received request for downloading media with id: {}", mediaId);
        RespDownload respDownload = mediaService.download(mediaId);
        log.info("Media downloaded successfully.");
        return ResponseEntity.ok()
                .contentType(respDownload.getMediaType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + respDownload.getFileName() + "\"")
                .body(respDownload.getData());
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> deleteMedia(@PathVariable("id") long mediaId) throws IOException {
        log.info("Received request for deleting media with id: {}", mediaId);
        mediaService.deleteMedia(mediaId);
        log.info("Media deleted successfully.");
        return ResponseEntity.noContent().build();
    }

}
