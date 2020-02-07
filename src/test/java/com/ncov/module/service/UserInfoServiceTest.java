package com.ncov.module.service;

import com.ncov.module.common.enums.UserRole;
import com.ncov.module.common.exception.DuplicateException;
import com.ncov.module.common.exception.UserNotFoundException;
import com.ncov.module.controller.request.user.PasswordResetRequest;
import com.ncov.module.controller.resp.user.SignInResponse;
import com.ncov.module.entity.HospitalInfoEntity;
import com.ncov.module.entity.SupplierInfoEntity;
import com.ncov.module.entity.UserInfoEntity;
import com.ncov.module.mapper.HospitalInfoMapper;
import com.ncov.module.mapper.SupplierMapper;
import com.ncov.module.mapper.UserInfoMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultJwtParser;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserInfoServiceTest {

    @Mock
    private UserInfoMapper userInfoMapper;
    @Mock
    private HospitalInfoMapper hospitalInfoMapper;
    @Mock
    private SupplierMapper supplierMapper;
    @InjectMocks
    private UserInfoService userInfoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(userInfoService, "baseMapper", userInfoMapper);
        ReflectionTestUtils.setField(userInfoService, "jwtSecret", "123456");
        ReflectionTestUtils.setField(userInfoService, "jwtExpirationInMs", 259200000L);
        when(userInfoMapper.findByUserPhone(anyString())).thenReturn(UserInfoEntity.builder().id(3L).userRoleId(2).userNickName("Nick test").userPasswordSHA256(DigestUtils.sha256Hex("password")).userIdentificationNumber("110102196001010001").userPhone("18810119999").build());
        when(hospitalInfoMapper.selectByHospitalCreatorUserId(anyLong())).thenReturn(HospitalInfoEntity.builder().id(101L).hospitalName("测试医院").build());
        when(supplierMapper.selectByMaterialSupplierCreatorUserId(anyLong())).thenReturn(SupplierInfoEntity.builder().id(102L).materialSupplierName("测试供应商").build());
    }

    @Test
    void should_save_user_when_create_unique_user_given_user() {
        UserInfoEntity savedUser = userInfoService.createUniqueUser(UserInfoEntity.builder().userNickName("nick").userPasswordSHA256("abc").gmtCreated(new Date()).userPhone("12345678901").userRoleId(UserRole.SUPPLIER.getRoleId()).build());

        assertNotNull(savedUser);
        verify(userInfoMapper).selectCountByPhoneOrNickName(eq("12345678901"), eq("nick"));
        verify(userInfoMapper).insert(any(UserInfoEntity.class));
    }

    @Test
    void should_throw_duplicate_exception_when_create_unique_user_given_user_nickname_or_phone_already_exists() {
        when(userInfoMapper.selectCountByPhoneOrNickName(anyString(), anyString())).thenReturn(1);

        assertThrows(DuplicateException.class, () -> userInfoService.createUniqueUser(UserInfoEntity.builder().userNickName("nick").userPasswordSHA256("abc").gmtCreated(new Date()).userPhone("12345678901").userRoleId(UserRole.SUPPLIER.getRoleId()).build()));
    }

    @Test
    void should_return_token_and_expiration_time_when_sign_in_given_phone_and_password() {
        SignInResponse response = userInfoService.signIn("18800001111", "password");

        assertTrue(StringUtils.isNotEmpty(response.getToken()));
        assertTrue(new Date().getTime() < response.getExpiresAt().getTime());
        assertTrue(new DefaultJwtParser().isSigned(response.getToken()));
        Claims jwtClaims = new DefaultJwtParser().setSigningKey("123456").parseClaimsJws(response.getToken()).getBody();
        assertEquals(3L, jwtClaims.get("id", Long.class).longValue());
        assertEquals("Nick test", jwtClaims.get("userNickName", String.class));
        assertEquals(UserRole.SUPPLIER.name(), jwtClaims.get("userRole", String.class));
    }

    @Test
    void should_return_hospital_info_in_token_when_sign_in_given_user_is_a_hospital() {
        when(userInfoMapper.findByUserPhone(anyString())).thenReturn(UserInfoEntity.builder().id(3L).userRoleId(3).userNickName("Nick test").userPasswordSHA256(DigestUtils.sha256Hex("password")).build());

        SignInResponse response = userInfoService.signIn("18800001111", "password");
        Claims jwtClaims = new DefaultJwtParser().setSigningKey("123456").parseClaimsJws(response.getToken()).getBody();
        assertEquals(101L, jwtClaims.get("organisationId", Long.class).longValue());
        assertEquals("测试医院", jwtClaims.get("organisationName", String.class));
    }

    @Test
    void should_return_supplier_info_in_token_when_sign_in_given_user_is_a_supplier() {
        SignInResponse response = userInfoService.signIn("18800001111", "password");
        String token = response.getToken();
        Claims jwtClaims = new DefaultJwtParser().setSigningKey("123456").parseClaimsJws(response.getToken()).getBody();
        assertEquals(102L, jwtClaims.get("organisationId", Long.class).longValue());
        assertEquals("测试供应商", jwtClaims.get("organisationName", String.class));
    }

    @Test
    void should_throw_bad_credential_exception_when_sign_in_given_phone_not_exist() {
        when(userInfoMapper.findByUserPhone(anyString())).thenReturn(null);

        assertThrows(BadCredentialsException.class, () -> userInfoService.signIn("18900001111", "password"));
    }

    @Test
    void should_throw_bad_credential_exception_when_sign_in_given_password_does_not_match_with_record() {
        when(userInfoMapper.findByUserPhone(anyString())).thenReturn(UserInfoEntity.builder().userPasswordSHA256("correctpassword").build());

        assertThrows(BadCredentialsException.class, () -> userInfoService.signIn("18800001111", "wrongpassword"));
    }

    @Test
    void should_have_password_reset_when_reset_password_given_request() {
        userInfoService.resetPassword(PasswordResetRequest.builder().telephone("18810119999").identificationNumber("110102196001010001").password("changedPassword").build());

        ArgumentCaptor<UserInfoEntity> userCaptor = ArgumentCaptor.forClass(UserInfoEntity.class);
        verify(userInfoMapper).updateById(userCaptor.capture());
        UserInfoEntity user = userCaptor.getValue();
        assertEquals(DigestUtils.sha256Hex("changedPassword"), user.getUserPasswordSHA256());
    }

    @Test
    void should_throw_user_not_found_exception_when_reset_password_given_phone_not_exist() {
        when(userInfoMapper.findByUserPhone(anyString())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userInfoService.resetPassword(PasswordResetRequest.builder().telephone("18810119999").identificationNumber("110102196001010001").password("changedPassword").build()));
    }

    @Test
    void should_throw_user_not_found_exception_when_reset_password_given_identification_number_not_match() {
        when(userInfoMapper.findByUserPhone(anyString())).thenReturn(UserInfoEntity.builder().id(3L).userRoleId(2).userNickName("Nick test").userPasswordSHA256(DigestUtils.sha256Hex("password")).userIdentificationNumber("110102196001010002").userPhone("18810119999").build());

        assertThrows(UserNotFoundException.class, () -> userInfoService.resetPassword(PasswordResetRequest.builder().telephone("18810119999").identificationNumber("110102196001010001").password("changedPassword").build()));
    }
}