package sample.application.iViewsImpl;

import fxQuick.annotations.FXInject;
import fxQuick.MatIcons;
import fxQuick.chart.helpers.XYSeriesBuilder;
import fxQuick.iViews.FXView;
import fxQuick.iViews.Props;
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
        setState("fun", () -> System.out.println("hallo"));
        loadFXML("view/samle2.fxml");
        generateSampleData();


    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void generateSampleData() {
        XYSeriesBuilder builder = new XYSeriesBuilder();
        builder.createSeries("My portfolio");
        for (int i = 0; i < 12; i++) {
            builder.addData(i,Math.random()*50);
        }
        areaChart.getData().add(builder.build());

        XYChart.Series series2 = new XYChart.Series();
        builder.createSeries("My Other");
        for (int i = 0; i < 12; i++) {
            builder.addData(i,Math.random()*50);
        }
        areaChart.getData().add(builder.build());

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
