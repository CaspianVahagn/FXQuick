package fxQuick.iViews;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;

import fxQuick.FXInject;
import fxQuick.ServiceManager;
import fxQuick.exeptions.FXViewException;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * 
 * @author Dominik Leipelt
 * 
 *         Simple View to Load FXML and to Switch views
 * 
 *         overrwrite Init to apply Changes
 * 
 *         call loadFXML(url) to load a file and this Class will be the
 *         Controller of the FXML-file
 * 
 *         call setNodeProperty or setParentProperty, to dock this view to a
 *         Parent to make Switching possible
 * 
 *         call swichTo(View) to switch to a new view
 */
public abstract class FXView extends FXBase {

	protected class Wrapper<T> {
		public T value;

		public Wrapper(T value) {
			this.value = value;
		}
	}

	private FXMLLoader loader;

	private Node root;

	private String viewId;

	public abstract void init();

	private ObjectProperty<Node> nodeProperty;

	private ObjectProperty<Parent> parentProperty;

	public FXView() {
		super();
		init();

	}

	/**
	 * Loads a FXML file an sets this class to its controller inject UI object with
	 * ids with @FXML
	 * 
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

	/**
	 * Switches to a new View and will dock the new View to the Parent or Parent
	 * Node of the current View If no Parentproperty or Nodeproperty is set, nothing
	 * will happen.
	 * 
	 * @param view
	 */
	public void switchTo(FXView view) {
		ServiceManager.viewIdMap.remove(viewId);
		if (getRoot().getParent() != null && !(getRoot().getParent() instanceof BorderPane)) {
			Pane p = (Pane) getRoot().getParent();
			int index = p.getChildren().indexOf(getRoot());
			p.getChildren().remove(getRoot());
			p.getChildren().add(index, view.getRoot());

		} else {
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

	}

	/**
	 * Dock a Parent or Node Property to the new View, to enable View switching.
	 * <br>
	 * example: <code>
	 * <br/>
	 * FXView view = new ExampleVie();
	 * <br/>
	 * BorderPane bp = new BorderPane();
	 * <br/>
	 * view.dock(bp.centerProperty()); 
	 *</code>
	 * 
	 * @param dockProperty
	 * @throws FXViewException
	 */
	@SuppressWarnings("unchecked")
	public void dock(ObjectProperty<?> dockProperty) throws FXViewException {
		if (dockProperty.get() instanceof Parent) {
			setParentProperty((ObjectProperty<Parent>) dockProperty);
			this.getParentProperty().set((Parent) getRoot());
		} else if (dockProperty.get() instanceof Node) {
			setNodeProperty((ObjectProperty<Node>) dockProperty);
			this.nodeProperty.set(this.getRoot());
		} else {
			throw new FXViewException("Objectproperty<" + dockProperty.getValue().getClass().getSimpleName()
					+ "> not supported - Use <Parent> or <Node>");
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

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		ServiceManager.viewIdMap.put(viewId, this);
		this.viewId = viewId;
	}

	public FXView injectViewById(String id) {
		while(!ServiceManager.viewIdMap.containsKey(id)) {
			
		}
		if (ServiceManager.viewIdMap.containsKey(id)) {
			
			System.out.println("CALL");
			return ServiceManager.viewIdMap.get(id);

		}else {
			System.out.println("CALL failed: " + id );
		}
		return null;
	}

}
