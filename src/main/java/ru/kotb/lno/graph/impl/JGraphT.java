package ru.kotb.lno.graph.impl;

import org.jgrapht.graph.SimpleGraph;
import org.springframework.stereotype.Component;
import ru.kotb.lno.graph.components.Edge;
import ru.kotb.lno.graph.Graph;
import ru.kotb.lno.graph.components.Node;

import java.util.Set;


@Component
public class JGraphT implements Graph {

    private final org.jgrapht.Graph<Node, Edge> graph = new SimpleGraph<>(Edge.class);

    @Override
    public void addNode(String name) {
        if (hasNode(name)) {
            return;
        }
        Node node = new Node(name);
        graph.addVertex(node);
    }

    @Override
    public void addEdge(String firstNodeName, String secondNodeName, String name, int w1, int w2) {
        Node node1 = getNode(firstNodeName);
        Node node2 = getNode(secondNodeName);
        Edge edge = new Edge(name, w1, w2);
        graph.addEdge(node1, node2, edge);
    }

    @Override
    public Node getNode(String name) {
        return graph.vertexSet().stream()
                .filter(node -> node.getName().equals(name))
                .findAny().orElse(null);
    }

    @Override
    public Edge getEdge(String firstNodeName, String secondNodeName) {
        Node node1 = getNode(firstNodeName);
        Node node2 = getNode(secondNodeName);
        return graph.getEdge(node1, node2);
    }

    @Override
    public Set<Edge> getEdges(String nodeName) {
        Node node = getNode(nodeName);

        if (node == null) {
            return null;
        }

        return graph.edgesOf(node);
    }

    @Override
    public boolean hasNode(String nodeName) {
        return getNode(nodeName) != null;
    }

    @Override
    public boolean hasEdge(String firstNodeName, String secondNodeName) {
        return getEdge(firstNodeName, secondNodeName) != null;
    }

    @Override
    public void removeNode(String nodeName) {
        Node node = getNode(nodeName);
        graph.removeVertex(node);
    }

    @Override
    public void removeEdge(String firstNodeName, String secondNodeName) {
        Node node1 = getNode(firstNodeName);
        Node node2 = getNode(secondNodeName);
        graph.removeEdge(node1, node2);
    }
}
