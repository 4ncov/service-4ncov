package com.ncov.module.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ncov.module.common.enums.MaterialStatus;
import com.ncov.module.common.enums.UserStatus;
import com.ncov.module.common.exception.MaterialNotFoundException;
import com.ncov.module.controller.dto.AddressDto;
import com.ncov.module.controller.dto.MaterialDto;
import com.ncov.module.controller.request.material.MaterialRequest;
import com.ncov.module.controller.resp.material.MaterialResponse;
import com.ncov.module.entity.HospitalInfoEntity;
import com.ncov.module.entity.MaterialRequiredEntity;
import com.ncov.module.entity.UserInfoEntity;
import com.ncov.module.mapper.MaterialRequiredMapper;
import com.ncov.module.security.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.access.AccessDeniedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class MaterialRequiredServiceTest {

    @Mock
    private UserInfoService userInfoService;
    @Mock
    private MaterialRequiredMapper materialRequiredMapper;
    @Mock
    private UserContext userContext;
    @Mock
    private HospitalService hospitalService;
    @Spy
    @InjectMocks
    private MaterialRequiredService materialRequiredService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        doAnswer((call) -> {
            List<MaterialRequiredEntity> entities = call.getArgument(0);
            for (int i = 0; i < entities.size(); ++i) {
                entities.get(i).setId(i + 10L);
            }
            return true;
        }).when(materialRequiredService).saveBatch(anyList());
        when(userInfoService.getUser(anyLong())).thenReturn(UserInfoEntity.builder().status(MaterialStatus.PENDING.name()).build());
        when(userContext.getUserId()).thenReturn(1L);
        when(userContext.isSysAdmin()).thenReturn(false);
        when(hospitalService.list(any(LambdaQueryWrapper.class))).thenReturn(Collections.singletonList(HospitalInfoEntity.builder().id(1L).logo("image.png").build()));
        when(hospitalService.getById(anyLong())).thenReturn(HospitalInfoEntity.builder().id(1L).logo("image.png").build());
    }

    @Test
    public void should_save_material_when_save_required_info_given_request_and_organisation_id_and_user_id() {
        MaterialDto materialDto = MaterialDto.builder()
                .name("N95口罩")
                .category("口罩")
                .quantity(100000.0)
                .standard("ISO-8859-1")
                .imageUrls(Arrays.asList("https://oss.com/b.jpg", "https://oss.com/a.jpg"))
                .build();
        MaterialRequest request = MaterialRequest.builder()
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
                .materials(Collections.singletonList(materialDto))
                .organisationName("哈哈")
                .build();

        List<MaterialResponse> responses = materialRequiredService.saveRequiredInfo(request, 1L, 2L);

        assertEquals(1, responses.size());
        MaterialResponse response = responses.get(0);
        assertEquals(materialDto, response.getMaterial());
        assertEquals(AddressDto.builder().country("中国").province("湖北省").city("武汉市").district("东西湖区").streetAddress("银潭路1号").build(), response.getAddress());
        assertEquals("张三", response.getContactorName());
        assertEquals("18801234567", response.getContactorPhone());
        assertEquals("医护人员急用", response.getComment());
        assertEquals("哈哈", response.getOrganisationName());
        assertEquals("PENDING", response.getStatus());
        assertEquals("image.png", response.getOrganisationLogo());
        assertNotNull(response.getGmtCreated());
        ArgumentCaptor<List> entitiesCaptor = ArgumentCaptor.forClass(List.class);
        verify(materialRequiredService).saveBatch(entitiesCaptor.capture());
        List<MaterialRequiredEntity> entities = entitiesCaptor.getValue();
        assertEquals(1, entities.size());
        MaterialRequiredEntity entity = entities.get(0);
        assertEquals(1L, entity.getMaterialRequiredOrganizationId().longValue());
        assertEquals(2L, entity.getMaterialRequiredUserId().longValue());
    }

    @Test
    void should_save_multiple_materials_when_save_required_info_given_request_containing_multiple_materials() {
        MaterialRequest request = MaterialRequest.builder()
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
                .materials(Arrays.asList(
                        MaterialDto.builder().name("N95口罩").category("口罩").quantity(100000.0).standard("ISO-8859-1").imageUrls(Arrays.asList("https://oss.com/b.jpg", "https://oss.com/a.jpg")).build(),
                        MaterialDto.builder().name("医用口罩").category("口罩").quantity(50000.0).standard("ISO-8859-1").imageUrls(Arrays.asList("https://oss.com/b.jpg", "https://oss.com/a.jpg")).build()
                ))
                .organisationName("哈哈")
                .build();

        List<MaterialResponse> responses = materialRequiredService.saveRequiredInfo(request, 1L, 2L);
        assertEquals(2, responses.size());
        ArgumentCaptor<List> entitiesCaptor = ArgumentCaptor.forClass(List.class);
        verify(materialRequiredService).saveBatch(entitiesCaptor.capture());
        List<MaterialRequiredEntity> entities = entitiesCaptor.getValue();
        assertEquals(2, entities.size());
    }

    @Test
    void should_save_material_as_published_when_save_required_info_given_request_and_user_is_verified() {
        when(userInfoService.getUser(anyLong())).thenReturn(UserInfoEntity.builder().status(UserStatus.VERIFIED.name()).build());
        MaterialRequest request = MaterialRequest.builder()
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
                .materials(Arrays.asList(
                        MaterialDto.builder().name("N95口罩").category("口罩").quantity(100000.0).standard("ISO-8859-1").imageUrls(Arrays.asList("https://oss.com/b.jpg", "https://oss.com/a.jpg")).build(),
                        MaterialDto.builder().name("医用口罩").category("口罩").quantity(50000.0).standard("ISO-8859-1").imageUrls(Arrays.asList("https://oss.com/b.jpg", "https://oss.com/a.jpg")).build()
                ))
                .organisationName("哈哈")
                .build();

        materialRequiredService.saveRequiredInfo(request, 1L, 2L);

        ArgumentCaptor<List> entitiesCaptor = ArgumentCaptor.forClass(List.class);
        verify(materialRequiredService).saveBatch(entitiesCaptor.capture());
        List<MaterialRequiredEntity> entities = entitiesCaptor.getValue();
        assertEquals(2, entities.size());
        assertTrue(entities.stream().allMatch(MaterialRequiredEntity::isApproved));
    }

    @Test
    void should_update_material_when_required_is_present_and_publisher_of_the_required_is_request_user() {
        when(materialRequiredService.updateById(any(MaterialRequiredEntity.class)))
                .thenReturn(true);
        when(materialRequiredMapper.selectById(anyLong()))
                .thenReturn(MaterialRequiredEntity.builder().id(223L)
                        .materialRequiredUserId(1L).materialRequiredOrganizationId(1L).build());
        MaterialResponse requiredInfo = materialRequiredService.update(
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
        assertEquals("223", requiredInfo.getId());
    }

    @Test
    void should_throw_access_denied_exception_when_publisher_of_the_required_is_not_requesting_user() {
        when(materialRequiredMapper.selectById(anyLong()))
                .thenReturn(MaterialRequiredEntity.builder()
                        .materialRequiredUserId(123L)
                        .build());
        assertThrows(AccessDeniedException.class
                , () -> materialRequiredService.update(223L
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
    void should_throw_material_notFound_exception_when_required_is_absent() {
        when(materialRequiredMapper.selectById(anyLong())).thenReturn(null);
        assertThrows(MaterialNotFoundException.class
                , () -> materialRequiredService.update(223L
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
