package fxQuick.iViews;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;



import fxQuick.FXInject;
import fxQuick.ServiceManager;
import fxQuick.exeptions.FXViewException;
import fxQuick.iconControl.IncludeView;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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

	

	private ObjectProperty<Node> nodeProperty;

	private ObjectProperty<Parent> parentProperty;
	
	private final Props state = new Props();

	
	public FXView() {
		super();
		init(new Props());

	}
	
	public FXView(Props props) {
		super();
		init(props);
		

	}
	
	public void setState(String s, Object o) {
		this.state.add(s, o);
	}
	
	public abstract void init(Props props);

	/**
	 * Loads a FXML file an sets this class to its controller inject UI object with
	 * ids with @FXML
	 * 
	 * @param url
	 */
	public void loadFXML(String url) {
		URL val = FXView.class.getClassLoader().getResource(url);
		loader = new FXMLLoader(val);
		boolean lookup = false;
		try {
			InputStream is = FXView.class.getClassLoader().getResourceAsStream(url);
			byte[] buffer = new byte[2024];
		    StringBuilder stringBuilder = new StringBuilder();
		    int length = 0;

		    while ((length = is.read(buffer)) >= 0) {
		        stringBuilder.append(new String(Arrays.copyOfRange(buffer, 0, length), "UTF-8"));
		    }

		    String contents = stringBuilder.toString();
			
			if(contents.contains("IncludeView")) {
				lookup = true;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		Map<String, Object> ns = getNameSpace();
//		
//		ns.put("props", state);		
		
		loader.setController(this);
		try {
			setRoot(loader.load());
			if(lookup) {
				System.out.println("Check for includes");
				for(IncludeView inc: getAllNodes((Parent) root)) {
					inc.invokeInclude(state);
					
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static ArrayList<IncludeView> getAllNodes(Parent root) {
	    ArrayList<IncludeView> nodes = new ArrayList<IncludeView>();
	    addAllDescendents(root, nodes);
	    return nodes;
	}

	private static void addAllDescendents(Parent parent, ArrayList<IncludeView> nodes) {
	    for (Node node : parent.getChildrenUnmodifiable()) {
	    	
	    	if(node instanceof IncludeView) {
	    		nodes.add((IncludeView) node);
	    	}
	    	if(node instanceof TabPane) {
	    		TabPane p = (TabPane) node;
	    	
	    		
	    		for (Tab tnode : p.getTabs()) {
	    			
	    			
	    			 if (tnode.getContent() instanceof Parent)
	    		            addAllDescendents((Parent)tnode.getContent(), nodes);
	    			
	    		}
	    	}
	        if (node instanceof Parent)
	            addAllDescendents((Parent)node, nodes);
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
	
	public ObservableMap<String,Object> getNameSpace(){
		return loader.getNamespace();
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

	/**
	 * Makes FXView Injection possible for other views, by calling the id
	 * 
	 * @param viewId : the Id for the view
	 */
	public void setViewId(String viewId) {
		ServiceManager.viewIdMap.put(viewId, this);
		this.viewId = viewId;
	}

	public FXView injectViewById(String id) {
		while (!ServiceManager.viewIdMap.containsKey(id)) {

		}
		if (ServiceManager.viewIdMap.containsKey(id)) {
			
			return ServiceManager.viewIdMap.get(id);

		}
		return null;
	}

}
