package org.foodie_tour.modules.mail.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.modules.mail.dto.request.SendMailRequest;
import org.foodie_tour.modules.mail.service.MailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailController {
    MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestBody SendMailRequest request) {
        mailService.sendMail(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Đã gửi");
    }
}
