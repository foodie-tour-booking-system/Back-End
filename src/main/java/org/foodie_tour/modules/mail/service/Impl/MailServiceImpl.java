package org.foodie_tour.modules.mail.service.Impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.foodie_tour.modules.mail.dto.request.SendMailRequest;
import org.foodie_tour.modules.mail.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailServiceImpl implements MailService {
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    @NonFinal
    String SENDER;

    @Async
    public void sendMail(SendMailRequest request) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(SENDER);
        simpleMailMessage.setTo(request.getTo());
        simpleMailMessage.setSubject(request.getSubject());
        simpleMailMessage.setText(request.getContent());

        javaMailSender.send(simpleMailMessage);
    }

}
