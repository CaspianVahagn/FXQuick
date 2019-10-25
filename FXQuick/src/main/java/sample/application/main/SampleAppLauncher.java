package sample.application.main;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;

import fxQuick.FXConfigration;
import fxQuick.FXController;
import fxQuick.FXInject;
import fxQuick.FXService;
import fxQuick.ServiceManager;
import fxQuick.iViews.FXView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.application.iViewsImpl.SampleBasicView;
import sample.application.service.SampleService;

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

		stage.getIcons().add(new Image(SampleAppLauncher.class.getClassLoader().getResourceAsStream("images/icon.png")));

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				System.out.println("BYEBYE");
				Platform.exit();
				System.exit(0);

			}
		});
		stage.show();
		

	}
	
	public static void main(String[] args) {
		launch(args);
	}


}
