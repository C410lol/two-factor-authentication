package com.ms.mail.mailmicroservice.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.mail.mailmicroservice.dtos.VerificationMailDto;
import com.ms.mail.mailmicroservice.services.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailListener {

    private final MailService mailService;

    @KafkaListener(groupId = "mail-microservice", topics = {"mails"})
    public void consumeMailVerificationMessage(String verificationMailDtoJson) throws JsonProcessingException {
        mailService.sendVerificationMail(convertJsonToVerificationMailDto(verificationMailDtoJson));
    }

    private VerificationMailDto convertJsonToVerificationMailDto(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, VerificationMailDto.class);
    }

}
