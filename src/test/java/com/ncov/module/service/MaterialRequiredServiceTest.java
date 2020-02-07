package com.ncov.module.service;

import com.ncov.module.controller.dto.MaterialDto;
import com.ncov.module.controller.request.material.MaterialRequest;
import com.ncov.module.entity.MaterialRequiredEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doAnswer;

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
    public void addMaterialRequired(){
        MaterialDto materialDto = MaterialDto.builder()
                .name("N95口罩")
                .category("口罩")
                .quantity(100000.0)
                .standard("ISO-8859-1")
                .build();
        List<MaterialDto> list = new ArrayList<>();
        list.add(materialDto);
        List<String> urls = new ArrayList<>();
        urls.add("https://baidu.com/b.jpg");
        urls.add("https://baidu.com/a.jpg");
        MaterialRequest ma = MaterialRequest.builder().address("湖北省武汉市东西湖区银潭路1号")
                .contactorName("张三")
                .contactorPhone("18801234567")
                .comment("医护人员急用")
                .imageUrls(urls)
                .materials(list)
                .organisationName("哈哈")
                .address("山东")
                .build();
        materialRequiredService.saveRequiredInfo(ma);
    }
}
