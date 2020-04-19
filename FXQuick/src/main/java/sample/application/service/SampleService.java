package sample.application.service;

import fxQuick.annotations.FXInject;
import fxQuick.annotations.FXService;

@FXService
public class SampleService {


    @FXInject
    OtherService service;

    public void test() {
        System.out.println("twerks");
        service.test();
    }

}
