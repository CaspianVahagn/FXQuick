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

public class SampleBasicView extends FXView {

	@FXML
	private Text HalloWelt;

	private SampleChartView otherview;

	@Override
	public void init() {
		loadFXML("view/sample.fxml");
		async(200,()->{
			return (SampleChartView) injectViewById("holla");
		}).await(view ->{ 
			otherview = view;
		});
		

	}
	
	

	public void basicAction(ActionEvent e) {

		
		
		if (otherview != null) {
			otherview.miaumal();
		} else {
			switchTo(new SampleBasicView2());
		}

	}

}
