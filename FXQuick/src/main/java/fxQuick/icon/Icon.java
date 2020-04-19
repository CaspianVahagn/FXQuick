package fxQuick.icon;

import fxQuick.FontInitializer;
import fxQuick.MatIcons;
import javafx.scene.text.Text;

public class Icon extends Text {

    public static final String REGULAR = "Material Icons";
    public static final String OUTLINED = "Material Icons Outlined";
    private double size = 2;
    private String iconType = REGULAR;

    private String iconName = "";
    private String unit = "em";

    public Icon() {
        super();
        if (!FontInitializer.isInitalized()) {
            FontInitializer.init();
        }
        applyStyle();
    }

    public Icon(String iconName) {
        this();
        setIconName(iconName);
        applyStyle();
    }

    public Icon(String iconName, double size, String unit) {
        this();
        setIconName(iconName);
        this.unit = unit;
        this.size = size;
        applyStyle();
    }

    private void applyStyle() {
        String style = String.format("-fx-font-family: '%s'; -fx-font-size: %s;", iconType, size + unit);

        this.setStyle(style);
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getIconType() {
        return iconType;
    }

    public void setIconType(String iconType) {

        this.iconType = iconType;
        applyStyle();
    }

    public String getIconName() {
        return iconName;

    }

    public void setIconName(String name) {

        if (name.contains("ol:")) {
            setIconType(OUTLINED);
            name = name.split(":")[1];

        }

        this.iconName = MatIcons.valueOf(name.toUpperCase()).getUniCode();
        setText(this.iconName);
        applyStyle();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
        applyStyle();
    }

    public void addStyle(String style) {
        String myStyle = String.format("-fx-font-family: '%s'; -fx-font-size: %s;", iconType, size + unit);
        this.setStyle(myStyle + style);
    }

}
