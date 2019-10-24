package fxQuick.iconControl;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.javafx.scene.control.skin.TabPaneSkin;

import javafx.application.Platform;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import sample.application.iViewsImpl.SampleChartView;

public class IncludeView extends AnchorPane{
	
	private String fxml;
	
	
	public String getFxml() {
		return fxml;
	}

	public void setFxml(String fxml) {
		this.fxml = fxml;
		ExecutorService es = Executors.newSingleThreadExecutor();
		es.execute(()->{
			while(this.getParent() == null) {
				
			}
			Platform.runLater(()->{
				Pane t = (Pane) this.getParent();
				t.getChildren().clear();
				t.getChildren().add(new SampleChartView().getRoot());
			});
			
		});
		
		
		
	}

	
	

}
