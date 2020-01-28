package com.ncov.module.controller;

import com.ncov.module.common.ServiceExecuteResult;
import com.ncov.module.controller.resp.RestResponse;
import com.ncov.module.service.HelloService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@RequestMapping("/api/test")
public class HelloController {

    @Inject
    private HelloService service;

    @GetMapping("/hello")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(RestResponse.getResp("Test", null));
    }

    @PostMapping("hello")
    public ResponseEntity<?> testPost() {
        return ResponseEntity.ok(RestResponse.getResp(service.hello()));
    }

    @PutMapping("hello")
    public ResponseEntity<?> testPut() {
        ServiceExecuteResult<?> result = service.hello();
        return result.isSuccess() ? ResponseEntity.ok(RestResponse.getResp(result)) :
                ResponseEntity.badRequest().body(RestResponse.getResp(result));
    }

}
