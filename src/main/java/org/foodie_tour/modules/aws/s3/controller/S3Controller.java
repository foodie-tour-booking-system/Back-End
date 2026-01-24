package org.foodie_tour.modules.aws.s3.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.aws.s3.service.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/aws/s3")
public class S3Controller {
    S3Service s3Service;

    @PostMapping("/upload-custom-prefix")
    public ResponseEntity<String> uploadCustomPrefix(@RequestPart (value = "file") MultipartFile file,
                                                     @RequestPart (value = "prefix") String prefix) throws IOException {
        s3Service.uploadFileWithCustomPrefix(file, prefix);
        return ResponseEntity.status(HttpStatus.OK).body("Tải lên tệp thành công");
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteFile(@RequestBody String fileUrl) {
        s3Service.deleteByPublicUrl(fileUrl);
        return ResponseEntity.status(HttpStatus.OK).body("Đã xoá tệp");
    }
}
