package com.ms.mail.mailmicroservice.services;

import com.ms.mail.mailmicroservice.dtos.VerificationMailDto;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    public void sendVerificationMail(@NotNull VerificationMailDto verificationMailDto) {
        try {
            var simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("Authentication-Website");
            simpleMailMessage.setTo(verificationMailDto.getEmail());
            simpleMailMessage.setSubject("Hey, verification code is here!");
            simpleMailMessage.setText("Your verification code is -> " + verificationMailDto.getVerificationCode());
            javaMailSender.send(simpleMailMessage);
        } catch (MailException mailException) {
            throw new RuntimeException(mailException.getMessage());
        }
    }

}
