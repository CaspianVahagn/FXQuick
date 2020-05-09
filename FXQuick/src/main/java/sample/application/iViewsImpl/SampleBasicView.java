package sample.application.iViewsImpl;

import sample.application.service.SampleService;
import sample.application.service.TestFeignFX;
import fxQuick.FXUtils;
import fxQuick.annotations.FXInject;
import fxQuick.annotations.ViewConfig;
import fxQuick.iViews.FXView;
import fxQuick.iViews.Props;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

@ViewConfig(
        fxml = "view/sample.fxml",
        styleSheets = ""
)
public class SampleBasicView extends FXView {

    @FXML
    private Text HalloWelt;

    @FXML
    private Button test;

    private SampleChartView otherview;

    @FXML
    private Props values;

    @FXInject
    SampleService sampleService;

    public SampleBasicView() {
        super();
    }

    public SampleBasicView(Props props) {
        super(props);
        props.add("text","bananananaanan");
        this.values = props;
    }

    @Override
    public void init(Props props) {
        async(200, () -> {
            Thread.sleep(1000);
            return "hello";
        }).await((err, p) -> {
            System.out.println("this is a " + err);
            props.<Runnable>ifContains("fun", e -> e.run()).otherwise(() -> System.out.println("ERROR"));
            System.out.println("Props: " + props.get("bastard"));
            FXUtils.hoverColorTransition(test,(Color) test.getBackground().getFills().get(0).getFill(),Color.rgb(11,90,200),300);
        });

    }

    public void basicAction(ActionEvent e) {
        async(() -> {
            return sampleService.helloTest();
            //return "HELLOOO";
        }).await((err, param) -> {
            System.out.println(param);
        });
    }

}
