package net.mauhiz.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public enum Messages {
    ;
    public static String get(Class<?> caller, String key, Object... params) {
        return get(caller.getPackage(), key, params);
    }

    public static String get(Package caller, String key, Object... params) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(caller.getName());
            String value = bundle.getString(key);
            if (params == null) {
                return value;
            }
            return MessageFormat.format(value, params);
        } catch (MissingResourceException mre) {
            Logger.getLogger(Messages.class).error("Bundle not found: " + caller.getName());
            return "???";
        }
    }
}
