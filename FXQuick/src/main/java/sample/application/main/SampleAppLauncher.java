package sample.application.main;

import fxQuick.FXConfigration;
import fxQuick.FXUtils;
import fxQuick.HotCssScheduler;
import fxQuick.annotations.FXScan;
import fxQuick.iViews.FXView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.application.iViewsImpl.SampleBasicView;

import java.io.IOException;
import java.util.stream.Stream;

@FXScan(rootPackages = {"sample"}, dev = true)
public class SampleAppLauncher extends Application {

    public final String INCONSOLATA_FONT = "https://fonts.googleapis.com/css?family=Inconsolata:400,700&display=swap";


    @Override
    public void start(Stage stage) throws Exception {
        FXConfigration.init(this);

        FXView view = new SampleBasicView();
        Parent root = (Parent) view.getRoot();
        Scene scene = new Scene(root, 1200, 900);

        view.dock(scene.rootProperty());
        scene.getStylesheets().add(INCONSOLATA_FONT);
        scene.getStylesheets().add(FXUtils.loadFromResources("style.css").toString());
        stage.setTitle("mainApp");
        stage.setScene(scene);

        stage.getIcons()
                .add(new Image(FXUtils.loadStreamFromResources("images/icon.png")));

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                System.out.println("BYEBYE");
                Platform.exit();
                System.exit(0);
            }
        });
        stage.show();

        IntegerProperty i = new SimpleIntegerProperty(0);
        System.out.println("Number of active threads from the given thread: " + Thread.activeCount());
        AnimationTimer t = new AnimationTimer() {
            String hash = "";
            long elapsed = 0;
            @Override
            public void handle(long now) {
                if (Thread.activeCount() != i.get()) {
                    Thread.getAllStackTraces().entrySet().stream().forEach(threadEntry -> {
                        System.out.println("--Thread : " + threadEntry.getKey() );
                        Stream.of(threadEntry.getValue()).forEach(stackTraceElement -> {
                            System.out.println("\t \\_ Calls: " + stackTraceElement.getClassName() +"."+ stackTraceElement.getMethodName());

                        });
                    });
                    System.out.println("Number of active threads from the given thread: " + Thread.activeCount());
                    i.set(Thread.activeCount());
                }
            }
        };
        t.start();
//        HotCssScheduler scheduler = new HotCssScheduler();
//        scheduler.addSheetToObserve(scene.getStylesheets());
//        scheduler.start();
    }

}
