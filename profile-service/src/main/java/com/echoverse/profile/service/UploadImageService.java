package com.echoverse.profile.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.echoverse.profile.dto.response.ImageFileResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
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
public class UploadImageService {

    private final Cloudinary cloudinary;

    public ImageFileResponseDto uploadImage(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        String publicValue = generatePublicValue(file.getOriginalFilename());
        String extension   = getFileName(file.getOriginalFilename())[1];
        File fileUpload = convert(file);

        cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap("public_id",publicValue));
        cleanDisk(fileUpload);

        var url = cloudinary.url().generate(StringUtils.join(publicValue, ".", extension));

        return ImageFileResponseDto.builder()
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

    private File convert(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        File convFile = new File(StringUtils.join(
                generatePublicValue(file.getOriginalFilename()),
                getFileName(file.getOriginalFilename())[1]
        ));
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

}
