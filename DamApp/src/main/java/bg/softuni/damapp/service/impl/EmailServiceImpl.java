package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.model.dto.ContactFormDto;
import bg.softuni.damapp.model.dto.ContactMessageDto;
import bg.softuni.damapp.model.entity.ContactMessage;
import bg.softuni.damapp.model.entity.ReplyContactMessage;
import bg.softuni.damapp.repository.ReplyRepository;
import bg.softuni.damapp.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final ReplyRepository replyRepository;

    @Value("${spring.mail.username}")
    private String damContactEmail;

    public EmailServiceImpl(JavaMailSender emailSender, ReplyRepository replyRepository) {
        this.emailSender = emailSender;
        this.replyRepository = replyRepository;
    }

    @Override
    public void sendEmailToAdmin(ContactFormDto contactFormDto) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(damContactEmail);
        mailMessage.setSubject("Ново съобщение от " + contactFormDto.getName());
        mailMessage.setText("Име: " + contactFormDto.getName() + "\nИмейл: " + contactFormDto.getEmail() + "\nСъобщение:\n" + contactFormDto.getMessage());
        emailSender.send(mailMessage);
    }

    @Override
    public void sendReply(String recipientEmail, String replyMessage, ContactMessageDto contactMessageDto) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject("Отговор");
        mailMessage.setText(replyMessage);
        emailSender.send(mailMessage);

        ContactMessage contactMessage = convertToEntity(contactMessageDto);

        ReplyContactMessage reply = new ReplyContactMessage();
        reply.setReplyMessage(replyMessage);
        reply.setTimestamp(LocalDateTime.now());
        reply.setContactMessage(contactMessage);
        replyRepository.save(reply);
    }

    private static ContactMessage convertToEntity(ContactMessageDto dto) {
        ContactMessage entity = new ContactMessage();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setMessage(dto.getMessage());
        entity.setTimestamp(dto.getTimestamp());

        return entity;
    }
}
