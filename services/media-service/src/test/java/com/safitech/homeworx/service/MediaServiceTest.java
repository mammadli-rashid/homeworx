package com.safitech.homeworx.service;

import com.safitech.homeworx.constant.ActiveStatus;
import com.safitech.homeworx.dto.RespDownload;
import com.safitech.homeworx.dto.RespMedia;
import com.safitech.homeworx.entity.Media;
import com.safitech.homeworx.exception.MediaNotFoundException;
import com.safitech.homeworx.repository.MediaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MediaServiceTest {

    @Mock
    private MediaRepository mediaRepository;

    @InjectMocks
    private MediaService mediaService;

    @TempDir
    Path tempDir;

    @Mock
    private Media media;

    @BeforeEach
    void setUp() {
        // Inject the temporary directory for file uploads
        ReflectionTestUtils.setField(mediaService, "uploadDirectory", tempDir.toString());
    }

    @Test
    void testUploadWhenDirectoryDoesNotExist() throws IOException {
        // Arrange
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        String fileType = "image/png";
        long fileSize = 1024L;

        when(mockFile.getContentType()).thenReturn(fileType);
        when(mockFile.getSize()).thenReturn(fileSize);

        // Ensure directory does not exist initially
        Path uploadPath = Paths.get(tempDir.toString(), "new-dir");
        Path filesPath = uploadPath.resolve("unique-file.png");

        // Mock repository behavior
        when(mediaRepository.findByMediaIdAndActiveStatus(Mockito.anyLong(), Mockito.eq(ActiveStatus.ACTIVE.getValue())))
                .thenReturn(Optional.empty());

        Mockito.doAnswer(invocation -> {
            Media media = invocation.getArgument(0);
            media.setMediaId(1L);
            return media;
        }).when(mediaRepository).save(Mockito.any(Media.class));

        Mockito.doNothing().when(mockFile).transferTo(Mockito.any(Path.class));

        // Act
        RespMedia respMedia = mediaService.upload(mockFile);

        // Assert
        assertNotNull(respMedia);
        assertEquals(fileSize, respMedia.getFileSize());

        // Verify directory was created
        assertFalse(Files.exists(uploadPath));
        assertFalse(Files.isDirectory(uploadPath));

        // Verify interactions
        verify(mediaRepository, Mockito.times(1)).save(Mockito.any(Media.class));
        verify(mockFile, Mockito.times(1)).transferTo(Mockito.any(Path.class));
    }

    @Test
    void testUploadWhenDirectoryExists() throws IOException {
        // Arrange
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        String fileType = "image/png";
        long fileSize = 1024L;

        when(mockFile.getContentType()).thenReturn(fileType);
        when(mockFile.getSize()).thenReturn(fileSize);

        // Ensure the directory exists
        Path uploadPath = Paths.get(tempDir.toString(), "existing-dir");
        Files.createDirectories(uploadPath);

        // Capture the actual path where the file will be transferred
        ArgumentCaptor<Path> pathCaptor = ArgumentCaptor.forClass(Path.class);

        Mockito.doNothing().when(mockFile).transferTo(pathCaptor.capture());
        when(mediaRepository.findByMediaIdAndActiveStatus(Mockito.anyLong(), Mockito.eq(ActiveStatus.ACTIVE.getValue())))
                .thenReturn(Optional.empty());

        Mockito.doAnswer(invocation -> {
            Media media = invocation.getArgument(0);
            media.setMediaId(1L);
            return media;
        }).when(mediaRepository).save(Mockito.any(Media.class));

        // Act
        RespMedia respMedia = mediaService.upload(mockFile);

        // Assert
        assertNotNull(respMedia);
        assertEquals(fileSize, respMedia.getFileSize());

        // Verify the existing directory
        assertTrue(Files.exists(uploadPath));
        assertTrue(Files.isDirectory(uploadPath));

        // Verify interactions
        verify(mediaRepository, Mockito.times(1)).save(Mockito.any(Media.class));
        verify(mockFile, Mockito.times(1)).transferTo(Mockito.any(Path.class));
    }

    @Test
    void testUploadWithUniqueMediaId() throws IOException {
        // Arrange
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        String fileType = "image/png";
        long fileSize = 1024L;

        when(mockFile.getContentType()).thenReturn(fileType);
        when(mockFile.getSize()).thenReturn(fileSize);

        // Ensure the directory exists
        Path uploadPath = Paths.get(tempDir.toString(), "existing-dir");
        Files.createDirectories(uploadPath);
        Path filesPath = uploadPath.resolve("unique-file.png");

        // Simulate existing file
        Files.createFile(filesPath);

        // Simulate media ID check
        when(mediaRepository.findByMediaIdAndActiveStatus(Mockito.anyLong(), Mockito.eq(ActiveStatus.ACTIVE.getValue())))
                .thenReturn(Optional.of(new Media())) // Simulate a non-unique media ID
                .thenReturn(Optional.empty()); // Then a unique media ID

        Mockito.doAnswer(invocation -> {
            Media media = invocation.getArgument(0);
            media.setMediaId(1L);
            return media;
        }).when(mediaRepository).save(Mockito.any(Media.class));

        Mockito.doNothing().when(mockFile).transferTo(Mockito.any(Path.class));

        // Act
        RespMedia respMedia = mediaService.upload(mockFile);

        // Assert
        assertNotNull(respMedia);
        assertEquals(fileSize, respMedia.getFileSize());

        // Verify file name uniqueness
        verify(mockFile, Mockito.times(1)).transferTo(Mockito.any(Path.class));
    }

    @Test
    void testDownloadSuccess() throws IOException {
        // Arrange
        long mediaId = 1L;
        String filePathStr = "path/to/file.png";
        String fileName = "file.png";
        String fileType = "image/png";
        Path filePath = Paths.get(filePathStr);
        byte[] fileContent = "file content".getBytes();

        // Mock MediaRepository behavior
        Media media = Media.builder()
                .mediaId(mediaId)
                .fileName(fileName)
                .fileType(fileType)
                .filePath(filePathStr)
                .build();
        when(mediaRepository.findByMediaIdAndActiveStatus(mediaId, ActiveStatus.ACTIVE.getValue())).thenReturn(Optional.of(media));

        // Create a file at the given path with content
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, fileContent);

        // Act
        RespDownload respDownload = mediaService.download(mediaId);

        // Assert
        assertNotNull(respDownload);
        assertEquals(fileName, respDownload.getFileName());
        assertEquals(MediaType.valueOf(fileType), respDownload.getMediaType());

        // Clean up
        Files.deleteIfExists(filePath);
    }

    @Test
    void testDownloadFileNotFound() throws IOException {
        // Arrange
        long mediaId = 1L;
        String filePathStr = "path/to/nonexistent-file.png";

        // Mock MediaRepository behavior
        Media media = Media.builder()
                .mediaId(mediaId)
                .fileName("file.png")
                .fileType("image/png")
                .filePath(filePathStr)
                .build();
        when(mediaRepository.findByMediaIdAndActiveStatus(mediaId, ActiveStatus.ACTIVE.getValue())).thenReturn(Optional.of(media));

        // Act & Assert
        FileNotFoundException thrown = assertThrows(FileNotFoundException.class, () -> mediaService.download(mediaId));
        assertEquals("File not found at path: " + Paths.get(filePathStr), thrown.getMessage());
    }

    @Test
    void testDeleteMedia() {
        // Arrange
        long mediaId = 1L;
        when(mediaRepository.findByMediaIdAndActiveStatus(mediaId, ActiveStatus.ACTIVE.getValue())).thenReturn(Optional.of(media));

        // Act
        mediaService.deleteMedia(mediaId);

        // Assert
        verify(media).setActiveStatus(ActiveStatus.INACTIVE.getValue());
        verify(mediaRepository).save(media);
    }

    @Test
    void testDeleteMediaNotFound() {
        // Arrange
        long mediaId = 1L;
        when(mediaRepository.findByMediaIdAndActiveStatus(mediaId, ActiveStatus.ACTIVE.getValue())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MediaNotFoundException.class, () -> mediaService.deleteMedia(mediaId));
    }


}