package fxQuick.iViews;

import fxQuick.ServiceManager;
import fxQuick.annotations.FXScan;
import fxQuick.annotations.ViewConfig;
import fxQuick.exeptions.AnnotationScanException;
import fxQuick.exeptions.FXViewException;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import javax.swing.text.View;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author Dominik Leipelt
 * <p>
 * Simple View to Load FXML and to Switch views
 * <p>
 * overrwrite Init to apply Changes
 * <p>
 * call loadFXML(url) to load a file and this Class will be the
 * Controller of the FXML-file
 * <p>
 * call setNodeProperty or setParentProperty, to dock this view to a
 * Parent to make Switching possible
 * <p>
 * call swichTo(View) to switch to a new view
 */
public abstract class FXView extends FXBase {

    private final Props state = new Props();
    private FXMLLoader loader;

    private Node root;

    private String viewId;

    private ObjectProperty<Node> nodeProperty;

    private ObjectProperty<Parent> parentProperty;

    public FXView() {
        super();
        processAnnotations();
        init(new Props());

    }

    public FXView(Props props) {
        super();
        processAnnotations();
        init(props);

    }

    public static ArrayList<IncludeView> getAllNodes(Parent root) {
        ArrayList<IncludeView> nodes = new ArrayList<IncludeView>();
        addAllDescendents(root, nodes);
        return nodes;
    }

    private boolean processAnnotations(){
        Class annotationClass = ViewConfig.class;
        Class applicationClass =this.getClass();
        if(applicationClass.isAnnotationPresent(annotationClass)){
            Annotation annotation = applicationClass.getAnnotation(annotationClass);
            ViewConfig viewConfig = (ViewConfig) annotation;
            loadFXML(viewConfig.fxml());
            if(viewConfig.styleSheets().length >= 1){
                ((Parent)root).getStylesheets().addAll(viewConfig.styleSheets());
            }
            return true;
        }else{
            return false;
        }
    }

    private static void addAllDescendents(Parent parent, ArrayList<IncludeView> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            if (node instanceof IncludeView) {
                nodes.add((IncludeView) node);
            }
            if (node instanceof TabPane) {
                TabPane p = (TabPane) node;
                for (Tab tnode : p.getTabs()) {
                    if (tnode.getContent() instanceof Parent)
                        addAllDescendents((Parent) tnode.getContent(), nodes);
                }
            }
            if (node instanceof Parent)
                addAllDescendents((Parent) node, nodes);
        }
    }

    public void setState(String s, Object o) {
        this.state.add(s, o);
    }

    public void setState(String s, Runnable o) {
        this.state.add(s, o);
    }

    public void setState(String s, EventHandler<Event> o) {
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
        boolean fxController = false;
        try {
            InputStream is = FXView.class.getClassLoader().getResourceAsStream(url);
            byte[] buffer = new byte[2024];
            StringBuilder stringBuilder = new StringBuilder();
            int length = 0;

            while ((length = is.read(buffer)) >= 0) {
                stringBuilder.append(new String(Arrays.copyOfRange(buffer, 0, length), "UTF-8"));
            }

            String contents = stringBuilder.toString();
            if (contents.contains("fx:controller=")) fxController = true;
            if (contents.contains("IncludeView")) {
                lookup = true;
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
//		Map<String, Object> ns = getNameSpace();
//
//		ns.put("props", state);
        if (fxController) {
            loader.setControllerFactory(o -> {
                return this;
            });
        } else {
            loader.setController(this);
        }

        try {
            setRoot(loader.load());
            if (lookup) {
                System.out.println("Check for includes");
                for (IncludeView inc : getAllNodes((Parent) root)) {
                    inc.invokeInclude(state);

                }
            }

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

    public ObservableMap<String, Object> getNameSpace() {
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
     * </code>
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

    protected class Wrapper<T> {
        public T value;

        public Wrapper(T value) {
            this.value = value;
        }
    }

}
