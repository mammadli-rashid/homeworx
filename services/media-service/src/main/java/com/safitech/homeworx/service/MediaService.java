package com.safitech.homeworx.service;

import com.safitech.homeworx.constant.ActiveStatus;
import com.safitech.homeworx.constant.MediaTypeExtension;
import com.safitech.homeworx.dto.RespDownload;
import com.safitech.homeworx.dto.RespMedia;
import com.safitech.homeworx.entity.Media;
import com.safitech.homeworx.exception.MediaNotFoundException;
import com.safitech.homeworx.mapper.MediaMapper;
import com.safitech.homeworx.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaService {
    @Value("${media.upload.dir}")
    private String uploadDirectory;

    private final MediaRepository mediaRepository;

    private static final Map<String, String> MEDIA_TYPE_EXTENSIONS = new HashMap<>();

    static {
        for (MediaTypeExtension mediaTypeExtension : MediaTypeExtension.values()) {
            MEDIA_TYPE_EXTENSIONS.put(mediaTypeExtension.getFileType(), mediaTypeExtension.getExtension());
        }
    }

    public RespMedia upload(MultipartFile file) throws IOException {
        log.info("Starting to get file details...");

        String fileName = generateFileName();
        String fileType = file.getContentType();
        String fileExtension = MEDIA_TYPE_EXTENSIONS.get(fileType);
        long fileSize = file.getSize();

        Path uploadPath = Paths.get(uploadDirectory);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filesPath = uploadPath.resolve(fileName.concat(fileExtension));

        while (Files.exists(filesPath)) {
            fileName = generateFileName();
        }

        filesPath = uploadPath.resolve(fileName.concat(fileExtension));

        file.transferTo(filesPath);
        log.info("File successfully stored in the local storage: {}", filesPath);

        log.info("Starting to persist media metadata into database...");
        long mediaId = generateMediaId();
        while (mediaRepository.findByMediaIdAndActiveStatus(mediaId, ActiveStatus.ACTIVE.getValue()).isPresent()) {
            mediaId = generateMediaId();
        }
        Media media = Media.builder()
                .mediaId(mediaId)
                .fileName(fileName)
                .fileType(file.getContentType())
                .filePath(filesPath.toFile().getAbsolutePath())
                .fileSize(fileSize)
                .build();
        Media savedMedia = mediaRepository.save(media);
        log.info("Media metadata successfully stored in the database: {}", media);

        return MediaMapper.INSTANCE.toRespMedia(savedMedia);
    }

    public RespDownload download(long mediaId) throws IOException {
        log.info("Starting to download...");

        Media media = findMediaById(mediaId);

        Path filePath = Paths.get(media.getFilePath());

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found at path: " + filePath);
        }

        byte[] fileContent = Files.readAllBytes(filePath);
        String fileName = media.getFileName();
        MediaType mediaType = MediaType.valueOf(media.getFileType());

        log.info("File successfully downloaded: {}", filePath);

        return new RespDownload(fileName, fileContent, mediaType);
    }

    public void deleteMedia(long mediaId) {
        Media media = findMediaById(mediaId);
        media.setActiveStatus(ActiveStatus.INACTIVE.getValue());
        mediaRepository.save(media);
    }

    private String generateFileName() {
        UUID uuid = UUID.randomUUID();
        String name = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(uuid.toString().getBytes(StandardCharsets.UTF_8))
                .replaceAll("[^a-zA-Z]", "");
        return name.length() > 128 ? name.substring(0, 128) : name;
    }

    private long generateMediaId() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }

    private Media findMediaById(long mediaId) {
        return mediaRepository.findByMediaIdAndActiveStatus(mediaId, ActiveStatus.ACTIVE.getValue())
                .orElseThrow(() -> new MediaNotFoundException(mediaId));
    }

}
