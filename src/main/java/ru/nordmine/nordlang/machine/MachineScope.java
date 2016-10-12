package ru.nordmine.nordlang.machine;

import ru.nordmine.nordlang.machine.exceptions.RunException;
import ru.nordmine.nordlang.machine.value.Value;

import java.util.HashMap;
import java.util.Map;

public class MachineScope {

    private MachineScope parent;
    private Map<Integer, Value> values;

    public MachineScope(MachineScope parent) {
        this.parent = parent;
        this.values = new HashMap<>();
    }

    public MachineScope getParent() {
        return parent;
    }

    public void defineInCurrentScope(int nameIndex, Value value) throws RunException {
        MachineScope scope = getScopeByNameIndex(nameIndex);
        if (scope != null) {
            throw new RunException("variable already defined in current scope");
        }
        values.put(nameIndex, value);
    }

    public void set(int nameIndex, Value newValue) throws RunException {
        MachineScope scope = getScopeByNameIndex(nameIndex);
        if (scope == null) {
            throw new RunException("variable is not defined");
        } else {
            scope.values.put(nameIndex, newValue);
        }
    }

    public Value get(int nameIndex) throws RunException {
        MachineScope scope = getScopeByNameIndex(nameIndex);
        if (scope == null) {
            throw new RunException("variable is not defined");
        } else {
            return scope.values.get(nameIndex);
        }
    }

    private MachineScope getScopeByNameIndex(int nameIndex) {
        for (MachineScope scope = this; scope != null; scope = scope.parent) {
            Value found = scope.values.get(nameIndex);
            if (found != null) {
                return scope;
            }
        }
        return null;
    }
}
