package nordlang.machine;

import nordlang.exceptions.RunException;

import java.util.HashMap;
import java.util.Map;

public class Scope {

    private Scope parent;
    private Map<String, int[]> values;

    public Scope(Scope parent) {
        this.parent = parent;
        this.values = new HashMap<>();
    }

    public Scope getParent() {
        return parent;
    }

    public void defineInCurrentScope(String name, int width) throws RunException {
        if (values.containsKey(name)) {
            throw new RunException(String.format("variable %s already defined in current scope", name));
        }
        values.put(name, new int[width]);
    }

    public void set(String name, int offset, int val) throws RunException {
        int[] value = getValue(name);
        value[offset] = val;
    }

    public Integer get(String name, int offset) throws RunException {
        return getValue(name)[offset];
    }

    private int[] getValue(String name) throws RunException {
        for (Scope scope = this; scope != null; scope = scope.parent) {
            int[] found = scope.values.get(name);
            if (found != null) {
                return found;
            }
        }
        throw new RunException(String.format("variable %s is not defined", name));
    }
}
