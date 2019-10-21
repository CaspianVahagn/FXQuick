package de.application.service;

import FXQuick.FXInject;
import FXQuick.FXService;

@FXService
public class SampleService {
	
	
	@FXInject
	OtherService service;
	
	public void test() {
		System.out.println("twerks");
		service.test();
	}

}
