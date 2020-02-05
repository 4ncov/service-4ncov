package com.ncov.module.service;

import com.ncov.module.common.enums.UserRole;
import com.ncov.module.common.exception.DuplicateException;
import com.ncov.module.entity.UserInfoEntity;
import com.ncov.module.mapper.UserInfoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserInfoServiceTest {

    @Mock
    private UserInfoMapper userInfoMapper;
    @InjectMocks
    private UserInfoService userInfoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(userInfoService, "baseMapper", userInfoMapper);
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
}