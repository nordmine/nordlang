package ru.nordmine.nordlang.syntax;

import java.util.HashMap;
import java.util.Map;

public class Signatures {

    private final Map<String, MethodInfo> methods = new HashMap<>();

    public void putMethod(MethodInfo methodInfo) {
        methods.put(methodInfo.getName(), methodInfo);
    }

    public boolean contains(String name) {
        return methods.containsKey(name);
    }

    public MethodInfo getMethodByName(String methodName) {
        return methods.get(methodName);
    }

    public MethodInfo getMainMethod() {
        return getMethodByName("main");
    }
}
