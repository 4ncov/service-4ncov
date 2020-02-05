package com.ncov.module.service;

import com.ncov.module.common.enums.UserRole;
import com.ncov.module.controller.request.hospital.HospitalSignUpRequest;
import com.ncov.module.controller.resp.hospital.HospitalResponse;
import com.ncov.module.entity.HospitalInfoEntity;
import com.ncov.module.entity.UserInfoEntity;
import com.ncov.module.mapper.HospitalInfoMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HospitalServiceTest {

    @Mock
    private UserInfoService userInfoService;
    @Mock
    private HospitalInfoMapper hospitalInfoMapper;
    @InjectMocks
    private HospitalService hospitalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(hospitalService, "baseMapper", hospitalInfoMapper);
        when(userInfoService.createUniqueUser(any(UserInfoEntity.class))).thenReturn(UserInfoEntity.builder().id(32L).userIdentificationNumber("110102198509250091").build());
        when(hospitalInfoMapper.insert(any(HospitalInfoEntity.class))).then((e) -> {
            ((HospitalInfoEntity) e.getArgument(0)).setId(98L);
            return 1;
        });
    }

    @Test
    void should_return_hospital_response_when_sign_up_given_sign_up_request() {
        HospitalResponse response = hospitalService.signUp(HospitalSignUpRequest.builder()
                .name("Wuhan Test Hospital")
                .uniformSocialCreditCode("123456789012345678")
                .imageUrls(Collections.singletonList("https://oss.com/images/test.png"))
                .contactorName("Test")
                .contactorTelephone("18888888888")
                .password("12345678")
                .identificationNumber("110102198509250091")
                .build());

        assertEquals(98L, response.getId().longValue());
        assertEquals("Wuhan Test Hospital", response.getName());
        assertEquals("123456789012345678", response.getUniformSocialCreditCode());
        assertEquals("Test", response.getContactorName());
        assertEquals("18888888888", response.getContactorTelephone());
        assertEquals("110102198509250091", response.getIdentificationNumber());
        assertNotNull(response.getGmtCreated());
    }

    @Test
    void should_create_new_user_with_password_hashed_when_sign_up() {
        hospitalService.signUp(HospitalSignUpRequest.builder()
                .name("Wuhan Test Hospital")
                .uniformSocialCreditCode("123456789012345678")
                .imageUrls(Collections.singletonList("https://oss.com/images/test.png"))
                .contactorName("Test")
                .contactorTelephone("18888888888")
                .password("12345678")
                .identificationNumber("110102198509250091")
                .build());

        ArgumentCaptor<UserInfoEntity> userCaptor = ArgumentCaptor.forClass(UserInfoEntity.class);
        verify(userInfoService).createUniqueUser(userCaptor.capture());
        UserInfoEntity user = userCaptor.getValue();
        assertEquals("Wuhan Test Hospital", user.getUserNickName());
        assertEquals(DigestUtils.sha256Hex("12345678"), user.getUserPasswordSHA256());
        assertNotNull(user.getGmtCreated());
        assertEquals("18888888888", user.getUserPhone());
        assertEquals("110102198509250091", user.getUserIdentificationNumber());
        assertEquals(UserRole.HOSPITAL.getRoleId(), user.getUserRoleId());
    }

    @Test
    void should_save_hospital_when_sign_up() {
        hospitalService.signUp(HospitalSignUpRequest.builder()
                .name("Wuhan Test Hospital")
                .uniformSocialCreditCode("123456789012345678")
                .imageUrls(Arrays.asList("https://oss.com/images/test.png", "https://oss.com/images/test-2.png"))
                .contactorName("Test")
                .contactorTelephone("18888888888")
                .password("12345678")
                .identificationNumber("110102198509250091")
                .build());

        ArgumentCaptor<HospitalInfoEntity> hospitalCaptor = ArgumentCaptor.forClass(HospitalInfoEntity.class);
        verify(hospitalInfoMapper).insert(hospitalCaptor.capture());
        HospitalInfoEntity hospital = hospitalCaptor.getValue();
        assertEquals("Wuhan Test Hospital", hospital.getHospitalName());
        assertEquals("123456789012345678", hospital.getHospitalUniformSocialCreditCode());
        assertEquals(32L, hospital.getHospitalCreatorUserId().longValue());
        assertEquals("https://oss.com/images/test.png,https://oss.com/images/test-2.png", hospital.getHospitalVerifyImageUrls());
        assertEquals("Test", hospital.getHospitalContactorName());
        assertEquals("18888888888", hospital.getHospitalContactorTelephone());
        assertNotNull(hospital.getGmtCreated());
    }
}