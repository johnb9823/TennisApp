package com.example.tennisapp.domain.court.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    public String uploadFile(MultipartFile file, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 실패", e);
        }
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    public void deleteFile(String fileUrl) {
        // fileUrl 예: https://{bucket}.s3.amazonaws.com/courts/uuid_filename.jpg
        // bucket 이름 이후 경로만 추출
        String bucketUrl = "https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/"; // 리전 포함
        String key = fileUrl.substring(bucketUrl.length());
        amazonS3.deleteObject(bucketName, key);
    }


}
