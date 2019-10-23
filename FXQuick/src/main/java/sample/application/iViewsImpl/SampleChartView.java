package sample.application.iViewsImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.javafx.charts.Legend;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import fxQuick.iViews.FXView;
import fxQuick.iconControl.IconLabel;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SampleChartView extends FXView{
	
	@FXML
	BarChart<String, Integer> barChart;
	@FXML
	VBox iconBox;
	
	@Override
	public void init() {
		
			loadFXML("view/chartSample.fxml");
			generateDummyData();
			ExecutorService s = Executors.newSingleThreadExecutor();
			s.execute(()->{
				
				for(FontAwesomeIcons icon:FontAwesomeIcons.values()) {
					IconLabel il = new IconLabel();
					il.setText(icon.toString());
					il.setIcon(icon.toString());
					il.minWidth(60);
					il.getStyleClass().add("primary-font");
					il.setIconColor("white");
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Platform.runLater(()->iconBox.getChildren().add(il));
					
				}
			});
			
		
	}
	
	private void generateDummyData() {
		XYChart.Series<String, Integer> series1 = new XYChart.Series<String, Integer>();
		series1.setName("Sommer" );

		series1.getData().add(new XYChart.Data("Sonne", (int) (Math.random() * 1000)));
		series1.getData().add(new XYChart.Data("Regen", (int) (Math.random() * 700)));
		series1.getData().add(new XYChart.Data("Schnee/Hagel", (int) (Math.random() * 50)));
		
		 
		XYChart.Series<String, Integer> series2 = new XYChart.Series<String, Integer>();
		series2.setName("Winter" );
		 
		series2.getData().add(new XYChart.Data("Sonne", (int) (Math.random() * 700)));
		series2.getData().add(new XYChart.Data("Regen", (int) (Math.random() * 1000)));
		series2.getData().add(new XYChart.Data("Schnee/Hagel", (int) (Math.random() * 1000)));
		
		barChart.getData().addAll(series1, series2);
//		for(Node n : barChart.getChildrenUnmodifiable()){
//			   if(n instanceof Legend){
//			      for(Legend.LegendItem legendItem : ((Legend)n).getItems()){
//			        System.out.println(legendItem);
//			      }
//			   }
//			}
		
	}
	
	public void backTo(ActionEvent e) {
		switchTo(new SampleBasicView());
	}

}
