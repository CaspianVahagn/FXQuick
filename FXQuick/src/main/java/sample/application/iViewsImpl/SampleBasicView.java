package sample.application.iViewsImpl;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fxQuick.FXController;
import fxQuick.iViews.FXView;
import fxQuick.iViews.Props;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class SampleBasicView extends FXView {

	@FXML
	private Text HalloWelt;

	private SampleChartView otherview;

	public SampleBasicView() {
		super();
	}

	public SampleBasicView(Props props) {
		super(props);
	}

	@Override
	public void init(Props props) {
		loadFXML("view/sample.fxml");
		async(200,()->{
			Thread.sleep(1000);
			return "hello";
		}).await((err,p )->{
			
			System.out.println(err);
			props.<Runnable>ifContains("fun", e -> e.run()).otherwise(()-> System.out.println("ERROR"));
			System.out.println("Props: " + props.get("bastard"));
		});
		

	}

	public void basicAction(ActionEvent e) {

		switchTo(new SampleBasicView2());

	}

}
