package com.ncov.module.controller.request.hospital;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalSignUpRequest {

    private String name;
    private String uniformSocialCreditCode;
    private String address;
    private String verifyInfoUrl1;
    private String verifyInfoUrl2;
    private String verifyInfoUrl3;
    private String contactorName;
    private String contactorTelephone;
    private String password;
}
