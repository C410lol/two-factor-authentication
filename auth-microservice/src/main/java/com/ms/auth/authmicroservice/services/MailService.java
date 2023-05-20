package com.ms.auth.authmicroservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.auth.authmicroservice.dtos.VerificationMailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMailVerificationMessage(VerificationMailDto verificationMailDto) throws JsonProcessingException {
        kafkaTemplate.send("mails", convertMailDtoToString(verificationMailDto));
    }

    private String convertMailDtoToString(VerificationMailDto verificationMailDto) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(verificationMailDto);
    }

}
