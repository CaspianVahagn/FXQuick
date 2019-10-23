package de.application.iViewsImpl;


import java.io.IOException;

import de.application.iViews.FXView;
import fxQuick.FXController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;


public class BasicView extends FXView{
	
	@FXML
	private Text HalloWelt;
	
	@Override
	public void init() {
			loadFXML("view/sample.fxml");
	}
	
	public void basicAction(ActionEvent e) {
		switchTo(new BasicView2());
	}

}
