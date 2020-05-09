package sample.application.service;

import fxQuick.FXFeign.FXFeignClient;
import fxQuick.annotations.FXService;

@FXService
public class OtherService {

    FXFeignClient fxFeignClient;

    public OtherService(FXFeignClient testFeign) {
        this.fxFeignClient = testFeign;
    }

    public void test() {
        System.out.println("It works");
    }
}
