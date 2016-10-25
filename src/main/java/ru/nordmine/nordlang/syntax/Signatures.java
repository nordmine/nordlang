package ru.nordmine.nordlang.syntax;

import ru.nordmine.nordlang.lexer.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Signatures {

    private final Map<String, MethodInfo> methods = new HashMap<>();

    public void putMethod(MethodInfo methodInfo) {
        methods.put(methodInfo.getName(), methodInfo);
    }

    public boolean containsMethodName(String name) {
        return methods.containsKey(name);
    }

    public Optional<MethodInfo> getMethodByParamList(String methodName, List<ParamInfo> params) {
        List<TypeToken> paramTypes = params.stream()
                .map(ParamInfo::getType)
                .collect(Collectors.toList());
        return getMethodByParamTypes(methodName, paramTypes);
    }

    public Optional<MethodInfo> getMethodByParamTypes(String methodName, List<TypeToken> paramTypes) {
        MethodInfo methodInfo = methods.get(methodName);
        if (methodInfo == null) {
            return Optional.empty();
        }
        List<ParamInfo> params = methodInfo.getParams();
        if (params.size() != paramTypes.size()) {
            return Optional.empty();
        }
        for (int i = 0; i < params.size() && i < paramTypes.size(); i++) {
            ParamInfo param = params.get(i);
            if (!param.getType().equals(paramTypes.get(i))) {
                return Optional.empty();
            }
        }
        return Optional.of(methodInfo);
    }
}
