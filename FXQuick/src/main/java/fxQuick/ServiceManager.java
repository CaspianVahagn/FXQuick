package fxQuick;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServiceManager {
	public final static Map<Class<?>, Object> CLASSES = new HashMap<>();
	
	public final static Map<Class<?>,Set<Field>> DEMANDED_FIELDS = new HashMap<>();
	
	
 
}
