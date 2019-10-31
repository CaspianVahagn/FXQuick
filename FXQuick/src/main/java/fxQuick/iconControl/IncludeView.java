package fxQuick.iconControl;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



import fxQuick.ServiceManager;
import fxQuick.iViews.FXView;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import sample.application.iViewsImpl.SampleChartView;


public class IncludeView extends AnchorPane {

	private String viewName;
	private String viewId;
	

	public String getViewName() {
		return viewName;
	}
	

	public void setViewName(String viewName) {
		this.viewName = viewName;
		SimpleBooleanProperty done = new SimpleBooleanProperty(false);
		Thread thread = new Thread(() -> {
			while (this.getParent() == null) {

			}
           
			Pane t = (Pane) this.getParent();
			if (t instanceof BorderPane) {
				FXView view = null;
				if (!viewName.contains(".")) {
					for (String packageName : ServiceManager.getPackageQualifier()) {

						try {
							view = (FXView) (Class.forName(packageName + "." + viewName).getDeclaredConstructor().newInstance());
						} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {

						}

					}
				} else {
					try {
						view = (FXView) (Class.forName(viewName).getDeclaredConstructor().newInstance());
					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {

					}
				}
				
				FXView viewInstance = view;
				if(viewId != null) {
					viewInstance.setViewId(viewId);
					System.out.println(viewId);
				}
				Platform.runLater(() -> {
					if (((BorderPane) t).leftProperty().get() != null &&((BorderPane) t).leftProperty().get().equals(this)) {
						((BorderPane) t).leftProperty().set(viewInstance.getRoot());
						viewInstance.setNodeProperty(((BorderPane) t).leftProperty());
					}
					if (((BorderPane) t).topProperty().get() != null && ((BorderPane) t).topProperty().get().equals(this)) {
						((BorderPane) t).topProperty().set(viewInstance.getRoot());
						viewInstance.setNodeProperty(((BorderPane) t).topProperty());
					}

					if (((BorderPane) t).rightProperty().get() != null && ((BorderPane) t).rightProperty().get().equals(this)) {
						((BorderPane) t).rightProperty().set(viewInstance.getRoot());
						viewInstance.setNodeProperty(((BorderPane) t).rightProperty());
					}

					if (((BorderPane) t).centerProperty().get() != null && ((BorderPane) t).centerProperty().get().equals(this)) {
						((BorderPane) t).centerProperty().set(viewInstance.getRoot());
						viewInstance.setNodeProperty(((BorderPane) t).centerProperty());
					}
				});

			} else {

				FXView view = null;
				if (!viewName.contains(".")) {
					for (String packageName : ServiceManager.getPackageQualifier()) {

						try {
							view = (FXView) (Class.forName(packageName + "." + viewName).newInstance());
						} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {

						}

					}
				} else {
					try {
						view = (FXView) (Class.forName(viewName).newInstance());
					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {

					}
				}
				FXView viewInstance = view;
				if (view != null) {
					if(viewId != null) {
						viewInstance.setViewId(viewId);
					}
					Platform.runLater(() -> {
						t.getChildren().remove(this);
						t.getChildren().add(viewInstance.getRoot());
					});
				} else {
					Label l = new Label("ERROR " + viewName + " not in Namespaces "
							+ Arrays.asList(ServiceManager.getPackageQualifier()) + " +. Try Qualified Name!");
					l.setTextFill(Color.RED);
					l.setStyle("-fx-font-weight:bold;-fx-background-color:black;");
					l.setMinHeight(40);

					Platform.runLater(() -> {
						t.getChildren().add(l);
					});
				}
				
				
			}
			done.set(true);
		});
		thread.setDaemon(true);
		
		thread.start();
		done.addListener((a,b,c)->{
			System.out.println("DONE");
			
		});
		

	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
			
		this.viewId = viewId;
	}

}
