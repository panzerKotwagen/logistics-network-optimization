package ru.kotb.lno.graph.impl;

import lombok.AllArgsConstructor;
import org.jgrapht.graph.SimpleGraph;
import org.springframework.stereotype.Component;
import ru.kotb.lno.graph.Graph;
import ru.kotb.lno.graph.components.Edge;
import ru.kotb.lno.graph.components.Node;
import ru.kotb.lno.optimization.CompromiseScheme;
import ru.kotb.lno.optimization.GlobalCriteria;
import ru.kotb.lno.optimization.Solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;


@Component
public class JGraphT implements Graph {

    private Solver solver = new Solver();

    private CompromiseScheme scheme = new CompromiseScheme();

    private final org.jgrapht.Graph<String, Edge> graph = new SimpleGraph<>(Edge.class);

    @Override
    public void addNode(String name) {
        if (hasNode(name)) {
            return;
        }
        graph.addVertex(name);
    }

    @Override
    public void addEdge(String firstNodeName, String secondNodeName, String name, int w1, int w2) {
        Edge edge = new Edge(name, w1, w2);
        graph.addEdge(firstNodeName, secondNodeName, edge);
    }

    @Override
    public Node getNode(String nodeName) {
        Optional<String> optionalName = graph.vertexSet().stream()
                .filter(s -> s.equals(nodeName))
                .findAny();

        if (optionalName.isEmpty()) {
            return null;
        }

        return new Node(nodeName);
    }

    @Override
    public Edge getEdge(String firstNodeName, String secondNodeName) {
        return graph.getEdge(firstNodeName, secondNodeName);
    }

    @Override
    public Set<Edge> getEdges(String nodeName) {
        if (!hasNode(nodeName)) {
            return null;
        }

        return graph.edgesOf(nodeName);
    }

    @Override
    public boolean hasNode(String nodeName) {
        return graph.containsVertex(nodeName);
    }

    @Override
    public boolean hasEdge(String firstNodeName, String secondNodeName) {
        return getEdge(firstNodeName, secondNodeName) != null;
    }

    @Override
    public void removeNode(String nodeName) {
        graph.removeVertex(nodeName);
    }

    @Override
    public void removeEdge(String firstNodeName, String secondNodeName) {
        graph.removeEdge(firstNodeName, secondNodeName);
    }

    @Override
    public void updateEdgeWeight(String firstNodeName, String secondNodeName, int w1, int w2) {
        Edge edge = getEdge(firstNodeName, secondNodeName);
        edge.getWeights()[0] = w1;
        edge.getWeights()[1] = w2;
    }

    Set<Edge> getAdjacentEdges(String nodeName) {
        return graph.edgesOf(nodeName);
    }

    public Map<String, GlobalCriteria> findShortestPath(String firstNodeName) {
        Map<String, GlobalCriteria> criteriaMap = new LinkedHashMap<>();
        for (String vertex : graph.vertexSet()) {
            criteriaMap.put(vertex, GlobalCriteria.MAX_CRITERIA);
        }

        Queue<NodeAndCriteria> queue = new LinkedList<>();
        queue.add(new NodeAndCriteria(firstNodeName, GlobalCriteria.ZERO_CRITERIA));

        while (!queue.isEmpty()) {
            NodeAndCriteria current = queue.poll();

            //Minus because we are solving the problem of minimization
            int res = -solver.compareTwoCriteria(current.criteria, criteriaMap.get(current.node));
            //Processing only the vertex with the smallest distance
            if (res < 0) {
                continue;
            }

            for (Edge adjacentEdge : getAdjacentEdges(current.node)) {
                String neighbourNode = getOppositeNode(adjacentEdge, current.node);
                GlobalCriteria comparedCriteria = current.criteria.add(
                        new GlobalCriteria(adjacentEdge.getWeights())
                );

                List<GlobalCriteria> criteriaList = List.of(
                        criteriaMap.get(neighbourNode),
                        comparedCriteria
                );

                GlobalCriteria bestCriteria = solver.solve(criteriaList);

                //We consider this new path only if it is better than
                // any path we have found so far
                if (bestCriteria.equals(comparedCriteria)) {
                    criteriaMap.put(neighbourNode, bestCriteria);
                    queue.add(new NodeAndCriteria(neighbourNode, bestCriteria));
                }
            }
        }
        return criteriaMap;
    }

    /**
     * Returns the node at the opposite end from {@code adjacentEdge}
     *
     * @param adjacentEdge the node for which we are looking for a neighbor node
     * @param current      the name of the known vertex
     * @return the node at the opposite end from {@code adjacentEdge}
     */
    private static String getOppositeNode(Edge adjacentEdge, String current) {
        String source = adjacentEdge.getSourseNode();
        String target = adjacentEdge.getTargetNode();
        if (source.equals(current)) {
            return target;
        } else {
            return source;
        }
    }

    @AllArgsConstructor
    private static class NodeAndCriteria {

        public String node;

        public GlobalCriteria criteria;
    }
}
