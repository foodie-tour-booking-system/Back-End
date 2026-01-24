package org.foodie_tour.modules.aws.s3.service.Impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.foodie_tour.modules.aws.s3.service.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class S3ServiceImpl implements S3Service {
//    S3Client s3Client;
//
//    @Value("${aws.s3.bucket}")
//    @NonFinal
//    String BUCKET;
//
//    @Value("${aws.s3.region}")
//    @NonFinal
//    String REGION;
//
//    public String uploadFile(MultipartFile file) throws IOException {
//        String fileName = generateFileName(file);
//        PutObjectRequest request = PutObjectRequest.builder()
//                .bucket(BUCKET)
//                .key(fileName)
//                .contentType(file.getContentType())
//                .build();
//
//        s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
//        return getPublicUrl(fileName);
//    }
//
//    // Upload with custom prefix
//    public String uploadFileWithCustomPrefix(MultipartFile file, String prefix) throws IOException {
//        String filename = generateFileName(file);
//        filename = prefix + "/" + filename;
//
//        PutObjectRequest request = PutObjectRequest.builder()
//                .bucket(BUCKET)
//                .key(filename)
//                .contentType(file.getContentType())
//                .build();
//
//        s3Client.putObject(request,
//                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
//
//        return getPublicUrl(filename);
//    }
//
//    public String getPublicUrl(String filename) {
//        return String.format("https://%s.%s.%s.amazonaws.com/%s",
//                BUCKET,
//                s3Client.serviceName(),
//                REGION,
//                filename);
//    }
//
//    public void deleteFile(String filename) throws IOException {
//        DeleteObjectRequest request = DeleteObjectRequest.builder()
//                .bucket(BUCKET)
//                .key(filename)
//                .build();
//
//        s3Client.deleteObject(request);
//    }
//
//    public boolean deleteByPublicUrl(String publicUrl) {
//        try {
//            String baseUrl = String.format("https://%s.s3.%s.amazonaws.com/", BUCKET, REGION);
//
//            if (!publicUrl.startsWith(baseUrl)) {
//                throw new IllegalArgumentException("URL của tài nguyên không tồn tại: " + publicUrl);
//            }
//
//            String objectKey = publicUrl.substring(baseUrl.length());
//
//            deleteFile(objectKey);
//            return true;
//        } catch (IOException e) {
//            return false;
//        }
//    }
//
//    private String generateFileName(MultipartFile file) {
//        String originalFilename = file.getOriginalFilename();
//        String extension = "";
//
//        if (originalFilename != null && originalFilename.contains(".")) {
//            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//        }
//        return UUID.randomUUID() + extension;
//    }
}
