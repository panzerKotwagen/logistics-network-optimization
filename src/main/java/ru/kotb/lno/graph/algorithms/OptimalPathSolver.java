package ru.kotb.lno.graph.algorithms;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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

    @Setter
    private int mainCriteriaIdx = 0;

    /**
     * A sheet with the maximum values of concessions at each step
     */
    @Setter
    private Double globalCompromiseValue;


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
    private final Map<Integer, Double> referenceStateControlSetMap = new HashMap<>();

    /**
     * Memorization of calculated values
     */
    private final Map<Integer, Map<State, Result>> cacheW = new HashMap<>();

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

    private Double mainCriteriaValue;

    private State lastState;

    /**
     * Clear all the collections
     */
    private void clear() {
        stateSet.clear();
        stateControlSetMap.clear();
        referenceStateControlSetMap.clear();
        cacheW.clear();
        startState = null;
    }

    /**
     * Initializes states and controls
     *
     * @param graph
     * @param start
     */
    private void initStatesAndControls(Graph graph, String start) {
        Deque<String> deque = new LinkedList<>();
        Map<String, Integer> stateToStageNum = new HashMap<>();
        Set<String> viewed = new HashSet<>();
        stateToStageNum.put(start, 0);
        deque.add(start);

        while (!deque.isEmpty()) {
            String current = deque.pollFirst();
            viewed.add(current);
            int stageNumber = stateToStageNum.get(current);
            State currentState = new State(current);

            Set<Control> controlSet = new HashSet<>();
            for (Edge edge : graph.getAdjacentEdges(current)) {
                String next = graph.getOppositeNode(edge, current);

                if (viewed.contains(next)) {
                    continue;
                }

                deque.add(next);
                stateToStageNum.put(next, stageNumber + 1);

                State nextState = new State(next);
                Control control = new Control(edge, currentState, nextState);
                controlSet.add(control);
            }

            stateControlSetMap.put(currentState, controlSet);
        }

    }

    private void initReferenceControls(Graph graph, String startNode, String lastNode, int mainCriteriaIdx) {
        //The shortest route according to one of the criteria for
        // comparison at the algorithm step
        List<String> referencePath;
        if (mainCriteriaIdx == 0) {
            DijkstraShortestPath.Result w1Result = dijkstra.findShortestPath(graph, startNode, 0);
            referencePath = dijkstra.restoreOptimalPath(w1Result.getPreviousNodeList(), lastNode);
            mainCriteriaValue = w1Result.getDistances().get(lastNode);
        } else {
            DijkstraShortestPath.Result w2Result = dijkstra.findShortestPath(graph, startNode, 1);
            referencePath = dijkstra.restoreOptimalPath(w2Result.getPreviousNodeList(), lastNode);
            mainCriteriaValue = w2Result.getDistances().get(lastNode);
        }

        stageCount = referencePath.size() - 1;
    }

    /**
     * Initializes a variety of states, controls, etc.
     *
     * @param graph the graph in which you want to find the
     *              optimal route
     */
    public void init(Graph graph, String startNode, String lastNode, double globalCompromiseValue, int mainCriteriaIdx) {

        this.globalCompromiseValue = globalCompromiseValue;
        this.mainCriteriaIdx = mainCriteriaIdx;

        clear();

        if (startState == null) {
            startState = new State(startNode);
        }
        if (lastState == null) {
            lastState = new State(lastNode);
        }

        initStatesAndControls(graph, startNode);

        initReferenceControls(graph, startNode, lastNode, mainCriteriaIdx);
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

        for (Control control : stateControlSetMap.get(state)) {
            double wi = w(stage, state, control);
            State nextState = phi(state, control);

            nextState.mainCriteriaValue = state.mainCriteriaValue
                    + control.edge.getWeights()[mainCriteriaIdx];

            double optimalWi;
            if (nextState.mainCriteriaValue > mainCriteriaValue + globalCompromiseValue) {
                optimalWi = Double.MAX_VALUE;
            } else if (nextState.equals(lastState)) {
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
        optimalPath.add(startState.nodeName);
        for (int i = 0; i < stageCount; i++) {
            Result res = W(i, current);
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof State state)) return false;
            return Objects.equals(nodeName, state.nodeName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nodeName);
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
