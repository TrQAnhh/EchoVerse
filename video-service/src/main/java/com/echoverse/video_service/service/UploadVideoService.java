package com.echoverse.video_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.echoverse.video_service.dto.response.FileResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jcodec.api.FrameGrab;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadVideoService {

    private final Cloudinary cloudinary;

    public FileResponseDto uploadFile(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        String originalFileName = file.getOriginalFilename();
        String publicValue = generatePublicValue(originalFileName);
        String extension = getFileExtension(originalFileName);
        File fileUpload = convert(file, publicValue, extension);

        String resourceType = determineResourceType(extension);

        cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap(
                "public_id", publicValue,
                "resource_type", resourceType
        ));
        cleanDisk(fileUpload);

        String url = cloudinary.url()
                .resourceType(resourceType)
                .generate(publicValue + "." + extension);

        return FileResponseDto.builder()
                .originalFileName(publicValue)
                .url(url)
                .build();
    }

    public String generatePublicValue(String originalName) {
        String fileName = getFileName(originalName)[0];
        return StringUtils.join(UUID.randomUUID().toString(),"_",fileName);
    }

    public String[] getFileName(String originalName) {
        return originalName.split("\\.");
    }

    private String determineResourceType(String extension) {
        switch (extension) {
            case "mp4":
            case "mov":
            case "avi":
            case "mkv":
                return "video";
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
            case "bmp":
                return "image";
            default:
                return "auto";
        }
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1) return "";
        return filename.substring(dotIndex + 1).toLowerCase();
    }

    private File convert(MultipartFile file, String publicValue, String extension) throws IOException {
        File convFile = new File(publicValue + "." + extension);
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, convFile.toPath());
        }
        return convFile;
    }

    private void cleanDisk(File file) {
        try {
            Path filePath = file.toPath();
            Files.delete(filePath);
        } catch (IOException e) {
            log.error("Error");
        }
    }

    public static long getDuration(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }

        File tempFile = File.createTempFile("temp-video-", suffix);
        multipartFile.transferTo(tempFile);

        try (FileChannelWrapper ch = NIOUtils.readableChannel(tempFile)) {
            FrameGrab grab = FrameGrab.createFrameGrab(ch);
            double durationDouble = grab.getVideoTrack().getMeta().getTotalDuration();
            return Math.round(durationDouble);
        } catch (Exception e) {
            throw new IOException("Fail to get video duration", e);
        } finally {
            tempFile.delete();
        }
    }
}
