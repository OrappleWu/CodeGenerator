package com.orapple.code.generator.utils;

/**
 * @Author: Orapple Wu
 * @Time: 2022/11/23
 * @Description: information of this type！
 */
public final class CodeUtils {

    private CodeUtils(){}

    public static String transferToJavaField(String columnName) {
        String[] names = columnName.split("_");
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < names.length; i++) {
            if (i == 0) {
                name.append(names[i]);
            } else {
                name.append(names[i].substring(0, 1).toUpperCase()).append(names[i].substring(1));
            }
        }
        return name.toString();
    }

    public static String transferToJavaClassName(String tableName) {
        final String className = transferToJavaField(tableName);
        return className.substring(0, 1).toUpperCase() + className.substring(1);
    }
}
