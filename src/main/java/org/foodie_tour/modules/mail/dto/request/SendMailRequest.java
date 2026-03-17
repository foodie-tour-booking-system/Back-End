package org.foodie_tour.modules.mail.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Getter
public class SendMailRequest {
    String[] to;
    String subject;
    String content;
}
