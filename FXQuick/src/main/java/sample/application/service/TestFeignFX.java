package sample.application.service;

import feign.Headers;
import feign.RequestLine;
import fxQuick.FXFeign.FXFeignClient;

import java.util.Map;

@Headers("Content-Type: application/json")
@FXFeignClient(baseUrl = "http://localhost:8081", api = "test")
public interface TestFeignFX {

    @RequestLine("GET")
    public Map<String,Object> getTest();

    @RequestLine("GET /hello")
    public Map<String,Object> getTest2();

    @RequestLine("POST")
    @Headers("Content-Type: application/json")
    public Map<String,Object> postTest(Map<String,Object> value);
}