package ru.kotb.lno.graph;

import java.util.HashSet;
import java.util.Set;


/**
 * The class describes a graph node
 */
public class Node {

    /**
     * Set of edges connected to the node
     */
    private Set<Edge> edges = new HashSet<>();

    /**
     * Node name
     */
    private String name;

}
