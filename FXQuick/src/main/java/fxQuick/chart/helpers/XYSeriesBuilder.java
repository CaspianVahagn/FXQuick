package fxQuick.chart.helpers;

import javafx.scene.chart.XYChart;

public class XYSeriesBuilder {

    private XYChart.Series series;

    public XYSeriesBuilder createSeries(String name){
        series = new XYChart.Series();
        series.setName(name);
        return this;
    }

    public <X,Y> XYSeriesBuilder addData(X x, Y y){
        series.getData().addAll(new XYChart.Data(x,y));
        return this;
    }

    public XYChart.Series build(){
        return series;
    }
}
