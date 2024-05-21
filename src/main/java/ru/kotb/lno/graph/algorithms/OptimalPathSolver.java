package ru.kotb.lno.graph.algorithms;

import lombok.AllArgsConstructor;
import ru.kotb.lno.graph.Graph;
import ru.kotb.lno.graph.components.Edge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * A class for solving the problem of finding the optimal path in
 * a graph with two criteria
 */
public class OptimalPathSolver
        extends DynamicProgramming<OptimalPathSolver.State, OptimalPathSolver.Control> {

    /**
     * The number of stages in the task. It ss equal to the number of
     * edges in the optimal path
     */
    private Integer stageCount;

    /**
     * The starting state. Corresponds to the vertex from which the
     * path is being searched
     */
    private State startState;

    /**
     * The set of the states. Corresponds to the vertices in the graph
     */
    private final Set<State> stateSet = new HashSet<>();

    /**
     * Corresponds a set of possible controls to each state.
     * Control - an arc adjacent to the vertex
     */
    private final Map<State, Set<Control>> stateControlSetMap = new HashMap<>();

    /**
     * The shortest route according to one of the criteria for
     * comparison at the algorithm step
     */
    private final Map<Integer, Weights> referenceStateControlSetMap = new HashMap<>();

    /**
     * Memorization of calculated values
     */
    private final Map<Integer, Map<State, WinningAndControl>> cacheW = new HashMap<>();

    /**
     * Initializes a variety of states, controls, etc.
     *
     * @param graph         the graph in which you want to find the
     *                      optimal route
     * @param referencePath The shortest route according to one of the
     *                      criteria for comparison at the algorithm step
     */
    public void init(Graph graph, List<String> referencePath) {
        stageCount = referencePath.size() - 1;

        for (String node : graph.nodeNamesSet()) {
            State state = new State(node);
            stateSet.add(state);

            //Form a set of possible controls for this condition
            Set<Control> controlSet = new HashSet<>();
            for (Edge edge : graph.getEdges(node)) {
                State targetState = new State(graph.getOppositeNode(edge, node));
                if (stateControlSetMap.containsKey(targetState)
                        || stateControlSetMap.containsKey(state)) {
                    continue;
                }

                Control control = new Control(
                        state,
                        targetState,
                        edge.getWeights()[0],
                        edge.getWeights()[1]
                );

                controlSet.add(control);
            }

            stateControlSetMap.put(state, controlSet);
        }

        startState = new State(referencePath.get(0));
        for (int i = 1; i < referencePath.size(); i++) {
            Edge edge = graph.getEdge(referencePath.get(i - 1), referencePath.get(i));
            Weights weights = new Weights(edge.getWeights()[0], edge.getWeights()[1]);
            referenceStateControlSetMap.put(i - 1, weights);
        }
    }

    @Override
    public Winnings w(Integer stage, State state, Control control) {
        double referenceW1 = referenceStateControlSetMap.get(stage).w1;
        double referenceW2 = referenceStateControlSetMap.get(stage).w2;

        double controlW1 = control.w1;
        double controlW2 = control.w2;

        double diff = referenceW1 - controlW1 + referenceW2 - controlW2;

        return new Winnings(diff, controlW1, controlW2);
    }

    @Override
    public State phi(State state, Control control) {
        if (stateControlSetMap.get(state).contains(control)) {
            return control.target;
        }
        throw new RuntimeException("The transmitted state does not have the specified control");
    }

    @Override
    public List<String> solve() {
        WinningAndControl winningAndControl = W(0, startState);
        return restoreOptimalPath();
    }

    @Override
    public WinningAndControl W(Integer stage, State state) {
        if (cacheW.containsKey(stage)) {
            if (cacheW.get(stage).containsKey(state)) {
                return cacheW.get(stage).get(state);
            }
        }

        Winnings bestW = null;
        Control bestControl = null;

        for (Control control : stateControlSetMap.get(state)) {
            Winnings wi = w(stage, state, control);
            State nextState = phi(state, control);

            Winnings Wi;
            if (stage == stageCount - 1) {
                Wi = wi;
            } else {
                Wi = wi.add(W(stage + 1, nextState).winnings);
            }

            if (bestW == null || Wi.compareTo(bestW) > 0) {
                bestW = Wi;
                bestControl = control;
            }
        }

        WinningAndControl res = new WinningAndControl(bestW, bestControl);
        if (!cacheW.containsKey(stage)) {
            cacheW.put(stage, new HashMap<>());
        }
        cacheW.get(stage).put(state, res);
        return res;
    }

    /**
     * Restores the optimal route
     *
     * @return the route in the form of a sequence of vertices names
     */
    private List<String> restoreOptimalPath() {
        List<String> optimalPath = new ArrayList<>();

        State current = startState;
        optimalPath.add(startState.nodeName);
        for (int i = 0; i < stageCount; i++) {
            WinningAndControl res = W(i, current);
            optimalPath.add(res.control.target.nodeName);
            Control control = res.control;
            current = phi(current, control);
        }

        return optimalPath;
    }

    @AllArgsConstructor
    public static class Winnings implements Comparable<Winnings> {

        Double difference;

        Double w1;

        Double w2;

        public Winnings add(Winnings o) {
            return new Winnings(
                    this.difference + o.difference,
                    this.w1 + o.w1,
                    this.w2 + o.w2
            );
        }

        @Override
        public int compareTo(Winnings o) {
            int res = Double.compare(this.difference, o.difference);
            if (res != 0) {
                return res;
            }

            if (this.w1 < o.w1) {
                return 1;
            }

            return 0;
        }
    }


    private static class Weights {

        double w1;

        double w2;

        public Weights(double... weights) {
            w1 = weights[0];
            w2 = weights[1];
        }
    }


    @AllArgsConstructor
    public static class WinningAndControl {

        Winnings winnings;

        Control control;
    }


    @AllArgsConstructor
    public static class State extends AbstractState {

        private String nodeName;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof State state))
                return false;
            return nodeName.equals(state.nodeName);
        }

        @Override
        public int hashCode() {
            return nodeName.hashCode();
        }
    }


    @AllArgsConstructor
    public static class Control extends AbstractControl {

        private State source;

        private State target;

        private double w1;

        private double w2;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Control control)) return false;
            return Double.compare(w1, control.w1) == 0
                    && Double.compare(w2, control.w2) == 0
                    && source.equals(control.source)
                    && target.equals(control.target);
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, target, w1, w2);
        }
    }
}
