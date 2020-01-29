package com.ncov.module.controller.request.requester;

import com.ncov.module.controller.dto.RequesterDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequesterSignUpRequest {

    private RequesterDto requester;
    private String organisationCode;
    private String identificationImageUrl;
}
