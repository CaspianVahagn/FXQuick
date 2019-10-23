package fxQuick.iconControl;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
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
public class IconLabel extends Label{
	
	private String icon;
	private String iconColor;
	
	private FontAwesomeIcon glyph = new FontAwesomeIcon();
	public String getIcon() {
		return icon;
		
	}

	public void setIcon(String iconName) {
		this.icon = iconName;
		if (iconName != null && iconName.length() > 1) {

			iconName = iconName.toUpperCase(); 

			glyph.setIcon(FontAwesomeIcons.valueOf(iconName));
			glyph.setFill(Color.WHITE);
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
	
	public FontAwesomeIcon getGlyph() {
		return glyph;
	}


	public String getIconColor() {
		return iconColor;
	}

	public void setIconColor(String c) {
		glyph.setFill(Color.valueOf(c));

	}
}
