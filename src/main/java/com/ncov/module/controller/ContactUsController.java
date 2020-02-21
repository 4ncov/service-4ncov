package com.ncov.module.controller;

import com.ncov.module.common.SwaggerConstants;
import com.ncov.module.controller.request.user.ContactUsCreateRequest;
import com.ncov.module.controller.resp.Page;
import com.ncov.module.controller.resp.PageResponse;
import com.ncov.module.controller.resp.RestResponse;
import com.ncov.module.controller.resp.user.ContactUsResponse;
import com.ncov.module.entity.ContactUsEntity;
import com.ncov.module.security.UserContext;
import com.ncov.module.service.ContactUsService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@AllArgsConstructor
@RequestMapping("/api/contact-messages")
public class ContactUsController {

    private final ContactUsService service;

    private final UserContext userContext;

    @ApiOperation(
            value = "create contact us.",
            tags = SwaggerConstants.TAG_CONTACT_MESSAGES
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestResponse createContactUs(@RequestBody @Valid ContactUsCreateRequest contactUsCreateRequest) {
        Date now = new Date();
        service.insert(ContactUsEntity.builder()
                .userPhone(contactUsCreateRequest.getTelephone())
                .content(contactUsCreateRequest.getContent())
                .createUserId(userContext.getUserId())
                .gmtCreated(now)
                .gmtModified(now)
                .build());
        return RestResponse.getResp("创建成功.");
    }

    @ApiOperation(
            value = "list contact us.",
            tags = SwaggerConstants.TAG_CONTACT_MESSAGES
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_SYSADMIN')")
    public PageResponse<ContactUsResponse> listContactUs(@RequestParam Integer page, @RequestParam Integer size) {
        Page<ContactUsResponse> responsePage = service.selectPage(ContactUsEntity.class
                , page
                , size
                , null
                , entity -> new ContactUsResponse(entity.getId()
                        , entity.getUserPhone()
                        , entity.getContent()
                        , entity.getGmtCreated()
                        , entity.getGmtModified()
                        , entity.getCreateUserId()));
        return PageResponse.getResp("请求成功.", responsePage);
    }

}
