package com.midhub.notification_microservice.services.implementations;

import com.midhub.notification_microservice.exceptions.EmailSendingException;
import com.midhub.notification_microservice.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = Logger.getLogger(EmailServiceImpl.class.getName());


    @Value("${email.admins}")
    private List<String> adminEmails;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendOrderEmail(String email, byte[] pdfContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Confirmation of your order");
        helper.setText("Attached you will find the details of your order. Thank you for choosing us! :D", true);
        helper.addAttachment("order.pdf", () -> new ByteArrayInputStream(pdfContent));

        mailSender.send(message);
        logger.info("Order email successfully sent to: " + email);
    }

    @Override
    public void sendEmailToAdmins(String subject, String message) {
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setFrom(senderEmail);
            helper.setTo(adminEmails.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(message);

            mailSender.send(mail);
            logger.info("Admin notification email sent successfully.");
        } catch (MessagingException e) {
            logger.log(Level.SEVERE, "ERROR sending admin notification email", e);
            throw new EmailSendingException("Failed to send admin notification email", e);
        }
    }
}
