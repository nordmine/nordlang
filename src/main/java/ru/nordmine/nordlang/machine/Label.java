package ru.nordmine.nordlang.machine;

public class Label {

    public static final Label EMPTY = new Label();

    private int position = -1;

    public int getPosition() {
        return position;
    }

    public void fix(int dstPosition) {
        this.position = dstPosition;
    }

    @Override
    public String toString() {
        return "-> " + position;
    }
}
