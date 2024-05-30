package ru.kotb.lno.graph.algorithms;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.kotb.lno.graph.Graph;
import ru.kotb.lno.graph.components.Edge;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * A class for solving the problem of finding the optimal path in
 * a graph with two criteria
 */
public class OptimalPathSolver
        extends DynamicProgramming<OptimalPathSolver.State, OptimalPathSolver.Control, Double, OptimalPathSolver.Result> {

    private final DijkstraShortestPath dijkstra = new DijkstraShortestPath();

    private final Graph graph;

    private final int mainCriteriaIdx;

    private double globalCompromiseValue;

    /**
     * Memorization of calculated values
     */
    private final Map<Integer, Map<State, Result>> cacheW = new HashMap<>();

    /**
     * The starting state. Corresponds to the node from which the
     * path is being searched
     */
    private State startState;

    private Double mainCriteriaValue;

    private State lastState;

    public OptimalPathSolver(Graph graph, String startState, String lastState, int mainCriteriaIdx, double globalCompromiseValue) {
        this.graph = graph;
        this.mainCriteriaIdx = mainCriteriaIdx;
        this.globalCompromiseValue = globalCompromiseValue;

        init(startState, lastState);
    }

    public void setStartState(String startState) {
        init(startState, lastState.nodeName);
    }

    public void setGlobalCompromiseValue(double globalCompromiseValue) {
        this.globalCompromiseValue = globalCompromiseValue;
        cacheW.clear();
    }

    /**
     * Initializes states and controls
     */
    private void initStatesAndControls(String start, String last) {
        lastState = new State(last);
        startState = new State(start);

        Deque<State> deque = new LinkedList<>();
        Set<State> viewed = new HashSet<>();
        deque.add(startState);

        while (!deque.isEmpty()) {
            State current = deque.pollFirst();

            if (!deque.isEmpty() && isStateLast(current)) {
                continue;
            }

            viewed.add(current);

            Set<Control> controlSet = new HashSet<>();
            for (Edge edge : graph.getAdjacentEdges(current.nodeName)) {
                State next = new State(graph.getOppositeNode(edge, current.nodeName));

                if (viewed.contains(next)) {
                    continue;
                }

                deque.add(next);

                Control control = new Control(edge, current, next);
                controlSet.add(control);
            }

            current.controlSet = controlSet;
        }

        if (!viewed.contains(lastState)) {
            throw new RuntimeException("There is no path from " + start
                    + " to " + last + " in the graph");
        }
    }

    private void initMainCriteriaValue(String startNode, String lastNode, int mainCriteriaIdx) {
        if (mainCriteriaIdx == 0) {
            DijkstraShortestPath.Result w1Result = dijkstra.findShortestPath(graph, startNode, 0);
            mainCriteriaValue = w1Result.getDistances().get(lastNode);
        } else {
            DijkstraShortestPath.Result w2Result = dijkstra.findShortestPath(graph, startNode, 1);
            mainCriteriaValue = w2Result.getDistances().get(lastNode);
        }
    }

    /**
     * Initializes a variety of states, controls, etc.
     */
    private void init(String startNode, String lastNode) {
        cacheW.clear();
        initStatesAndControls(startNode, lastNode);
        initMainCriteriaValue(startNode, lastNode, mainCriteriaIdx);
    }

    private boolean isStateLast(State current) {
        return current.nodeName.equals(lastState.nodeName);
    }

    @Override
    public Double w(Integer stage, State state, Control control) {
        if (mainCriteriaIdx == 0) {
            return control.edge.getWeights()[1];
        } else {
            return control.edge.getWeights()[0];
        }
    }

    @Override
    public State phi(State state, Control control) {
        return control.nextState;
    }

    @Override
    public Result W(Integer stage, State state) {
        if (cacheW.containsKey(stage)) {
            if (cacheW.get(stage).containsKey(state)) {
                return cacheW.get(stage).get(state);
            }
        }

        double bestW = Double.MAX_VALUE;
        Control bestControl = null;

        for (Control control : state.controlSet) {
            double wi = w(stage, state, control);
            State nextState = phi(state, control);

            nextState.mainCriteriaValue = state.mainCriteriaValue
                    + control.edge.getWeights()[mainCriteriaIdx];

            double optimalWi;
            if (nextState.mainCriteriaValue > mainCriteriaValue + globalCompromiseValue) {
                optimalWi = Double.MAX_VALUE;
            } else if (isStateLast(nextState)) {
                optimalWi = wi;
            } else {
                optimalWi = wi + W(stage + 1, nextState).win;
            }

            if (bestW == Double.MAX_VALUE || Double.compare(optimalWi, bestW) < 0) {
                bestW = optimalWi;
                bestControl = control;
            }
        }

        Result res = new Result(bestW, bestControl);
        if (!cacheW.containsKey(stage)) {
            cacheW.put(stage, new HashMap<>());
        }
        cacheW.get(stage).put(state, res);

        return res;
    }

    @Override
    public Result solve() {
        return W(0, startState);
    }

    /**
     * Restores the optimal route
     *
     * @return the route in the form of a sequence of vertices names
     */
    public List<String> restoreOptimalPath() {
        List<String> optimalPath = new ArrayList<>();

        State current = startState;
        int stageNum = 0;
        optimalPath.add(startState.nodeName);
        while (!isStateLast(current)) {
            Result res = W(stageNum++, current);
            optimalPath.add(res.bestControl.nextState.nodeName);
            Control control = res.bestControl;
            current = phi(current, control);
        }

        return optimalPath;
    }

    /**
     * State corresponding to the graph node
     */
    @RequiredArgsConstructor
    public static class State extends AbstractState {

        private final String nodeName;

        private double mainCriteriaValue;

        private Set<Control> controlSet;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof State state)) return false;
            return Double.compare(mainCriteriaValue, state.mainCriteriaValue) == 0 && Objects.equals(nodeName, state.nodeName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nodeName, mainCriteriaValue);
        }

        @Override
        public String toString() {
            return "State{" +
                    "nodeName='" + nodeName + '\'' +
                    ", mainCriteriaValue=" + mainCriteriaValue +
                    '}';
        }
    }


    /**
     * Control corresponding to the selected edge
     */
    @RequiredArgsConstructor
    public static class Control extends AbstractControl {

        private final Edge edge;

        private final State prevState;

        private final State nextState;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Control control)) return false;
            return edge.equals(control.edge);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(edge);
        }

        @Override
        public String toString() {
            return "Control{" +
                    "edge=" + edge +
                    ", prevState=" + prevState +
                    ", nextState=" + nextState +
                    '}';
        }
    }


    @RequiredArgsConstructor
    public static class Result {

        @Getter
        private final Double win;

        private final Control bestControl;

        @Override
        public String toString() {
            return "Result{" +
                    "win=" + win +
                    '}';
        }
    }
}
