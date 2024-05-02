package ru.kotb.lno.graph.impl;

import org.jgrapht.graph.SimpleGraph;
import org.springframework.stereotype.Component;
import ru.kotb.lno.graph.Graph;
import ru.kotb.lno.graph.components.Edge;
import ru.kotb.lno.graph.components.Node;

import java.util.Optional;
import java.util.Set;


@Component
public class JGraphT implements Graph {

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
}
