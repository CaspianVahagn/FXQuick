package fxQuick;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fxQuick.iViews.FXView;

public class ServiceManager {
	public final static Map<Class<?>, Object> CLASSES = new HashMap<>();
	
	public final static Map<Class<?>,Set<Field>> DEMANDED_FIELDS = new HashMap<>();
	
	public final static Map<String, FXView> viewIdMap = new HashMap<>();
	
	private static String[] packageQualifier;
	
	private static boolean isSet=false;

	public static String[] getPackageQualifier() {
		return packageQualifier;
	}

	public static void setPackageQualifier(String[] packageQualifier) {
		if(!isSet) {
			ServiceManager.packageQualifier = packageQualifier;
		}
		
	}
	
	
 
}
