package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.model.dto.ContactFormDto;
import bg.softuni.damapp.model.entity.ContactMessage;
import bg.softuni.damapp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {


    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String damContactEmail;

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
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
    public void sendReply(String recipientEmail, String replyMessage) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject("Отговор");
        mailMessage.setText(replyMessage);
        emailSender.send(mailMessage);
    }
}
