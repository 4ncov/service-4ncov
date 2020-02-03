package com.ncov.module.controller.resp.hospital;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalResponse {

    private Long id;
    private String name;
    private String uniformSocialCreditCode;
    private String address;
    private String contactorName;
    private String contactorTelephone;
    private Date gmtCreated;
    private Date gmtModified;
}
