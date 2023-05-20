package com.ms.auth.authmicroservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class VerificationMailDto {

    private String email;
    private Integer verificationCode;

}
