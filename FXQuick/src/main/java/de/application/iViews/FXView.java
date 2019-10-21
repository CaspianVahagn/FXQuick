package de.application.iViews;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;

import FXQuick.FXInject;
import FXQuick.ServiceManager;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

public abstract class FXView {

	private FXMLLoader loader;

	private Node root;

	public abstract void init();

	private ObjectProperty<Node> nodeProperty;

	private ObjectProperty<Parent> parentProperty;

	public FXView() {
		init();
		loadInjections();
	}

	public void loadFXML(String url) {
		URL val = FXView.class.getClassLoader().getResource(url);
		loader = new FXMLLoader(val);
		loader.setController(this);
		try {
			setRoot(loader.load());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void switchTo(FXView view) {

		if (parentProperty != null) {
			parentProperty.set((Parent) view.root);
			view.setParentProperty(this.parentProperty);
			setParentProperty(null);
		}
		if (nodeProperty != null) {
			nodeProperty.set(view.root);
			view.setNodeProperty(this.nodeProperty);
			setNodeProperty(null);
		}

	}

	@SuppressWarnings("unchecked")
	public <T> T inject(Class<T> clazz) {
		return (T) ServiceManager.CLASSES.get(clazz);

	}

	private void loadInjections() {
		if (ServiceManager.DEMANDED_FILED.containsKey(this.getClass())) {
			for (Field f : ServiceManager.DEMANDED_FILED.get(this.getClass())) {
				try {
					f.setAccessible(true);

					f.set(this, ServiceManager.CLASSES.get(f.getType()));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();

				} catch (IllegalAccessException e) {
					e.printStackTrace();

				}
			}
		}
		

	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	public ObjectProperty<Node> getNodeProperty() {
		return nodeProperty;
	}

	public void setNodeProperty(ObjectProperty<Node> nodeProperty) {
		this.nodeProperty = nodeProperty;
	}

	public ObjectProperty<Parent> getParentProperty() {
		return parentProperty;
	}

	public void setParentProperty(ObjectProperty<Parent> parentProperty) {
		this.parentProperty = parentProperty;
	}

}
