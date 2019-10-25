package fxQuick.iconControl;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.javafx.scene.control.skin.TabPaneSkin;

import fxQuick.ServiceManager;
import fxQuick.iViews.FXView;
import javafx.application.Platform;
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

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
		ExecutorService es = Executors.newSingleThreadScheduledExecutor(r ->{
			Thread t = Executors.defaultThreadFactory().newThread(r);
			t.setDaemon(true);
			return t;
		});
		es.execute(() -> {
			while (this.getParent() == null) {

			}

			Pane t = (Pane) this.getParent();
			if (t instanceof BorderPane) {
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
		});

	}

}
