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

/**
 * 
 * @author Dominik Leipelt
 * 
 * Simple View to Load FXML and to Switch views
 * 
 * overrwrite Init to apply Changes 
 * 
 * call loadFXML(url) to load a file and this Class will be the Controller of the FXML-file
 * 
 * call setNodeProperty or setParentProperty, to dock this view to a Parent to make
 * Switching possible
 * 
 * call swichTo(View) to switch to a new view
 */
public abstract class FXView extends FXBase{

	private FXMLLoader loader;

	private Node root;

	public abstract void init();

	private ObjectProperty<Node> nodeProperty;

	private ObjectProperty<Parent> parentProperty;

	public FXView() {
		super();
		init();
		
	}
	/**Loads a FXML file an sets this class to its controller
	 * inject UI object with ids with @FXML 
	 * @param url
	 */
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
	/**Switches to a new View and will dock the new View to the Parent or Parent Node of 
	 * the current View 
	 * If no Parentproperty or Nodeproperty is set, nothing will happen.
	 * @param view
	 */
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
