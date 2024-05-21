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


public class DynamicProgrammingSolver extends DynamicProgramming<DynamicProgrammingSolver.State, DynamicProgrammingSolver.Control> {

    private Integer stageCount;

    private State startState;

    private Set<State> stateSet = new HashSet<>();

    private Map<State, Set<Control>> stateControlSetMap = new HashMap<>();

    private Map<Integer, Weights> referenceStateControlSetMap = new HashMap<>();

    private Map<Integer, WinningAndControl> cacheW = new HashMap<>();

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
    public double w(Integer stage, State state, Control control) {
        double referenceW1 = referenceStateControlSetMap.get(stage).w1;
        double referenceW2 = referenceStateControlSetMap.get(stage).w2;

        double controlW1 = control.w1;
        double controlW2 = control.w2;

        return referenceW1 - controlW1 + referenceW2 - controlW2;
    }

    @Override
    public State phi(State state, Control control) {
        if (stateControlSetMap.get(state).contains(control)) {
            return control.target;
        }
        throw new RuntimeException("The transmitted state does not have the specified control");
    }

    @Override
    public WinningAndControl solve() {
        return W(0, startState);
    }

    @Override
    public WinningAndControl W(Integer stage, State state) {
        if (cacheW.containsKey(state)) {
            return cacheW.get(state);
        }

        Double bestW = null;
        Control bestControl = null;

        for (Control control : stateControlSetMap.get(state)) {
            Double wi = w(stage, state, control);
            State nextState = phi(state, control);

            Double Wi;
            if (stage == stageCount - 1) {
                Wi = wi;
            } else {
                Wi = wi + W(stage + 1, nextState).w;
            }

            if (bestW == null || Wi > bestW) {
                bestW = Wi;
                bestControl = control;
            }
        }

        WinningAndControl res = new WinningAndControl(bestW, bestControl);
        cacheW.put(stage, res);
        return res;
    }

    public List<String> restoreOptimalPath() {
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

        Double w;

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
