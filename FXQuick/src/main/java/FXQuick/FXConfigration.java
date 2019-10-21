package FXQuick;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
/**Configuration for Injection.
 * This Class will scan and prepare Injections for Given classes.
 * Each FXService will be a single Instance. 
 * @author Dominik Reset
 *
 */
public class FXConfigration {
	private static final String ASCIIART = "  ________   __                __ _                       _   _             \r\n"
			+ " |  ____\\ \\ / /               / _(_)                     | | (_)            \r\n"
			+ " | |__   \\ V / ___ ___  _ __ | |_ _  __ _ _   _ _ __ __ _| |_ _  ___  _ __  \r\n"
			+ " |  __|   > < / __/ _ \\| '_ \\|  _| |/ _` | | | | '__/ _` | __| |/ _ \\| '_ \\ \r\n"
			+ " | |     / . \\ (_| (_) | | | | | | | (_| | |_| | | | (_| | |_| | (_) | | | |\r\n"
			+ " |_|    /_/ \\_\\___\\___/|_| |_|_| |_|\\__, |\\__,_|_|  \\__,_|\\__|_|\\___/|_| |_|\r\n"
			+ "                                     __/ |                                  \r\n"
			+ "                                    |___/                                   ";

	public static final String version =
			  "\t\t\t _   _               _               __   _____ \r\n"
			+ "\t\t\t| | | |             (_)             /  | |  _  |\r\n"
			+ "\t\t\t| | | | ___ _ __ ___ _  ___  _ __   `| | | |/' |\r\n"
			+ "\t\t\t| | | |/ _ \\ '__/ __| |/ _ \\| '_ \\   | | |  /| |\r\n"
			+ "\t\t\t\\ \\_/ /  __/ |  \\__ \\ | (_) | | | | _| |_\\ |_/ /\r\n"
			+ "\t\t\t \\___/ \\___|_|  |___/_|\\___/|_| |_| \\___(_)___/";
	private static String[] packageNames;
	private static String[] potentialInjections;

	public static void scanServices(String... packageNames) {
		FXConfigration.packageNames = packageNames;
	}
	/**
	 * provide packageNames for objects wich need injections on Runtime 
	 * @param packageNames
	 */
	public static void scanRuntimeInjections(String... packageNames) {

		FXConfigration.potentialInjections = packageNames;
	}
	/**
	 * provide Packags with services to inject 
	 * @param packageNames
	 */
	private static void applyScanServices(String... packageNames) {
		FXConfigration.packageNames = packageNames;

		System.out.println("------------------- Initialize FXServices -----------------");
		
		for (String packageName : packageNames) {
			Reflections refs = (new Reflections(packageName));
			Set<Class<?>>annotatedClasses = refs.getTypesAnnotatedWith(FXController.class);
			annotatedClasses.addAll(refs.getTypesAnnotatedWith(FXService.class));
			for (Class<?> clazz : annotatedClasses ) {
				System.out.println("---> " + clazz.getName());
				if (!Modifier.isAbstract(clazz.getModifiers())) {
					Object o;
					try {
						o = clazz.newInstance();
						ServiceManager.CLASSES.put(clazz, o);
					} catch (InstantiationException | IllegalAccessException e) {
						
						e.printStackTrace();
					}

				}
			}
		}
		FieldAnnotationsScanner scanner = new FieldAnnotationsScanner();
		for (Object o : ServiceManager.CLASSES.values()) {
			Set<Field> fields = (new Reflections(o.getClass(), scanner).getFieldsAnnotatedWith(FXInject.class));
			for (Field field : fields) {
				if (field.getDeclaringClass().equals(o.getClass())) {
					field.setAccessible(true);
					try {
						field.set(o, ServiceManager.CLASSES.get(field.getType()));
					} catch (IllegalArgumentException e) {

						e.printStackTrace();
					} catch (IllegalAccessException e) {

						e.printStackTrace();
					}
				}

			}
		}
		System.out.println("------------------- FXServices initialized -----------------\n");
	}

	private static void applyScanRuntimeInjections(String... packageNames) {
		System.out.println("------------------- Prepare FXInjections -------------------");
		FieldAnnotationsScanner scanner = new FieldAnnotationsScanner();

		for (String packageName : packageNames) {
			Set<Field> fields = (new Reflections(packageName, scanner).getFieldsAnnotatedWith(FXInject.class));
			for (Field field : fields) {
				if (ServiceManager.DEMANDED_FIELDS.containsKey(field.getDeclaringClass())) {
					ServiceManager.DEMANDED_FIELDS.get(field.getDeclaringClass()).add(field);
				} else {
					Set<Field> fieldset = new HashSet<>();
					fieldset.add(field);
					System.out.println("[prepare for]---> " + field.getDeclaringClass().getName());
					ServiceManager.DEMANDED_FIELDS.put(field.getDeclaringClass(), fieldset);
				}

			}
		}
		System.out.println("------------------- FXInjections prepared -----------------");

	}
	/**
	 * Scans all the given Packages and will prepare Injections 
	 * and generates Services with Injections
	 */
	public static void apply() {
		System.out.println(ASCIIART);
		System.out.println("================================================================== Version 1.0");
		System.out.println();
		long t = System.currentTimeMillis();
		applyScanServices(packageNames);
		applyScanRuntimeInjections(potentialInjections);
	}

}
