package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.model.dto.ContactFormDto;
import bg.softuni.damapp.model.dto.ContactMessageDto;
import bg.softuni.damapp.model.entity.ContactMessage;
import bg.softuni.damapp.repository.ContactRepository;
import bg.softuni.damapp.service.ContactService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public void saveMessage(ContactFormDto contactFormDto) {
        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setName(contactFormDto.getName());
        contactMessage.setEmail(contactFormDto.getEmail());
        contactMessage.setMessage(contactFormDto.getMessage());
        contactMessage.setTimestamp(LocalDateTime.now());

        contactRepository.save(contactMessage);
    }

    @Override
    public List<ContactMessage> findAllContactMessages() {
        return contactRepository.findAll();
    }

    @Override
    public ContactMessageDto findMessageById(UUID messageId) {
        ContactMessage contactMessage = contactRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("Message not found"));
        return convertToDto(contactMessage);
    }

    private static ContactMessageDto convertToDto(ContactMessage contactMessage) {
        ContactMessageDto dto = new ContactMessageDto();
        dto.setId(contactMessage.getId());
        dto.setName(contactMessage.getName());
        dto.setEmail(contactMessage.getEmail());
        dto.setMessage(contactMessage.getMessage());
        dto.setTimestamp(contactMessage.getTimestamp());
        return dto;
    }
}
