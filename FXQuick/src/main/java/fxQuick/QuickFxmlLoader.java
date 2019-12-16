package fxQuick;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import sun.reflect.misc.ReflectUtil;

public class QuickFxmlLoader extends FXMLLoader {

	private Object controllerStored;
	
	

	public void setStoredController(Object controller) {
		this.controllerStored = controller;
	}

	public QuickFxmlLoader(URL val) {
		super(val);
		setControllerFactory(o ->{
			if(controllerStored != null)
			return controllerStored;
			ReflectUtil.checkPackageAccess(o);
			
			try {
				return  o.newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		});
	}

}
