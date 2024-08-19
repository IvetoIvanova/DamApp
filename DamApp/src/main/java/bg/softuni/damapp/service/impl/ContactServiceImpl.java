package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.model.dto.ContactFormDto;
import bg.softuni.damapp.model.entity.ContactMessage;
import bg.softuni.damapp.repository.ContactRepository;
import bg.softuni.damapp.service.ContactService;
import bg.softuni.damapp.service.ConversationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public void saveMessage(ContactFormDto contactFormDto) {
        System.out.println("Saving message: " + contactFormDto.getName() + ", " + contactFormDto.getEmail() + ", " + contactFormDto.getMessage());

        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setName(contactFormDto.getName());
        contactMessage.setEmail(contactFormDto.getEmail());
        contactMessage.setMessage(contactFormDto.getMessage());
        contactMessage.setTimestamp(LocalDateTime.now());

        contactRepository.save(contactMessage);

        System.out.println("Message saved successfully");
    }

    @Override
    public List<ContactMessage> findAllContactMessages() {
        return contactRepository.findAll();
    }
}
