package bg.softuni.damapp.service;

import bg.softuni.damapp.model.dto.ContactFormDto;
import bg.softuni.damapp.model.dto.ContactMessageDto;
import bg.softuni.damapp.model.entity.ContactMessage;

import java.util.List;
import java.util.UUID;

public interface ContactService {
    void saveMessage(ContactFormDto contactFormDto);

    List<ContactMessage> findAllContactMessages();

    ContactMessageDto findMessageById(UUID messageId);
}
