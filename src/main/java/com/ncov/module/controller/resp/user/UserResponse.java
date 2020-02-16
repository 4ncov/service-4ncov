package com.ncov.module.controller.resp.user;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private String id;
    private String nickName;
    private Date gmtCreated;
    private Date gmtModified;
    private String phone;
    private String role;
    private String status;
}
