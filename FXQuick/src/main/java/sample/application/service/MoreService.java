package sample.application.service;

import fxQuick.annotations.FXService;

@FXService
public class MoreService {

    SampleService sampleService;

    public MoreService(SampleService sampleService) {
        this.sampleService = sampleService;
    }
}
