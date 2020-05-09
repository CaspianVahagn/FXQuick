package sample.application.service;

import fxQuick.annotations.FXInject;
import fxQuick.annotations.FXService;

import java.util.Collections;
import java.util.Map;

@FXService
public class SampleService {


    OtherService service;

    TestFeignFX testFeignFX;
    @FXInject
    public SampleService(OtherService service, String banane, TestFeignFX testFeignFX) {
        System.out.println("banane = " + banane);
        this.service = service;
        this.testFeignFX = testFeignFX;
    }

    public SampleService(OtherService service) {
        this.service = service;
    }



    public void test() {
        System.out.println("twerks");
        service.test();
    }

    public Map<String,Object> helloTest(){
        System.out.println("CALL MEEEE");
        return testFeignFX.postTest(Collections.singletonMap("Hallo","Server"));
    }

}
