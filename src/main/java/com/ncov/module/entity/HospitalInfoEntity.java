package com.ncov.module.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ncov.module.common.Constants;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("hospital_info")
public class HospitalInfoEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String hospitalName;
    private String hospitalUniformSocialCreditCode;
    private Long hospitalCreatorUserId;
    private String hospitalVerifyImageUrls;
    private String hospitalContactorName;
    private String hospitalContactorTelephone;
    private Date gmtCreated;
    private Date gmtModified;

    public List<String> getAllImageUrls() {
        if (StringUtils.isEmpty(getHospitalVerifyImageUrls())) {
            return Collections.emptyList();
        }
        return Arrays.stream(getHospitalVerifyImageUrls().split(Constants.DELIMITER_COMMA))
                .collect(Collectors.toList());
    }

    public void setHospitalVerifyImageUrls(String hospitalVerifyImageUrls) {
        this.hospitalVerifyImageUrls = hospitalVerifyImageUrls;
    }

    public void setHospitalVerifyImageUrls(List<String> imageUrls) {
        if (CollectionUtils.isEmpty(imageUrls)) {
            setHospitalVerifyImageUrls("");
        }
        setHospitalVerifyImageUrls(String.join(Constants.DELIMITER_COMMA, imageUrls));
    }
}
