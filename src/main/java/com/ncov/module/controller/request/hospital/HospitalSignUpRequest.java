package com.ncov.module.controller.request.hospital;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalSignUpRequest {

    private String name;
    private String uniformSocialCreditCode;
    private String address;
    private List<String> imageUrls;
    private String contactorName;
    private String contactorTelephone;
    private String password;
}
