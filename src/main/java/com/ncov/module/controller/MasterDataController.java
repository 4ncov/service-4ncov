package com.ncov.module.controller;

import com.ncov.module.controller.resp.RestResponse;
import com.ncov.module.service.MasterDataService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/master-data")
public class MasterDataController {

    private final MasterDataService masterDataService;

    @GetMapping("/provinces")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse<List<String>> getChinaProvinces() {
        return RestResponse.getResp("请求成功.", masterDataService.getProvinces());
    }

    @GetMapping("/cities")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse<List<String>> getCities(@RequestParam String province) {
        return RestResponse.getResp("请求成功.", masterDataService.getCitiesByProvince(province));
    }

    @GetMapping("/districts")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse<List<String>> getDistricts(@RequestParam String province, @RequestParam String city) {
        return RestResponse.getResp("请求成功.",
                masterDataService.getDistrictsByProvinceAndCity(province, city));
    }
}
