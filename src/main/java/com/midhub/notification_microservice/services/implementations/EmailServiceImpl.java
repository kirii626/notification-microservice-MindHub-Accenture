package com.midhub.notification_microservice.services.implementations;

import com.midhub.notification_microservice.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendOrderEmail(String email, byte[] pdfContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Confirmation of your order");
        helper.setText("Attached you will find the details of your order. Thank you for choosing us.", true);
        helper.addAttachment("order.pdf", () -> new ByteArrayInputStream(pdfContent));

        mailSender.send(message);
        System.out.println("Email sent to: " + email);

    }
}
