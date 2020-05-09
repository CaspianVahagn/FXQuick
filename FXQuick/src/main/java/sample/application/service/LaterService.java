package sample.application.service;

import fxQuick.annotations.FXService;

@FXService
public class LaterService {

        MoreService moreService;
        OtherService otherService;

    public LaterService(MoreService moreService, OtherService otherService) {
        this.moreService = moreService;
        this.otherService = otherService;
    }
}
