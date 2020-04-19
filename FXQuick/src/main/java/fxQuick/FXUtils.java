package fxQuick;

import javafx.animation.*;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.InputStream;
import java.net.URL;

public class FXUtils {

    public static InputStream loadStreamFromResources(String resourceFileName){
        return FXConfigration.getApp().getClass().getClassLoader().getResourceAsStream(resourceFileName);
    }

    public static URL loadFromResources(String resourceFileName){
        return FXConfigration.getApp().getClass().getClassLoader().getResource(resourceFileName);
    }

    public static void hoverColorTransition(Control node, Color from, Color to, final int duration){
        Transition t = new Transition() {
            {
                setCycleDuration(Duration.millis(duration));
                setInterpolator(Interpolator.SPLINE(0.1,.9,0.7,0.4));
            }

            @Override
            protected void interpolate(double frac) {
               node.setBackground(new Background(new BackgroundFill(from.interpolate(to,frac), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        };
        Transition t2 = new Transition() {
            {
                setCycleDuration(Duration.millis(duration));
                setInterpolator(Interpolator.SPLINE(0.1,.9,0.7,0.4));
            }

            @Override
            protected void interpolate(double frac) {
                node.setBackground(new Background(new BackgroundFill(to.interpolate(from,frac), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        };
        node.setOnMouseEntered(mouseEvent -> t.play());
        node.setOnMouseExited(mouseEvent -> t2.play());
    }
}
