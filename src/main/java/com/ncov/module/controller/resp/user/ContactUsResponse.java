package com.ncov.module.controller.resp.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 联系我们响应对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactUsResponse {

    private Long id;
    private String phone;
    private String content;
    private Date gmtCreated;
    private Date gmtModified;
    private Long createUserId;
}
