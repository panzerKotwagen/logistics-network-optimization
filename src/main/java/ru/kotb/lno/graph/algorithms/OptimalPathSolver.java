package ru.kotb.lno.graph.algorithms;

import lombok.AllArgsConstructor;
import lombok.Getter;
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

    private final DijkstraShortestPath dijkstra = new DijkstraShortestPath();

    /**
     * A sheet with the maximum values of concessions at each step
     */
    private List<Double> compromiseList = List.of(0d, 0d, 0d, 0d);

    /**
     *
     */
    private boolean isFirstCriteriaIsMoreImportant = true;

    /**
     * The set of the states. Corresponds to the vertices in the graph
     */
    private final Set<State> stateSet = new HashSet<>();

    /**
     * Corresponds a set of possible controls to each state.
     * Control - an arc adjacent to the node
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
     * The number of stages in the task. It ss equal to the number of
     * edges in the optimal path
     */
    private Integer stageCount;

    /**
     * The starting state. Corresponds to the node from which the
     * path is being searched
     */
    private State startState;

    /**
     * Initializes a variety of states, controls, etc.
     *
     * @param graph the graph in which you want to find the
     *              optimal route
     */
    public void init(Graph graph, List<Double> compromiseList,
                     boolean isFirstCriteriaIsMoreImportant) {

        this.compromiseList = compromiseList;
        this.isFirstCriteriaIsMoreImportant = isFirstCriteriaIsMoreImportant;
        clear();

        DijkstraShortestPath.Result w1Result = dijkstra.findShortestPath(graph, "S", 0);
        DijkstraShortestPath.Result w2Result = dijkstra.findShortestPath(graph, "S", 1);

        //The shortest route according to one of the criteria for
        // comparison at the algorithm step
        List<String> referencePath;
        if (isFirstCriteriaIsMoreImportant) {
            referencePath = dijkstra.restoreOptimalPath(w1Result.getPreviousNodeList(), "T");
        } else {
            referencePath = dijkstra.restoreOptimalPath(w2Result.getPreviousNodeList(), "T");
        }

        // The number of stages in the task. It ss equal to the number
        // of edges in the optimal path
        stageCount = referencePath.size() - 1;

        for (String node : graph.nodeNamesSet()) {
            State state = new State(node);
            stateSet.add(state);

            //Form a set of possible controls for this state
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

    /**
     * Clear all the collections
     */
    private void clear() {
        stateSet.clear();
        stateControlSetMap.clear();
        referenceStateControlSetMap.clear();
        cacheW.clear();
    }

    @Override
    public Winnings w(Integer stage, State state, Control control) {
        double referenceW1 = referenceStateControlSetMap.get(stage).w1;
        double referenceW2 = referenceStateControlSetMap.get(stage).w2;

        double controlW1 = control.w1;
        double controlW2 = control.w2;

        double diff;
        if (isFirstCriteriaIsMoreImportant) {
            diff = referenceW1 - controlW1;
        } else {
            diff = referenceW2 - controlW2;
        }

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

            Winnings optimalWi;
            if (stage == stageCount - 1) {
                optimalWi = wi;
            } else {
                optimalWi = wi.add(W(stage + 1, nextState).winnings);
            }

            if (optimalWi.difference < -compromiseList.get(stage)) {
                optimalWi.w2 = Double.MAX_VALUE;
            }

            if (bestW == null || compare(optimalWi, bestW) > 0) {
                bestW = optimalWi;
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

    @Override
    public Result solve() {
        WinningAndControl winningAndControl = W(0, startState);
        List<String> optPath = restoreOptimalPath();
        return new Result(winningAndControl, optPath);
    }

    /**
     * Compares two values of the gain by the amount of the concession.
     * If these values are equal, then the value of a more weighty
     * criterion is compared.
     *
     * @param win1 first winnings
     * @param win2 second winnings
     * @return a negative integer, zero, or a positive integer as this object
     * is worse than, equal to, or better than the specified object
     */
    private int compare(Winnings win1, Winnings win2) {
        if (isFirstCriteriaIsMoreImportant) {
            return -Double.compare(win1.w2, win2.w2);
        } else {
            return -Double.compare(win1.w1, win2.w1);
        }
    }

    /**
     * State corresponding to the graph node
     */
    @AllArgsConstructor
    public static class State extends AbstractState {

        /**
         * The node name
         */
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


    /**
     * Control corresponding to the selected edge
     */
    @AllArgsConstructor
    public static class Control extends AbstractControl {

        /**
         * Previous state
         */
        private State source;

        /**
         * Next state
         */
        private State target;

        /**
         * first weight of the edge
         */
        private double w1;

        /**
         * second weight of the edge
         */
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


    @AllArgsConstructor
    public static class Winnings implements Comparable<Winnings> {

        Double difference;

        Double w1;

        Double w2;

        public Winnings add(Winnings o) {
            return new Winnings(
                    this.difference,
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


    /**
     * The value of winning with optimal control
     */
    @AllArgsConstructor
    public static class WinningAndControl {

        Winnings winnings;

        Control control;
    }


    /**
     * The structure for returning the result of solving the problem
     */
    @AllArgsConstructor
    @Getter
    public static class Result {

        private WinningAndControl winningAndControl;

        /**
         * A path in the form of a list of vertices
         */
        private List<String> optimalPath;
    }


    private static class Weights {

        double w1;

        double w2;

        public Weights(double... weights) {
            w1 = weights[0];
            w2 = weights[1];
        }
    }
}
