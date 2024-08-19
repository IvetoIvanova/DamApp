package bg.softuni.damapp.service;

import bg.softuni.damapp.model.dto.ContactFormDto;
import bg.softuni.damapp.model.entity.ContactMessage;

import java.util.List;

public interface ContactService {
    void saveMessage(ContactFormDto contactFormDto);
    List<ContactMessage> findAllContactMessages();
}
