package sample.application.iViewsImpl;


import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fxQuick.FXController;
import fxQuick.iViews.FXView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;


public class SampleBasicView extends FXView{
	
	@FXML
	private Text HalloWelt;
	
	@Override
	public void init() {
			loadFXML("view/sample.fxml");
			
			
	}
	
	public void basicAction(ActionEvent e) {
		switchTo(new SampleBasicView2());
	}

}
