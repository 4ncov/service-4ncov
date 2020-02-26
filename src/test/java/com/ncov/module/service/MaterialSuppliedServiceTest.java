package com.ncov.module.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ncov.module.common.enums.UserStatus;
import com.ncov.module.common.exception.MaterialNotFoundException;
import com.ncov.module.controller.dto.AddressDto;
import com.ncov.module.controller.dto.MaterialDto;
import com.ncov.module.controller.request.material.MaterialRequest;
import com.ncov.module.controller.resp.material.MaterialResponse;
import com.ncov.module.entity.MaterialSuppliedEntity;
import com.ncov.module.entity.SupplierInfoEntity;
import com.ncov.module.entity.UserInfoEntity;
import com.ncov.module.mapper.MaterialSuppliedMapper;
import com.ncov.module.security.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.access.AccessDeniedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MaterialSuppliedServiceTest {

    @Mock
    private UserInfoService userInfoService;
    @Mock
    private MaterialSuppliedMapper materialSuppliedMapper;
    @Mock
    private UserContext userContext;
    @Mock
    private SupplierService supplierService;
    @Spy
    @InjectMocks
    private MaterialSuppliedService materialSuppliedService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        doAnswer((call) -> {
            List<MaterialSuppliedEntity> entities = call.getArgument(0);
            for (int i = 0; i < entities.size(); ++i) {
                entities.get(i).setId(i + 10L);
            }
            return true;
        }).when(materialSuppliedService).saveBatch(anyList());
        when(userInfoService.getUser(anyLong())).thenReturn(UserInfoEntity.builder().status(UserStatus.PENDING.name()).build());
        when(userContext.getUserId()).thenReturn(1L);
        when(userContext.isSysAdmin()).thenReturn(false);
        when(supplierService.list(any(LambdaQueryWrapper.class))).thenReturn(Collections.singletonList(SupplierInfoEntity.builder().id(1L).logo("logo.jpg").build()));
        when(supplierService.getById(anyLong())).thenReturn(SupplierInfoEntity.builder().id(1L).logo("logo.jpg").build());
    }

    @Test
    void should_return_material_when_create_given_material_and_organisation_id_and_user_id() {
        List<MaterialResponse> responses = materialSuppliedService.create(
                MaterialRequest.builder()
                        .materials(Collections.singletonList(MaterialDto.builder().name("Materialname").quantity(200000.0).standard("ISO9001").category("Mask").imageUrls(Arrays.asList("https://oss.com/images/1.png", "https://oss.com/images/2.png")).build()))
                        .organisationName("Supplier Organisation")
                        .address(AddressDto.builder()
                                .country("中国")
                                .province("湖北省")
                                .city("武汉市")
                                .district("东西湖区")
                                .streetAddress("银潭路1号")
                                .build())
                        .contactorName("Test M")
                        .contactorPhone("18800001111")
                        .comment("Test comment")
                        .build(),
                1L, 2L);

        assertEquals(1, responses.size());
        MaterialResponse response = responses.get(0);
        assertEquals("10", response.getId());
        assertEquals(MaterialDto.builder().name("Materialname").quantity(200000.0).standard("ISO9001").category("Mask").imageUrls(Arrays.asList("https://oss.com/images/1.png", "https://oss.com/images/2.png")).build(), response.getMaterial());
        assertEquals("Supplier Organisation", response.getOrganisationName());
        assertEquals("logo.jpg", response.getOrganisationLogo());
        assertEquals(AddressDto.builder().country("中国").province("湖北省").city("武汉市").district("东西湖区").streetAddress("银潭路1号").build(), response.getAddress());
        assertEquals("Test M", response.getContactorName());
        assertEquals("18800001111", response.getContactorPhone());
        assertEquals("Test comment", response.getComment());
        assertEquals("PENDING", response.getStatus());
        assertNotNull(response.getGmtCreated());
    }

    @Test
    void should_save_material_with_status_pending_when_create_given_material_and_organisation_id_and_user_id() {
        materialSuppliedService.create(
                MaterialRequest.builder()
                        .materials(Collections.singletonList(MaterialDto.builder().name("Materialname").quantity(200000.0).standard("ISO9001").category("Mask").imageUrls(Arrays.asList("https://oss.com/images/1.png", "https://oss.com/images/2.png")).build()))
                        .organisationName("Supplier Organisation")
                        .address(AddressDto.builder()
                                .country("中国")
                                .province("湖北省")
                                .city("武汉市")
                                .district("东西湖区")
                                .streetAddress("银潭路1号")
                                .build())
                        .contactorName("Test M")
                        .contactorPhone("18800001111")
                        .comment("Test comment")
                        .build(),
                1L, 2L);

        ArgumentCaptor<List> materialsCaptor = ArgumentCaptor.forClass(List.class);
        verify(materialSuppliedService).saveBatch(materialsCaptor.capture());
        List<MaterialSuppliedEntity> materials = materialsCaptor.getValue();
        assertEquals(1, materials.size());
        MaterialSuppliedEntity material = materials.get(0);
        assertEquals("Materialname", material.getMaterialSuppliedName());
        assertEquals("Mask", material.getMaterialSuppliedCategory());
        assertEquals("ISO9001", material.getMaterialSuppliedStandard());
        assertEquals("Test M", material.getMaterialSuppliedContactorName());
        assertEquals("18800001111", material.getMaterialSuppliedContactorPhone());
        assertEquals("中国", material.getCountry());
        assertEquals("湖北省", material.getProvince());
        assertEquals("武汉市", material.getCity());
        assertEquals("东西湖区", material.getDistrict());
        assertEquals("银潭路1号", material.getStreetAddress());
        assertEquals(200000.0, material.getMaterialSuppliedQuantity().doubleValue());
        assertEquals("Supplier Organisation", material.getMaterialSuppliedOrganizationName());
        assertEquals(1L, material.getMaterialSupplierOrganizationId().longValue());
        assertEquals(2L, material.getMaterialSuppliedUserId().longValue());
        assertEquals("Test comment", material.getMaterialSuppliedComment());
        assertEquals("PENDING", material.getMaterialSuppliedStatus());
        assertNotNull(material.getGmtCreated());
    }

    @Test
    void should_return_multiple_materials_when_create_given_request_containing_multiple_materials() {
        List<MaterialResponse> responses = materialSuppliedService.create(
                MaterialRequest.builder()
                        .materials(Arrays.asList(
                                MaterialDto.builder().name("Materialname").quantity(200000.0).standard("ISO9001").category("Mask").imageUrls(Arrays.asList("https://oss.com/images/1.png", "https://oss.com/images/2.png")).build(),
                                MaterialDto.builder().name("Coat").quantity(3000.0).standard("ISO9002").category("Coat").imageUrls(Arrays.asList("https://oss.com/images/1.png", "https://oss.com/images/2.png")).build()
                        ))
                        .organisationName("Supplier Organisation")
                        .address(AddressDto.builder()
                                .country("中国")
                                .province("湖北省")
                                .city("武汉市")
                                .district("东西湖区")
                                .streetAddress("银潭路1号")
                                .build())
                        .contactorName("Test M")
                        .contactorPhone("18800001111")
                        .comment("Test comment")
                        .build(),
                1L, 2L);

        assertEquals(2, responses.size());
        MaterialResponse response0 = responses.get(0);
        assertEquals("10", response0.getId());
        assertEquals(MaterialDto.builder().name("Materialname").quantity(200000.0).standard("ISO9001").category("Mask").imageUrls(Arrays.asList("https://oss.com/images/1.png", "https://oss.com/images/2.png")).build(), response0.getMaterial());
        assertEquals("Supplier Organisation", response0.getOrganisationName());
        assertEquals("logo.jpg", response0.getOrganisationLogo());
        assertEquals(AddressDto.builder()
                .country("中国")
                .province("湖北省")
                .city("武汉市")
                .district("东西湖区")
                .streetAddress("银潭路1号")
                .build(), response0.getAddress());
        assertEquals("Test M", response0.getContactorName());
        assertEquals("18800001111", response0.getContactorPhone());
        assertEquals("Test comment", response0.getComment());
        assertEquals("PENDING", response0.getStatus());
        assertNotNull(response0.getGmtCreated());
        MaterialResponse response1 = responses.get(1);
        assertEquals("11", response1.getId());
        assertEquals(MaterialDto.builder().name("Coat").quantity(3000.0).standard("ISO9002").category("Coat").imageUrls(Arrays.asList("https://oss.com/images/1.png", "https://oss.com/images/2.png")).build(), response1.getMaterial());
        assertEquals("Supplier Organisation", response1.getOrganisationName());
        assertEquals("logo.jpg", response1.getOrganisationLogo());
        assertEquals(AddressDto.builder()
                .country("中国")
                .province("湖北省")
                .city("武汉市")
                .district("东西湖区")
                .streetAddress("银潭路1号")
                .build(), response1.getAddress());
        assertEquals("Test M", response1.getContactorName());
        assertEquals("18800001111", response1.getContactorPhone());
        assertEquals("Test comment", response1.getComment());
        assertEquals("PENDING", response1.getStatus());
        assertNotNull(response1.getGmtCreated());
    }

    @Test
    void should_save_multiple_materials_with_status_pending_when_create_given_material_request_containing_multiple_materials() {
        materialSuppliedService.create(
                MaterialRequest.builder()
                        .materials(Arrays.asList(
                                MaterialDto.builder().name("Materialname").quantity(200000.0).standard("ISO9001").category("Mask").imageUrls(Arrays.asList("https://oss.com/images/1.png", "https://oss.com/images/2.png")).build(),
                                MaterialDto.builder().name("Coat").quantity(3000.0).standard("ISO9002").category("Coat").imageUrls(Arrays.asList("https://oss.com/images/1.png", "https://oss.com/images/2.png")).build()
                        ))
                        .organisationName("Supplier Organisation")
                        .address(AddressDto.builder()
                                .country("中国")
                                .province("湖北省")
                                .city("武汉市")
                                .district("东西湖区")
                                .streetAddress("银潭路1号")
                                .build())
                        .contactorName("Test M")
                        .contactorPhone("18800001111")
                        .comment("Test comment")
                        .build(),
                1L, 2L);

        ArgumentCaptor<List> materialsCaptor = ArgumentCaptor.forClass(List.class);
        verify(materialSuppliedService).saveBatch(materialsCaptor.capture());
        List<MaterialSuppliedEntity> materials = materialsCaptor.getValue();
        assertEquals(2, materials.size());
        MaterialSuppliedEntity material0 = materials.get(0);
        assertEquals("Materialname", material0.getMaterialSuppliedName());
        assertEquals("Mask", material0.getMaterialSuppliedCategory());
        assertEquals("ISO9001", material0.getMaterialSuppliedStandard());
        assertEquals("Test M", material0.getMaterialSuppliedContactorName());
        assertEquals("18800001111", material0.getMaterialSuppliedContactorPhone());
        assertEquals("中国", material0.getCountry());
        assertEquals("湖北省", material0.getProvince());
        assertEquals("武汉市", material0.getCity());
        assertEquals("东西湖区", material0.getDistrict());
        assertEquals("银潭路1号", material0.getStreetAddress());
        assertEquals(200000.0, material0.getMaterialSuppliedQuantity().doubleValue());
        assertEquals("Supplier Organisation", material0.getMaterialSuppliedOrganizationName());
        assertEquals(1L, material0.getMaterialSupplierOrganizationId().longValue());
        assertEquals(2L, material0.getMaterialSuppliedUserId().longValue());
        assertEquals("Test comment", material0.getMaterialSuppliedComment());
        assertEquals("PENDING", material0.getMaterialSuppliedStatus());
        assertNotNull(material0.getGmtCreated());
        MaterialSuppliedEntity material1 = materials.get(1);
        assertEquals("Coat", material1.getMaterialSuppliedName());
        assertEquals("Coat", material1.getMaterialSuppliedCategory());
        assertEquals("ISO9002", material1.getMaterialSuppliedStandard());
        assertEquals("Test M", material1.getMaterialSuppliedContactorName());
        assertEquals("18800001111", material1.getMaterialSuppliedContactorPhone());
        assertEquals("中国", material1.getCountry());
        assertEquals("湖北省", material1.getProvince());
        assertEquals("武汉市", material1.getCity());
        assertEquals("东西湖区", material1.getDistrict());
        assertEquals("银潭路1号", material1.getStreetAddress());
        assertEquals(3000.0, material1.getMaterialSuppliedQuantity().doubleValue());
        assertEquals("Supplier Organisation", material1.getMaterialSuppliedOrganizationName());
        assertEquals(1L, material1.getMaterialSupplierOrganizationId().longValue());
        assertEquals(2L, material1.getMaterialSuppliedUserId().longValue());
        assertEquals("Test comment", material1.getMaterialSuppliedComment());
        assertEquals("PENDING", material1.getMaterialSuppliedStatus());
        assertNotNull(material1.getGmtCreated());
    }

    @Test
    void should_save_materials_as_published_when_create_given_request_and_user_is_verified() {
        when(userInfoService.getUser(anyLong())).thenReturn(UserInfoEntity.builder().status(UserStatus.VERIFIED.name()).build());
        materialSuppliedService.create(
                MaterialRequest.builder()
                        .materials(Arrays.asList(
                                MaterialDto.builder().name("Materialname").quantity(200000.0).standard("ISO9001").category("Mask").imageUrls(Arrays.asList("https://oss.com/images/1.png", "https://oss.com/images/2.png")).build(),
                                MaterialDto.builder().name("Coat").quantity(3000.0).standard("ISO9002").category("Coat").imageUrls(Arrays.asList("https://oss.com/images/1.png", "https://oss.com/images/2.png")).build()
                        ))
                        .organisationName("Supplier Organisation")
                        .address(AddressDto.builder()
                                .country("中国")
                                .province("湖北省")
                                .city("武汉市")
                                .district("东西湖区")
                                .streetAddress("银潭路1号")
                                .build())
                        .contactorName("Test M")
                        .contactorPhone("18800001111")
                        .comment("Test comment")
                        .build(),
                1L, 2L);

        ArgumentCaptor<List> materialsCaptor = ArgumentCaptor.forClass(List.class);
        verify(materialSuppliedService).saveBatch(materialsCaptor.capture());
        List<MaterialSuppliedEntity> materials = materialsCaptor.getValue();
        assertEquals(2, materials.size());
        assertTrue(materials.stream().allMatch(MaterialSuppliedEntity::isApproved));
    }

    @Test
    void should_update_material_when_supplied_is_present_and_publisher_of_the_supplied_is_request_user() {
        when(materialSuppliedService.updateById(any(MaterialSuppliedEntity.class)))
                .thenReturn(true);
        when(materialSuppliedMapper.selectById(anyLong()))
                .thenReturn(MaterialSuppliedEntity.builder()
                        .id(223L).materialSuppliedUserId(1L).materialSupplierOrganizationId(1L).build());
        MaterialResponse suppliedInfo = materialSuppliedService.update(
                223L,
                MaterialRequest.builder()
                        .address(AddressDto.builder()
                                .country("中国")
                                .province("湖北省")
                                .city("武汉市")
                                .district("东西湖区")
                                .streetAddress("银潭路1号")
                                .build())
                        .contactorName("张三")
                        .contactorPhone("18801234567")
                        .comment("医护人员急用")
                        .materials(Collections.singletonList(MaterialDto.builder()
                                .name("N95口罩")
                                .category("口罩")
                                .quantity(100000.0)
                                .standard("ISO-8859-1")
                                .imageUrls(Arrays.asList("https://oss.com/b.jpg", "https://oss.com/a.jpg"))
                                .build()))
                        .build());
        assertEquals("223", suppliedInfo.getId());
    }

    @Test
    void should_throw_access_denied_exception_when_publisher_of_the_supplied_is_not_requesting_user() {
        when(materialSuppliedMapper.selectById(anyLong()))
                .thenReturn(MaterialSuppliedEntity.builder()
                        .materialSuppliedUserId(123L)
                        .build());
        when(userContext.getUserId()).thenReturn(12L);
        assertThrows(AccessDeniedException.class
                , () -> materialSuppliedService.update(223L
                        , MaterialRequest.builder()
                                .address(AddressDto.builder()
                                        .country("中国")
                                        .province("湖北省")
                                        .city("武汉市")
                                        .district("东西湖区")
                                        .streetAddress("银潭路1号")
                                        .build())
                                .contactorName("张三")
                                .contactorPhone("18801234567")
                                .comment("医护人员急用")
                                .materials(Collections.singletonList(MaterialDto.builder()
                                        .name("N95口罩")
                                        .category("口罩")
                                        .quantity(100000.0)
                                        .standard("ISO-8859-1")
                                        .imageUrls(Arrays.asList("https://oss.com/b.jpg", "https://oss.com/a.jpg"))
                                        .build()))
                                .build()));
    }

    @Test
    void should_throw_material_notFound_exception_when_supplied_is_absent() {
        when(materialSuppliedMapper.selectById(anyLong())).thenReturn(null);
        assertThrows(MaterialNotFoundException.class
                , () -> materialSuppliedService.update(223L
                        , MaterialRequest.builder()
                                .address(AddressDto.builder()
                                        .country("中国")
                                        .province("湖北省")
                                        .city("武汉市")
                                        .district("东西湖区")
                                        .streetAddress("银潭路1号")
                                        .build())
                                .contactorName("张三")
                                .contactorPhone("18801234567")
                                .comment("医护人员急用")
                                .materials(Collections.singletonList(MaterialDto.builder()
                                        .name("N95口罩")
                                        .category("口罩")
                                        .quantity(100000.0)
                                        .standard("ISO-8859-1")
                                        .imageUrls(Arrays.asList("https://oss.com/b.jpg", "https://oss.com/a.jpg"))
                                        .build()))
                                .build()));
    }
}