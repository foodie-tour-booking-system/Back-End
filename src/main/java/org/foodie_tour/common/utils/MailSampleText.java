package org.foodie_tour.common.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailSampleText {
    public static String CREATE_EMPLOYEE_TITLE = "[Felixiter Travel] - Employee account created";
    public static String CREATE_EMPLOYEE_CONTENT = """
    Hi,

    Your employee account has been created successfully. Please use the details below to log in to the system:

    Email: %s

    Password: %s

    — Felixiter Travel Team
    """;

    public static String CREATE_BOOKING_TITLE = "[Felixiter Travel] - Booking confirmed";
    public static String CREATE_BOOKING_CONTENT = """
    Dear Customer,
            
    Thank you for choosing Felixiter Travel. We sincerely appreciate your trust in our services.

    We confirm that your tour booking request has been processed successfully. Here are the details:

    Tour booking code: %s

    Customer name: %s

    Tour name: %s

    Departure time: %s

    Number of guests: %s

    Total cost: %s

    Please review the information above. If you have any questions or need further assistance, contact us at:

    Email: felixiter.travel@gmail.com

    Once again, thank you for choosing Felixiter Travel. We look forward to accompanying you on your upcoming trip!

    Sincerely,
    — Felixiter Travel Team
    """;

}
