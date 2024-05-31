package ru.kotb.lno.graph;

import ru.kotb.lno.graph.components.Edge;
import ru.kotb.lno.graph.components.Node;

import java.util.Set;


/**
 * The interface describes a simple undirected weighted graph
 */
public interface Graph {

    /**
     * Add node to graph
     *
     * @param name node name
     */
    void addNode(String name);

    /**
     * Add edge to graph
     *
     * @param firstNodeName  first node linked with edge
     * @param secondNodeName second node linked with edge
     * @param name           edge name
     * @param w1             weight value #1
     * @param w2             weight value #2
     */
    void addEdge(String firstNodeName, String secondNodeName, String name,
                 double w1, double w2);

    /**
     * Get node on its name
     *
     * @param name node name
     * @return node or null
     */
    Node getNode(String name);

    /**
     * Get all node names from the graph
     *
     * @return set of the node names
     */
    Set<String> nodeNamesSet();

    /**
     * Get edge
     *
     * @param firstNodeName  first node linked with edge
     * @param secondNodeName second node linked with edge
     * @return edge or null
     */
    Edge getEdge(String firstNodeName, String secondNodeName);

    /**
     * Get all edges connected with the specified node
     *
     * @param nodeName node name
     * @return set of edges connected to the node or null
     */
    Set<Edge> getEdges(String nodeName);

    /**
     * Check if graph has node with specified name
     *
     * @param nodeName node name
     * @return true if graph contains node with specified name
     */
    boolean hasNode(String nodeName);

    /**
     * Check if graph has edge between specified nodes
     *
     * @param firstNodeName  first node name
     * @param secondNodeName second node name
     * @return true if graph contains such an edge
     */
    boolean hasEdge(String firstNodeName, String secondNodeName);

    /**
     * Remove the node with the specified name
     *
     * @param nodeName node name
     */
    void removeNode(String nodeName);

    /**
     * Remove the edge between specified nodes
     *
     * @param firstNodeName  the name of the first node
     * @param secondNodeName the name of the second node
     */
    void removeEdge(String firstNodeName, String secondNodeName);

    /**
     * Updates edge weights
     *
     * @param firstNodeName  the name of the first node
     * @param secondNodeName the name of the second node
     */
    void updateEdgeWeight(String firstNodeName, String secondNodeName, double w1, double w2);

    /**
     * Returns the node at the opposite end from {@code adjacentEdge}
     *
     * @param adjacentEdge the node for which we are looking for a neighbor node
     * @param current      the name of the known vertex
     * @return the node at the opposite end from {@code adjacentEdge}
     */
    String getOppositeNode(Edge adjacentEdge, String current);

    /**
     * Get all adjacent edges with a vertex
     *
     * @param nodeName node name
     * @return set of adjacent edges
     */
    Set<Edge> getAdjacentEdges(String nodeName);
}
