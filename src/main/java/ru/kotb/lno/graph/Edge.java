package ru.kotb.lno.graph;

/**
 * The class describes a graph edge
 */
public class Edge {

    /**
     * Edge name
     */
    private String name;

    /**
     * First node connected to the edge
     */
    private Node previousNode;

    /**
     * Second node connected to the edge
     */
    private Node nextNode;

    /**
     * The edge weight №1
     */
    private int w1;

    /**
     * The edge weight №2
     */
    private int w2;
}
