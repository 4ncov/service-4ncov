package com.ncov.module.controller.request.supplier;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierSignUpRequest {
    //名称
    private String name;
    //统一社会信用代码
    private String uniformSocialCreditCode;
    //发货地址
    private String address;
    //资质图片地址
    private List<String> imageUrls;
    //联系人
    private String contactorName;
    //联系电话
    private String contactorTelephone;
    //密码
    private String password;
    //是否有物流
    private Boolean haveLogistics;
}
