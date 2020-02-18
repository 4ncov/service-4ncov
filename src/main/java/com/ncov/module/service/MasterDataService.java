package com.ncov.module.service;

import com.ncov.module.mapper.ProvinceCityDistrictMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MasterDataService {

    private final ProvinceCityDistrictMapper provinceCityDistrictMapper;

    public List<String> getProvinces() {
        return provinceCityDistrictMapper.selectDistinctProvinces();
    }

    public List<String> getCitiesByProvince(String province) {
        return provinceCityDistrictMapper.selectDistinctCitiesByProvince(province);
    }

    public List<String> getDistrictsByProvinceAndCity(String province, String city) {
        return provinceCityDistrictMapper.selectDistinctDistrictsByProvinceAndCity(province, city);
    }
}
