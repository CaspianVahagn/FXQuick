package fxQuick;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;

public class QuickFxmlLoader extends FXMLLoader{

	private Object controllerStored;
	
	public QuickFxmlLoader(URL val) {
		super(val);
	}

	@Override
	public <T> T load() throws IOException {
		if(getController() != null) {
			controllerStored = getController();
			setController(null);
		}
		return super.load();
	}
	
	@Override
	public void setController(Object controller) {
		if(controller == null) {
			super.setController(null);
		}else {
			if(controllerStored != null) {
				super.setController(controllerStored);
			}else {
				super.setController(controller);
			}
		}

		
	}
	
	
}
