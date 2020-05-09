package sample.application.service;

import feign.RequestLine;
import fxQuick.annotations.FXFeignClient;

import java.util.Map;

@FXFeignClient(baseUrl = "http://localhost:8081", api = "test")
public interface TestFeignFX {

    @RequestLine("GET")
    public Map<String,Object> getTest();
}