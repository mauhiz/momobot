package net.mauhiz.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class Messages {
    public static String get(Class<?> caller, String key, Object... params) {
        return get(caller.getPackage(), key, params);
    }
    
    public static String get(Package caller, String key, Object... params) {
        ResourceBundle bundle = ResourceBundle.getBundle(caller.getName());
        String value = bundle.getString(key);
        if (params == null) {
            return value;
        }
        return MessageFormat.format(value, params);
    }
}
