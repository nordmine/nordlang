package nordlang.exceptions;

public class ParserException extends LangException {

    private int line = 0;

    public ParserException(String message) {
        super(message);
    }

    public ParserException(int line, String message) {
        super(message);
        this.line = line;
    }

    @Override
    public String getMessage() {
        return String.format("Syntax error at line %s: %s", line, super.getMessage());
    }
}
