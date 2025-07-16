package org.diegodamian.ocp17.book.ch7.interfaces;

public abstract interface CanBurrow {
    public abstract Float getSpeed(int age);

    public static final int MINIUM_DEPTH = 1;
}