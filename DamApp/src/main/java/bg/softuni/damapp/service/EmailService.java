package bg.softuni.damapp.service;

import bg.softuni.damapp.model.dto.ContactFormDto;
import bg.softuni.damapp.model.dto.ContactMessageDto;

public interface EmailService {

    void sendEmailToAdmin(ContactFormDto contactFormDto);

    void sendReply(String recipientEmail, String replyMessage, ContactMessageDto contactMessage);
}
