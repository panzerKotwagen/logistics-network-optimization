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
import java.util.HashSet;
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

//    Set<Node> getAdjacentNodes(String nodeName) {
//        Set<Edge> adjacentEdges = graph.edgesOf(nodeName);
//        Set<Node> adjacentNodes = new HashSet<>();
//        for (Edge edge : adjacentEdges) {
//            String source = edge.getSourseNode();
//            String target = edge.getTargetNode();
//            if (source.equals(nodeName)) {
//                adjacentNodes.add(target);
//            } else {
//                adjacentNodes.add(source);
//            }
//        }
//        return adjacentNodes;
//    }

    @Override
    public Map<String, Integer> findShortestPath(String firstNodeName) {
        Map<String, Integer> distances = new LinkedHashMap<>();
        for (String vertex : graph.vertexSet()) {
            distances.put(vertex, Integer.MAX_VALUE);
        }
        distances.put(firstNodeName, 0);

        Queue<NodeAndDistance> queue = new LinkedList<>();
        queue.add(new NodeAndDistance(firstNodeName, 0));

        while (!queue.isEmpty()) {
            NodeAndDistance current = queue.poll();

            if (current.distance > distances.get(current.node)) {
                continue;
            }

            for (Edge adjacentEdge : getAdjacentEdges(current.node)) {
                String neighbourNode = getOppositeNode(adjacentEdge, current);
                int distance = current.distance + adjacentEdge.getWeights()[0];

                if (distance < distances.get(neighbourNode)) {
                    distances.put(neighbourNode, distance);
                    queue.add(new NodeAndDistance(neighbourNode, distance));
                }
            }
        }
        return distances;
    }

    private static String getOppositeNode(Edge adjacentEdge, NodeAndDistance current) {
        String source = adjacentEdge.getSourseNode();
        String target = adjacentEdge.getTargetNode();
        if (source.equals(current.node)) {
            return target;
        } else {
            return source;
        }
    }

    @AllArgsConstructor
    private static class NodeAndDistance {
        public String node;
        public Integer distance;
    }

//    public void foo() {
//        Map<Node, Edge> nodeEdgeMap = new LinkedHashMap<>();
//        Set<Node> viewed = new HashSet<>();
//        Edge lastOptimal = null;
//        Node current = getNode("");
//        for (Edge adjacentEdge : getAdjacentEdges(current.getName())) {
//            Node targetNode = adjacentEdge.getTargetNode();
//            //If the vertex has not been marked
//            if (!nodeEdgeMap.containsKey(targetNode)) {
//                nodeEdgeMap.put(targetNode, adjacentEdge);
//            } else {
//                //Find the optimal edge of two: the already recorded
//                // one and the current one
//                Edge existedEdge = nodeEdgeMap.get(targetNode);
//                List<Edge> comparedEdges = List.of(existedEdge, adjacentEdge);
//                Edge optimal = findOptimalEdge(comparedEdges);
//
//                nodeEdgeMap.put(targetNode, optimal);
//            }
//        }
//        List.of();
//    }

    private Edge findOptimalEdge(List<Edge> edges) {
        List<GlobalCriteria> criteriaList = new ArrayList<>();
        //For finding an edge with weighting coefficients equal to the
        // values of the global criterion
        Map<GlobalCriteria, Edge> edgeCriteriaMap = new HashMap<>();
        for (Edge edge : edges) {
            GlobalCriteria criteria = new GlobalCriteria(edge.getWeights());
            criteriaList.add(criteria);
            edgeCriteriaMap.put(criteria, edge);
        }
        //Build pareto set
        List<GlobalCriteria> paretoSet = solver.paretoSet(criteriaList);
        //Find optimal criteria
        GlobalCriteria optimal = scheme.convolution(paretoSet, new double[]{0.5, 0.5});

        return edgeCriteriaMap.get(optimal);
    }
}
