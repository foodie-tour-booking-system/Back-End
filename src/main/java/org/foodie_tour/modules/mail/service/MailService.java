package org.foodie_tour.modules.mail.service;

import org.foodie_tour.modules.mail.dto.request.SendMailRequest;

public interface MailService {
    void sendMail(SendMailRequest request);
}
