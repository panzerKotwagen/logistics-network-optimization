package ru.kotb.lno.graph.algorithms;

/**
 * An abstract class describing the solution of a dynamic programming problem
 *
 * @param <S> state class
 * @param <C> control class
 */
public abstract class DynamicProgramming
        <S extends DynamicProgramming.AbstractState, C extends DynamicProgramming.AbstractControl> {

    /**
     * Calculates local winnings
     *
     * @param stage   stage number
     * @param state   specified state
     * @param control specified control for the state
     * @return local winnings in the form of a number
     */
    public abstract double w(Integer stage, S state, C control);

    /**
     * The function expressing a change in the state of the system under
     * the influence of control
     *
     * @param state   state
     * @param control control
     * @return new state
     */
    public abstract S phi(S state, C control);

    /**
     * The basic functional equation
     * @param stage
     * @param state
     * @return
     * @param <T>
     */
    public abstract <T> T W(Integer stage, S state);

    /**
     *
     * @return
     * @param <T>
     */
    public abstract <T> T solve();

    /**
     * An abstract class describing the state of the system
     */
    protected static abstract class AbstractState {

    }


    /**
     * An abstract class describing management in the system
     */
    protected static abstract class AbstractControl {

    }
}
