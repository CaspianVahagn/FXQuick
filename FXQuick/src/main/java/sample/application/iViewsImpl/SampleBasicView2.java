package sample.application.iViewsImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fxQuick.FXController;
import fxQuick.FXInject;
import fxQuick.MatIcons;
import fxQuick.iViews.FXView;
import fxQuick.iViews.Props;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import sample.application.service.SampleService;


public class SampleBasicView2 extends FXView {

	@FXML
	AreaChart<Integer, Integer> areaChart;
	
	@FXML
	Text title;

	@FXInject
	SampleService sampleService;

	@Override
	public void init(Props props) {
		setState("bastard", "Penis");
		loadFXML("view/samle2.fxml");
		generateSampleData();
		

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void generateSampleData() {
		XYChart.Series series = new XYChart.Series();
		series.setName("My portfolio");
		// populating the series with data
		series.getData().add(new XYChart.Data(0, 0));
		series.getData().add(new XYChart.Data(1, 23));
		series.getData().add(new XYChart.Data(2, 14));
		series.getData().add(new XYChart.Data(3, 15));
		series.getData().add(new XYChart.Data(4, 24));
		series.getData().add(new XYChart.Data(5, 34));
		series.getData().add(new XYChart.Data(6, 36));
		series.getData().add(new XYChart.Data(7, 22));
		series.getData().add(new XYChart.Data(8, 45));
		series.getData().add(new XYChart.Data(9, 43));
		series.getData().add(new XYChart.Data(10, 17));
		series.getData().add(new XYChart.Data(11, 29));
		series.getData().add(new XYChart.Data(12, 25));
		areaChart.getData().add(series);

		XYChart.Series series2 = new XYChart.Series();
		series2.setName("Hola");
		for (int i = 0; i < 13; i++) {
			series2.getData().add(new XYChart.Data(i, (int) (Math.random() * 50)));
		}
		areaChart.getData().add(series2);
		
		title.setStyle("-fx-font-family:'Material Icons Outlined';-fx-font-size:12em;");
		title.setText(MatIcons.SD_CARD.getUniCode());
		title.setFill(Color.WHEAT);
	}

	public void basicAction(ActionEvent e) {
		System.out.println(title.getParent());
		sampleService.test();
		
		switchTo(new SampleChartView());
	}

}
