package com.ncov.module.service;

import com.ncov.module.common.enums.UserRole;
import com.ncov.module.controller.request.supplier.SupplierSignUpRequest;
import com.ncov.module.controller.resp.supplier.SupplierResponse;
import com.ncov.module.entity.SupplierInfoEntity;
import com.ncov.module.entity.UserInfoEntity;
import com.ncov.module.mapper.SupplierMapper;
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


class SupplierServiceTest {

    @Mock
    private SupplierMapper supplierMapper;
    @Mock
    private UserInfoService userInfoService;
    @InjectMocks
    private SupplierService supplierService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(supplierService, "baseMapper", supplierMapper);
        when(userInfoService.createUniqueUser(any(UserInfoEntity.class))).thenReturn(UserInfoEntity.builder().id(31L).build());
        when(supplierMapper.insert(any(SupplierInfoEntity.class))).then((mock) -> {
            ((SupplierInfoEntity) (mock.getArgument(0))).setId(703L);
            return 1;
        });
    }

    @Test
    void should_return_supplier_response_when_sign_up_given_supplier_sign_up_request() {
        SupplierResponse response = supplierService.signUp(SupplierSignUpRequest.builder()
                .name("Liangshan Haohan Supplier Pty Ltd")
                .identificationNumber("110301192001010001")
                .imageUrls(Collections.singletonList("https://oss.com/images/1.png"))
                .contactorName("Mock")
                .contactorTelephone("18900010001")
                .password("87654321")
                .haveLogistics(true)
                .build());

        assertEquals(703L, response.getId().longValue());
        assertEquals("Liangshan Haohan Supplier Pty Ltd", response.getName());
        assertEquals("110301192001010001", response.getIdentificationNumber());
        assertEquals("Mock", response.getContactorName());
        assertEquals("18900010001", response.getContactorTelephone());
        assertTrue(response.getHaveLogistics());
        assertNotNull(response.getGmtCreated());
    }

    @Test
    void should_create_user_when_sign_up() {
        supplierService.signUp(SupplierSignUpRequest.builder()
                .name("Liangshan Haohan Supplier Pty Ltd")
                .identificationNumber("110301192001010001")
                .imageUrls(Collections.singletonList("https://oss.com/images/1.png"))
                .contactorName("Mock")
                .contactorTelephone("18900010001")
                .password("87654321")
                .haveLogistics(true)
                .build());

        ArgumentCaptor<UserInfoEntity> userCaptor = ArgumentCaptor.forClass(UserInfoEntity.class);
        verify(userInfoService).createUniqueUser(userCaptor.capture());
        UserInfoEntity user = userCaptor.getValue();
        assertEquals("Liangshan Haohan Supplier Pty Ltd", user.getUserNickName());
        assertEquals(DigestUtils.sha256Hex("87654321"), user.getUserPasswordSHA256());
        assertEquals("18900010001", user.getUserPhone());
        assertEquals(UserRole.SUPPLIER.getRoleId(), user.getUserRoleId());
        assertNotNull(user.getGmtCreated());
    }

    @Test
    void should_save_supplier_when_sign_up() {
        supplierService.signUp(SupplierSignUpRequest.builder()
                .name("Liangshan Haohan Supplier Pty Ltd")
                .identificationNumber("110301192001010001")
                .imageUrls(Arrays.asList("https://oss.com/images/1.png", "https://oss.com/images/2.png"))
                .contactorName("Mock")
                .contactorTelephone("18900010001")
                .password("87654321")
                .haveLogistics(true)
                .build());

        ArgumentCaptor<SupplierInfoEntity> supplierCaptor = ArgumentCaptor.forClass(SupplierInfoEntity.class);
        verify(supplierMapper).insert(supplierCaptor.capture());
        SupplierInfoEntity supplier = supplierCaptor.getValue();
        assertEquals("Liangshan Haohan Supplier Pty Ltd", supplier.getMaterialSupplierName());
        assertEquals("Mock", supplier.getMaterialSupplierContactorName());
        assertEquals("110301192001010001", supplier.getMaterialSupplierIdentificationNumber());
        assertEquals("https://oss.com/images/1.png,https://oss.com/images/2.png", supplier.getMaterialSupplierVerifyImageUrls());
        assertEquals(31L, supplier.getMaterialSupplierCreatorUserId().longValue());
        assertEquals("18900010001", supplier.getMaterialSupplierContactorPhone());
        assertTrue(supplier.getHaveLogistics());
        assertNotNull(supplier.getGmtCreated());
    }
}