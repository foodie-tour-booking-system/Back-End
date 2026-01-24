package org.foodie_tour.modules.aws.s3.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Service {
    String uploadFile(MultipartFile file) throws IOException;
    String uploadFileWithCustomPrefix(MultipartFile file, String prefix) throws IOException;
    String getPublicUrl(String filename);
    void deleteFile(String filename) throws IOException;
    boolean deleteByPublicUrl(String publicUrl);

}
