package fxQuick;

import fxQuick.iViews.FXView;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ServiceManager {
    public final static Map<Class<?>, Object> CLASSES = new HashMap<>();

    public final static Map<Class<?>, Set<Field>> DEMANDED_FIELDS = new HashMap<>();

    public final static Map<String, FXView> viewIdMap = new HashMap<>();

    public static String[] packageQualifier;

    public static boolean isSet = false;

    public static String[] getPackageQualifier() {
        return packageQualifier;
    }

    protected static void setPackageQualifier(String[] packageQualifier) {
        if (!isSet) {
            ServiceManager.packageQualifier = packageQualifier;
        }

    }


}
