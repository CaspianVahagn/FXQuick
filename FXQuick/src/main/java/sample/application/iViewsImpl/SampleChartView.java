package sample.application.iViewsImpl;


import fxQuick.MatIcons;
import fxQuick.iViews.FXView;
import fxQuick.iViews.Props;
import fxQuick.icon.Icon;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class SampleChartView extends FXView {

    @FXML
    BarChart<String, Integer> barChart;
    @FXML
    VBox iconBox;
    @FXML
    Button miau;

    public SampleChartView() {
        super();
    }

    public SampleChartView(Props props) {
        super(props);
    }

    @Override
    public void init(Props props) {

        loadFXML("view/chartSample.fxml");
        generateDummyData();
        async(() -> {

            for (MatIcons icon : MatIcons.values()) {
                Icon il = new Icon();
                il.setIconName(icon.toString());
                il.minWidth(60);
                il.getStyleClass().add("primary-font");
                il.setFill(Color.WHITE);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Label l = new Label(icon.name());
                l.getStyleClass().add("primary-font");
                l.setGraphic(il);
                Platform.runLater(() -> iconBox.getChildren().add(l));
            }
            return 23;
        }).await((i) -> {
            System.out.println(i);
        });


        miau.setText(props.get("bastard"));


    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void generateDummyData() {
        XYChart.Series<String, Integer> series1 = new XYChart.Series<String, Integer>();
        series1.setName("Sommer");

        series1.getData().add(new XYChart.Data("Sonne", (int) (Math.random() * 1000)));
        series1.getData().add(new XYChart.Data("Regen", (int) (Math.random() * 700)));
        series1.getData().add(new XYChart.Data("Schnee/Hagel", (int) (Math.random() * 50)));


        XYChart.Series<String, Integer> series2 = new XYChart.Series<String, Integer>();
        series2.setName("Winter");

        series2.getData().add(new XYChart.Data("Sonne", (int) (Math.random() * 700)));
        series2.getData().add(new XYChart.Data("Regen", (int) (Math.random() * 1000)));
        series2.getData().add(new XYChart.Data("Schnee/Hagel", (int) (Math.random() * 1000)));
        XYChart.Series<String, Integer> series3 = new XYChart.Series<String, Integer>();
        series3.setName("Herbst");
        series3.getData().add(new XYChart.Data("Sonne", (int) (Math.random() * 700)));
        series3.getData().add(new XYChart.Data("Regen", (int) (Math.random() * 1000)));
        series3.getData().add(new XYChart.Data("Schnee/Hagel", (int) (Math.random() * 1000)));
        barChart.getData().addAll(series1, series2, series3);


    }

    public void backTo(ActionEvent e) {
        switchTo(new SampleBasicView());
    }

    public void miaumal() {
        miau.setText("Other view Attack");
    }

}
