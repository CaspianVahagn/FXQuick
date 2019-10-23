package de.application.service;

import fxQuick.FXInject;
import fxQuick.FXService;

@FXService
public class SampleService {
	
	
	@FXInject
	OtherService service;
	
	public void test() {
		System.out.println("twerks");
		service.test();
	}

}
