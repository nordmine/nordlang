package ru.nordmine.nordlang.lexer.types;

import ru.nordmine.nordlang.lexer.Tag;
import ru.nordmine.nordlang.machine.Program;

public class CharArray extends ValueToken {

    private final StringBuilder value;

    public CharArray(StringBuilder value) {
        super(Tag.STRING);
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public StringBuilder getValue() {
        return value;
    }

    @Override
    public void gen(Program program) {
        // todo массивы символов, известные при компиляции, должны быть определены в памяти и иметь своё внутренее имя-индекс
        for(int i = 0; i < value.length(); i++) {
            program.addPushCommand(value.charAt(i));
        }
    }
}
