package nordlang.machine;

import nordlang.exceptions.RunException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineScope {

    private MachineScope parent;
    private Map<Integer, List<Integer>> values;

    public MachineScope(MachineScope parent) {
        this.parent = parent;
        this.values = new HashMap<>();
    }

    public MachineScope getParent() {
        return parent;
    }

    public void defineInCurrentScope(int nameIndex, int width) throws RunException {
        if (values.containsKey(nameIndex)) {
            throw new RunException("variable already defined in current scope");
        }
        List<Integer> initialValue = new ArrayList<>(width);
        for (int i = 0; i < width; i++) {
            initialValue.add(0);
        }
        values.put(nameIndex, initialValue);
    }

    public void set(int nameIndex, int offset, int val) throws RunException {
        List<Integer> value = getValue(nameIndex);
        if (offset >= 0 && offset < value.size()) {
            value.set(offset, val);
        } else {
            throw new RunException("Out of bound offset");
        }
    }

    public int get(int nameIndex, int offset) throws RunException {
        List<Integer> value = getValue(nameIndex);
        if (offset >= 0 && offset < value.size()) {
            return value.get(offset);
        } else {
            throw new RunException("Out of bound offset");
        }
    }

    public int sizeOf(int nameIndex) throws RunException {
        return getValue(nameIndex).size();
    }

    private List<Integer> getValue(int nameIndex) throws RunException {
        for (MachineScope scope = this; scope != null; scope = scope.parent) {
            List<Integer> found = scope.values.get(nameIndex);
            if (found != null) {
                return found;
            }
        }
        throw new RunException("variable is not defined");
    }
}
