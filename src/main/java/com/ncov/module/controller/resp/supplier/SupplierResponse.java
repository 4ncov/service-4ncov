package com.ncov.module.controller.resp.supplier;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierResponse {

    private Long id;
    private String name;
    private String uniformSocialCreditCode;
    private String address;
    private String contactorName;
    private String contactorTelephone;
    private Date gmtCreated;
    private Date gmtModified;
    private Boolean haveLogistics;
}
