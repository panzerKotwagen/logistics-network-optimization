package ru.kotb.lno.graph.algorithms;

/**
 * An abstract class describing the solution of a dynamic programming problem
 *
 * @param <S> state class
 * @param <C> control class
 */
public abstract class DynamicProgramming
        <S extends DynamicProgramming.AbstractState, C extends DynamicProgramming.AbstractControl, W, R> {

    /**
     * Calculates local winnings
     *
     * @param stage   stage number
     * @param state   specified state
     * @param control specified control for the state
     * @return local winnings in the form of a number
     */
    public abstract W w(Integer stage, S state, C control);

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
     * The conditional optimal winnings at all steps from the
     * {@code stage} to the last
     *
     * @param stage stage number
     * @param state state
     * @return result
     */
    public abstract R W(Integer stage, S state);

    /**
     * Solves the problem
     *
     * @return result
     */
    public abstract R solve();

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
