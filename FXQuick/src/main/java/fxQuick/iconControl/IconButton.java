package fxQuick.iconControl;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.scene.control.Button;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;
/**
 * 
 * @author Dominik Reset
 *  
 *  Generates a Label With a icon
 *  by FontAwesomeIcon Jens Deters
 */
public class IconButton extends Button {

	private String icon;
	private String iconColor;
	private String glyphStyleClass;
	private FontAwesomeIcon glyph = new FontAwesomeIcon();

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String iconName) {
		this.icon = iconName;
		if (iconName != null && iconName.length() > 1) {

			iconName = iconName.toUpperCase();

			
			if(iconColor!=null) {
				glyph.setFill(Color.valueOf(iconColor));
			}else {
				glyph.setFill(Color.WHITE);
			}
			glyph.setIcon(FontAwesomeIcons.valueOf(iconName));
			this.setGraphic(glyph);
		}

//		FadeTransition transition = new FadeTransition(Duration.millis(100), this);
//		transition.setFromValue(1);
//		transition.setToValue(0);
//		transition.setAutoReverse(true);
//		transition.setCycleCount(2);
//
//		addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
//			System.out.println("schisdfhseuirz34897");
//			transition.playFromStart();
//		});
	}

	public void setIconColor(String c) {
		glyph.setFill(Color.valueOf(c));

	}
	public FontAwesomeIcon getGlyph() {
		return glyph;
	}

	public String getIconColor() {
		return iconColor;
	}

	public String getGlyphStyleClass() {
		return glyphStyleClass;
	}

	public void setGlyphStyleClass(String glyphStyleClass) {
		this.glyphStyleClass = glyphStyleClass;
		this.getGlyph().getStyleClass().add(glyphStyleClass);
	}

}
