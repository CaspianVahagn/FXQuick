package fxQuick.iconControl;


import fxQuick.icon.Icon;
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
	private Icon glyph = new Icon();

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String iconName) {
		this.icon = iconName;
		if (iconName != null && iconName.length() > 1) {

			

			
			if(iconColor!=null) {
				glyph.setFill(Color.valueOf(iconColor));
			}else {
				glyph.setFill(Color.WHITE);
			}
			glyph.setIconName(iconName);
			this.setGraphic(glyph);
		}


	}

	public void setIconColor(String c) {
		glyph.setFill(Color.valueOf(c));

	}
	public Icon getGlyph() {
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
