package sample.application.service;

import fxQuick.FXFeign.TestFeign;
import fxQuick.annotations.FXService;

@FXService
public class OtherService {

    TestFeign testFeign;

    public OtherService(TestFeign testFeign) {
        this.testFeign = testFeign;
    }

    public void test() {
        System.out.println("It works");
    }
}
