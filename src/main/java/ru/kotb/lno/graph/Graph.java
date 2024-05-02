package ru.kotb.lno.graph;


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
                 int w1, int w2);

    /**
     * Get node on its name
     *
     * @param name node name
     * @return node
     */
    Node getNode(String name);

    /**
     * Get edge on its name
     *
     * @param name edge name
     * @return edge
     */
    Edge getEdge(String name);

    /**
     * Get all edges connected with the specified node
     *
     * @param nodeName node name
     * @return set of edges connected to the node
     */
    Set<Edge> getEdges(String nodeName);
}
