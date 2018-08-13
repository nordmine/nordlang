package ru.nordmine.nordlang.syntax;

import ru.nordmine.nordlang.lexer.TypeToken;
import ru.nordmine.nordlang.lexer.WordToken;
import ru.nordmine.nordlang.machine.Label;

import java.util.ArrayList;
import java.util.List;

public class MethodInfo {

    private final TypeToken returnType;
    private final WordToken methodNameToken;
    private final List<ParamInfo> params = new ArrayList<>();
    private final Label beginLabel = new Label();

    private boolean hasReturnStatement = false;

    public MethodInfo(TypeToken returnType, WordToken methodNameToken) {
        this.returnType = returnType;
        this.methodNameToken = methodNameToken;
    }

    public void addParam(TypeToken type, WordToken id) {
        this.params.add(new ParamInfo(type, id));
    }

    public TypeToken getReturnType() {
        return returnType;
    }

    public WordToken getMethodNameToken() {
        return methodNameToken;
    }

    public String getName() {
        return methodNameToken.getLexeme();
    }

    public List<ParamInfo> getParams() {
        return params;
    }

    public Label getBeginLabel() {
        return beginLabel;
    }

    public boolean hasReturnStatement() {
        return hasReturnStatement;
    }

    public void setHasReturnStatement(boolean hasReturnStatement) {
        this.hasReturnStatement = hasReturnStatement;
    }

    @Override
    public String toString() {
        return returnType + " " + methodNameToken.getLexeme() + "(" + (params.isEmpty() ? "" : params) + ")";
    }
}
