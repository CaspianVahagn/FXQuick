package fxQuick;

import fxQuick.annotations.FXController;
import fxQuick.annotations.FXInject;
import fxQuick.annotations.FXScan;
import fxQuick.annotations.FXService;
import fxQuick.exeptions.AnnotationScanException;
import javafx.application.Application;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Configuration for Injection.
 * This Class will scan and prepare Injections for Given classes.
 * Each FXService will be a single Instance.
 *
 * @author Dominik Leipelt
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


    private static String[] packageNames;
    private static String[] potentialInjections;
    private static String[] viewPackages;
    private static Application app;
    private static boolean dev = false;

    public boolean isDev() {
        return dev;
    }

    protected static Application getApp() {
        return app;
    }

    public static void init(Application application){
        app = application;
        Class annotationClass = FXScan.class;
        Class applicationClass =application.getClass();
        if(applicationClass.isAnnotationPresent(annotationClass)){
            Annotation annotation = applicationClass.getAnnotation(annotationClass);
            FXScan fxScan = (FXScan) annotation;
            scanServices(fxScan.rootPackages());
            scanRuntimeInjections(fxScan.rootPackages());
            dev = fxScan.dev();
            String[] viewPackages = List.of(Package.getPackages())
                    .stream()
                    .filter(aPackage -> {
                for (int i = 0; i < fxScan.rootPackages().length;i++){
                    if(aPackage.getName().contains(fxScan.rootPackages()[i])){
                        return true;
                    }
                }
                return false;
            })
                    .map(aPackage -> aPackage.getName())
                    .toArray(String[]::new);
            addViewNameSpaces(viewPackages);
            apply();
        }else{
            throw new AnnotationScanException("Application class, has incomplete or no Anntotation present");
        }
    }

    private static void scanServices(String... packageNames) {
        FXConfigration.packageNames = packageNames;
    }

    /**
     * provide packageNames for objects wich need injections on Runtime
     *
     * @param packageNames
     */
    private static void scanRuntimeInjections(String... packageNames) {

        FXConfigration.potentialInjections = packageNames;
    }

    private static void addViewNameSpaces(String... packageNames) {
        FXConfigration.viewPackages = packageNames;
    }

    /**
     * provide Packags with services to inject
     *
     * @param packageNames
     */
    private static void applyScanServices(String... packageNames) {
        FXConfigration.packageNames = packageNames;

        System.out.println("------------------- Initialize FXServices -----------------");

        for (String packageName : packageNames) {
            Reflections refs = (new Reflections(packageName));
            Set<Class<?>> annotatedClasses = refs.getTypesAnnotatedWith(FXController.class);
            annotatedClasses.addAll(refs.getTypesAnnotatedWith(FXService.class));
            for (Class<?> clazz : annotatedClasses) {
                System.out.println("---> " + clazz.getName());
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    Object o;
                    try {
                        o = clazz.getDeclaredConstructor().newInstance();
                        ServiceManager.CLASSES.put(clazz, o);
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
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
        System.out.println("================================================================== Version 1.1");
        System.out.println();
        ServiceManager.setPackageQualifier(viewPackages);
        applyScanServices(packageNames);
        applyScanRuntimeInjections(potentialInjections);
    }

}
