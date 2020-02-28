package com.richzjc.download.util;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldUtils {
    private static Map<Class, List<Field>> fields = new HashMap<>();

    public static void putFields(Class cls) {
        if (!fields.containsKey(cls)) {
            List<Field> fieldList = new ArrayList<>();
            fieldList.addAll(new ArrayList<>(Arrays.asList(cls.getDeclaredFields())));
            fields.put(cls, fieldList);
        }
    }

    public static List<Field> getFields(Class cls) {
        if (!fields.containsKey(cls)) {
            putFields(cls);
        }
        return fields.get(cls);
    }
}
