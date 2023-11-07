package com.instargram101.member.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    public String saveFile(MultipartFile multipartFile) throws IOException {

        byte[] fileBytes = processUploadedFile(multipartFile);
        UUID uuid = UUID.randomUUID();
        String originalFilename = "profile_image/" + uuid + "_" + multipartFile.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileBytes.length);
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, originalFilename, new ByteArrayInputStream(fileBytes), metadata);
        return amazonS3.getUrl(bucket, originalFilename).toString();
    }

    private byte[] processUploadedFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return resizeFile(file, MAX_FILE_SIZE);
        }

        return file.getBytes();
    }

    private byte[] resizeFile(MultipartFile file, long targetSize) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        if (targetSize <= 0) {
            throw new IllegalArgumentException("Invalid target size");
        }

        byte[] fileBytes = file.getBytes();
        if (fileBytes.length <= targetSize) {
            return fileBytes;
        }

        return Arrays.copyOf(fileBytes, (int) targetSize);
    }
}