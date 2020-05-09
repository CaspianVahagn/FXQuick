package fxQuick;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import fxQuick.FXFeign.FXFeignClient;
import fxQuick.FXFeign.FXFeignConfig;
import fxQuick.annotations.*;
import fxQuick.exeptions.AnnotationScanException;
import fxQuick.exeptions.FXViewException;
import javafx.application.Application;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Class<FXScan> annotationClass = FXScan.class;
        Class applicationClass =application.getClass();
        if(applicationClass.isAnnotationPresent(annotationClass)){
            FXScan fxScan = getAnnotation(annotationClass, applicationClass);
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

    private static <T> T getAnnotation(Class<T> annotationClass, Class applicationClass) {
        Annotation annotation = applicationClass.getAnnotation(annotationClass);
        return (T) annotation;
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
            initFeignClients(refs);
            System.out.println("----------- Init Feign Done");
            InitSerivesAndController(refs);
        }
        FieldAnnotationsScanner scanner = new FieldAnnotationsScanner();
        for (Object o : ServiceManager.CLASSES.values()) {
            if(!o.getClass().isInterface()) continue;
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

    private static void InitSerivesAndController(Reflections refs) {
        Set<Class<?>> annotatedClasses = refs.getTypesAnnotatedWith(FXController.class);
        annotatedClasses.addAll(refs.getTypesAnnotatedWith(FXService.class));
        List<Class<?>> listOfAnnotations = annotatedClasses.stream().collect(Collectors.toList());
        listOfAnnotations.sort((aClass, t1) -> {
                int a = Stream.of(aClass.getDeclaredConstructors()).findFirst().get().getParameterCount();
                int b = Stream.of(t1.getDeclaredConstructors()).findFirst().get().getParameterCount();
                return Integer.compare(a, b);
        });

        List<Class<?>> initLater = new ArrayList<>();
        InitialitializingWithConstructor(listOfAnnotations, initLater);
        int counter = 0;
        while(true){
            if(initLater.isEmpty()) break;
            System.out.println("Another round for: " + initLater);
            List<Class<?>> checklist = new ArrayList<>();
            InitialitializingWithConstructor(initLater,checklist);
            initLater = checklist;
            Collections.shuffle(initLater);
            counter ++;
            if(counter > 100){
                var classes = initLater.stream().map(aClass -> "\n" + aClass.getName() + "\n \t \\____With constructor parameters with types: "
                        + Stream.of(aClass.getDeclaredConstructors()).collect(Collectors.toList())).collect(Collectors.toList()) + " \n";
                throw new AnnotationScanException("Possible circular dependency Injection within Classes: \n" + classes );
            }
        }

    }

    private static void InitialitializingWithConstructor(List<Class<?>> listOfAnnotations, List<Class<?>> initLater) {
        for (Class<?> clazz : listOfAnnotations) {
            System.out.println("---> Prepare  " + clazz.getName());
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                Object o;
                try {
                    Constructor constructor;
                    if(clazz.getDeclaredConstructors().length == 1){
                        constructor = clazz.getDeclaredConstructors()[0];
                    }else {
                        constructor = Stream.of(clazz.getDeclaredConstructors()).filter(constructor1 -> {
                           return constructor1.isAnnotationPresent(FXInject.class);
                        }).findFirst().orElseThrow(() ->
                                new AnnotationScanException("Class :" + clazz.getName() + " hast multiple Constructors  can't find annotation @FXInject"));
                    }
                    Object[] initialized = new Object[constructor.getParameterCount()];
                    int i = 0;
                    boolean shouldInit = true;
                    for(Class<?> aClass: constructor.getParameterTypes()){
                        if(aClass.isAnnotationPresent(FXService.class) || aClass.isAnnotationPresent(FXController.class) || aClass.isAnnotationPresent(FXFeignClient.class)){
                            if(ServiceManager.CLASSES.containsKey(aClass)){
                                System.out.println("Instance found " + aClass.getName());
                                initialized[i] = ServiceManager.CLASSES.get(aClass);
                            }else{
                                initLater.add(clazz);
                                shouldInit = false;
                            }
                        }
                        i++;
                    }
                    System.out.println("WEEO : " + initialized.length + " expected:" + constructor.getParameterCount());
                    if(!shouldInit) continue;
                    o = constructor.newInstance(initialized);
                    ServiceManager.CLASSES.put(clazz, o);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private static void initFeignClients(Reflections refs) {
        Set<Class<?>> annotatedClients = refs.getTypesAnnotatedWith(FXFeignClient.class);
        for (Class<?> clazz : annotatedClients) {
            System.out.println("---> Client enabled " + clazz.getName());
            if (Modifier.isInterface(clazz.getModifiers())) {
                Object o;
                try {
                    FXFeignClient client = getAnnotation(FXFeignClient.class, clazz);

                    if(client.config() != FXFeignClient.class){
                        FXFeignConfig config =(FXFeignConfig) client.config().getDeclaredConstructor().newInstance();
                        Feign.Builder builder = Feign.builder()
                                .client(config.client())
                                .encoder(config.encoder())
                                .decoder(config.decoder());
                        if(config.requestInterceptor() !=null) {
                            builder.requestInterceptor(config.requestInterceptor());
                        }
                        if(config.requestInterceptors() !=null) {
                            builder.requestInterceptors(config.requestInterceptors());
                        }
                        if(config.logger() !=null) {
                            builder.logger(config.logger());
                        }
                        if(config.logLevel() !=null) {
                            builder.logLevel(config.logLevel());
                        }
                        if(config.exceptionPropagationPolicy() !=null) {
                            builder.exceptionPropagationPolicy(config.exceptionPropagationPolicy());
                        }
                        if(config.errorDecoder() !=null) {
                            builder.errorDecoder(config.errorDecoder());
                        }
                        if(config.contract() != null){
                            builder.contract(config.contract());
                        }
                        o = builder.target(clazz, client.baseUrl() + "/" + client.api());
                    }else {
                        o = Feign.builder()
                                .client(new OkHttpClient())
                                .encoder(new JacksonEncoder())
                                .decoder(new JacksonDecoder())
                                .target(clazz, client.baseUrl() + "/" + client.api());
                    }
                    ServiceManager.CLASSES.put(clazz, o);
                } catch (NoClassDefFoundError | NoSuchMethodException e ) {
                    e.printStackTrace();
                    throw new FXViewException("Can't initialize Feign client");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
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
