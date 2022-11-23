package com.orapple.code.generator.content;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Orapple Wu
 * @Time: 2022/11/23
 * @Description: information of this type！
 */
public final class CodeTemplateContext {

    private CodeTemplateContext() {
    }

    public static ThreadLocal<ConcurrentHashMap<String, String>> context = new ThreadLocal<>();

    public static void setVal(String key, String value) {
        if (context.get() == null) {
            context.set(new ConcurrentHashMap<String, String>());
        }
        context.get().put(key, value);
    }

    public static String getVal(String key) {
        if (context.get() == null) {
            context.set(new ConcurrentHashMap<String, String>());
        }
        return context.get().get(key);
    }
}
