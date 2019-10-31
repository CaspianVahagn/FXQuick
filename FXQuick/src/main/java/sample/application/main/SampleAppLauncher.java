package sample.application.main;

import java.lang.reflect.Field;
import fxQuick.FXConfigration;
import fxQuick.iViews.FXView;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import sample.application.iViewsImpl.SampleBasicView;

public class SampleAppLauncher extends Application {

	public final String INCONSOLATA_FONT = "https://fonts.googleapis.com/css?family=Inconsolata:400,700&display=swap";
	

	
	@Override
	public void start(Stage stage) throws Exception {
		
		FXConfigration.scanServices("sample.application.service");
		FXConfigration.scanRuntimeInjections("sample.application.iViewsImpl");
		FXConfigration.addViewNameSpaces("sample.application.iViewsImpl");
		FXConfigration.apply();

		FXView view = new SampleBasicView();
		Parent root = (Parent) view.getRoot(); 

		Scene scene = new Scene(root, 1200, 900);
		
		view.dock(scene.rootProperty());
		scene.getStylesheets().add(INCONSOLATA_FONT);
		scene.getStylesheets().add("style.css");
		stage.setTitle("mainApp");
		stage.setScene(scene);
	
		stage.getIcons()
				.add(new Image(SampleAppLauncher.class.getClassLoader().getResourceAsStream("images/icon.png")));

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				System.out.println("BYEBYE");
				Platform.exit();
				System.exit(0);
			}
		});
		stage.show();
		
	
		
//		Stage.getWindows().addListener(new ListChangeListener<Window>() {
//
//			@Override
//			public void onChanged(Change<? extends Window> c) {
//
//				for (Window open : c.getList()) {
//
//					Window window = (Window) open;
//					if (window.getScene().getRoot() instanceof DialogPane) {
//						
//						Platform.runLater(() -> {
//							
//							((Stage) window).close();
//						});
//
//					}
//
//				}
//
//			}
//		});

		Alert a = new Alert(AlertType.ERROR);
		//a.showAndWait();
		IntegerProperty i = new SimpleIntegerProperty(0);
		System.out.println("Number of active threads from the given thread: " + Thread.activeCount());
		AnimationTimer t = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				if(Thread.activeCount() != i.get()) {
					System.out.println("Number of active threads from the given thread: " + Thread.activeCount());
					i.set(Thread.activeCount());
				}
				


			}
		};
		t.start();

	}

}
