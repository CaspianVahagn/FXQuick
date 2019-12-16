package fxQuick;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;

public class QuickFxmlLoader extends FXMLLoader {

	private Object controllerStored;

	public void setStoredController(Object controller) {
		this.controllerStored = controller;
	}

	public QuickFxmlLoader(URL val) {
		super(val);
	}

	@Override
	public void setController(Object controller) {
		System.out.println("Contr:" + controller);
		if (controllerStored != null) {
			super.setController(null);
			
			super.setController(controllerStored);
		} else {
			super.setController(controller);
		}
	}

}
