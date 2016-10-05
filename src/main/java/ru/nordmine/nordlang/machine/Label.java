package ru.nordmine.nordlang.machine;

public class Label {

    private int dstPosition = -1;

    public int getDstPosition() {
        return dstPosition;
    }

    public void setDstPosition(int dstPosition) {
        this.dstPosition = dstPosition;
    }

    @Override
    public String toString() {
        return "-> " + dstPosition;
    }
}
