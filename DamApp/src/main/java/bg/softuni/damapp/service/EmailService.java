package bg.softuni.damapp.service;

import bg.softuni.damapp.model.dto.ContactFormDto;

public interface EmailService {

    void sendEmailToAdmin(ContactFormDto contactFormDto);
    void sendReply(String recipientEmail, String replyMessage);
}
