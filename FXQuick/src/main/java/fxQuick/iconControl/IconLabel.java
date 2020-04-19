package fxQuick.iconControl;

import fxQuick.icon.Icon;
import javafx.beans.property.DoubleProperty;
import javafx.css.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Dominik Reset
 * <p>
 * Generates a Label With a icon by FontAwesomeIcon Jens Deters
 */
public class IconLabel extends Label {

    private static final CssMetaData GAP_META_DATA =
            new CssMetaData<IconLabel, Number>("-my-gap", StyleConverter.getSizeConverter(), 0d) {

                @Override
                public boolean isSettable(IconLabel node) {
                    return node.gapProperty == null || !node.gapProperty.isBound();
                }

                @Override
                public StyleableProperty<Number> getStyleableProperty(IconLabel node) {

                    return (StyleableProperty<Number>) node.gapProperty;
                }
            };
    private static final List<CssMetaData<? extends Styleable, ?>> cssMetaDataList;

    static {

        List<CssMetaData<? extends Styleable, ?>> temp =
                new ArrayList<CssMetaData<? extends Styleable, ?>>(Label.getClassCssMetaData());

        temp.add(GAP_META_DATA);
        cssMetaDataList = Collections.unmodifiableList(temp);

    }

    private String icon;
    private String iconColor;
    private String glyphStyleClass;
    private Icon glyph = new Icon();
    /**
     * ---- Style ---
     */

    private DoubleProperty gapProperty = new StyleableDoubleProperty(0) {
        @Override
        public CssMetaData<IconLabel, Number> getCssMetaData() {
            return GAP_META_DATA;
        }

        @Override
        public Object getBean() {
            System.out.println("Asking");
            return IconLabel.this;
        }

        @Override
        public String getName() {
            return "gap";
        }
    };

    public IconLabel() {


    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {


        return cssMetaDataList;
    }

    public String getIcon() {
        return icon;

    }

    public void setIcon(String iconName) {
        this.icon = iconName;

        if (iconName != null && iconName.length() > 1) {

            if (iconColor != null) {
                glyph.setFill(Color.valueOf(iconColor));
            } else {
                glyph.setFill(Color.WHITE);
            }
            glyph.setIconName(iconName);

            this.setGraphic(glyph);
        }


//		FadeTransition transition = new FadeTransition(Duration.millis(100), this);
//		transition.setFromValue(1);
//		transition.setToValue(0);
//		transition.setAutoReverse(true);
//		transition.setCycleCount(2);
//
//		addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
//
//			transition.playFromStart();
//		});

    }

    public Icon getGlyph() {
        return glyph;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String c) {
        glyph.setFill(Color.valueOf(c));

    }

    public String getGlyphStyleClass() {
        return glyphStyleClass;
    }

    public void setGlyphStyleClass(String glyphStyleClass) {
        this.glyphStyleClass = glyphStyleClass;
        this.getGlyph().getStyleClass().add(glyphStyleClass);

    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {

        return getClassCssMetaData();
    }


}
