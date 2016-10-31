package ru.nordmine.nordlang.syntax;

import ru.nordmine.nordlang.lexer.TypeToken;
import ru.nordmine.nordlang.lexer.WordToken;
import ru.nordmine.nordlang.machine.Label;

import java.util.ArrayList;
import java.util.List;

public class MethodInfo {

    private final TypeToken returnType;
    private final String name;
    private final List<ParamInfo> params = new ArrayList<>();
    private final Label beginLabel = new Label();

    public MethodInfo(TypeToken returnType, String name) {
        this.returnType = returnType;
        this.name = name;
    }

    public void addParam(TypeToken type, WordToken id) {
        this.params.add(new ParamInfo(type, id));
    }

    public TypeToken getReturnType() {
        return returnType;
    }

    public String getName() {
        return name;
    }

    public List<ParamInfo> getParams() {
        return params;
    }

    public Label getBeginLabel() {
        return beginLabel;
    }

    @Override
    public String toString() {
        return returnType + " " + name + "(" + params + ")";
    }
}
