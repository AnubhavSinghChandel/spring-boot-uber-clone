package com.project.uber.uberApp.services.implementation;

import com.project.uber.uberApp.services.EmailSendingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSendingServiceImpl implements EmailSendingService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String body) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            log.info("Email sent successfully!");

        }catch (Exception e){
            log.info("Mail could not be sent! \n{}", e.getMessage());
        }
    }

    @Override
    public void sendBulkEmail(String[] to, String subject, String body) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();

            message.setBcc(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            log.info("Emails sent successfully!");

        }catch (Exception e){
            log.info("Mails could not be sent! \n{}", e.getMessage());
        }
    }
}
