package fxQuick;

import javafx.scene.text.Font;

public class FontInitializer {
	
	private static boolean initalized = false;
	
	
	public static void init() {
		Font.loadFont(FontInitializer.class.getClassLoader().getResourceAsStream("materialReg.ttf"), 16);
		Font.loadFont(FontInitializer.class.getClassLoader().getResourceAsStream("matOutline.ttf"), 16);
		setInitalized(true);
	}


	public static boolean isInitalized() {
		return initalized;
	}


	private static void setInitalized(boolean initalized) {
		FontInitializer.initalized = initalized;
	}

}
