package de.application.iViews;

import java.lang.reflect.Field;

import FXQuick.ServiceManager;

/**
 * 
 * @author Dominik Reset
 *
 * Injects all FXServices on runtime when object is Initialized
 */
public class FXBase {
	
	public FXBase() {
		loadInjections();
	}
	
	private void loadInjections() {
		if (ServiceManager.DEMANDED_FIELDS.containsKey(this.getClass())) {
			for (Field f : ServiceManager.DEMANDED_FIELDS.get(this.getClass())) {
				try {
					f.setAccessible(true);

					f.set(this, ServiceManager.CLASSES.get(f.getType()));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();

				} catch (IllegalAccessException e) {
					e.printStackTrace();

				}
			}
		}
		

	}
}
