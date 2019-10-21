package de.application.iViewsImpl;


import java.io.IOException;

import FXQuick.FXController;
import de.application.iViews.FXView;
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
