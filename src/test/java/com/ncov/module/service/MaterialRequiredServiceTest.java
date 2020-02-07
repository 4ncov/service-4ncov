package com.ncov.module.service;

import com.ncov.module.controller.dto.MaterialDto;
import com.ncov.module.controller.request.material.MaterialRequest;
import com.ncov.module.controller.resp.material.MaterialResponse;
import com.ncov.module.entity.MaterialRequiredEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class MaterialRequiredServiceTest {

    @Spy
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
    }

    @Test
    public void should_save_material_when_save_required_info_given_request_and_organisation_id_and_user_id() {
        MaterialDto materialDto = MaterialDto.builder()
                .name("N95口罩")
                .category("口罩")
                .quantity(100000.0)
                .standard("ISO-8859-1")
                .build();
        MaterialRequest request = MaterialRequest.builder()
                .address("湖北省武汉市东西湖区银潭路1号")
                .contactorName("张三")
                .contactorPhone("18801234567")
                .comment("医护人员急用")
                .imageUrls(Arrays.asList("https://oss.com/b.jpg", "https://oss.com/a.jpg"))
                .materials(Collections.singletonList(materialDto))
                .organisationName("哈哈")
                .build();

        List<MaterialResponse> responses = materialRequiredService.saveRequiredInfo(request, 1L, 2L);

        assertEquals(1, responses.size());
        MaterialResponse response = responses.get(0);
        assertEquals(materialDto, response.getMaterial());
        assertEquals("湖北省武汉市东西湖区银潭路1号", response.getAddress());
        assertEquals("张三", response.getContactorName());
        assertEquals("18801234567", response.getContactorPhone());
        assertEquals("医护人员急用", response.getComment());
        assertEquals("哈哈", response.getOrganisationName());
        assertEquals("PENDING", response.getStatus());
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
                .address("湖北省武汉市东西湖区银潭路1号")
                .contactorName("张三")
                .contactorPhone("18801234567")
                .comment("医护人员急用")
                .imageUrls(Arrays.asList("https://oss.com/b.jpg", "https://oss.com/a.jpg"))
                .materials(Arrays.asList(
                        MaterialDto.builder().name("N95口罩").category("口罩").quantity(100000.0).standard("ISO-8859-1").build(),
                        MaterialDto.builder().name("医用口罩").category("口罩").quantity(50000.0).standard("ISO-8859-1").build()
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
}
