package ru.kotb.lno.graph.algorithms;

public abstract class DynamicProgramming<S extends DynamicProgramming.AbstractState, C extends DynamicProgramming.AbstractControl> {

    public abstract double w(Integer stage, S state, C control);

    public abstract S phi(S state, C control);

    public abstract <T> T W(Integer stage, S state);

    public abstract <T> T solve(S startState);

    public static abstract class AbstractState {

    }


    public static abstract class AbstractControl {

    }
}
