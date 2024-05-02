package ru.kotb.lno.graph.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jgrapht.graph.DefaultEdge;

import java.util.Objects;


/**
 * The class describes a graph edge
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Edge extends DefaultEdge {

    /**
     * Edge name
     */
    private String name;


    /**
     * The edge weight №1
     */
    private int w1;

    /**
     * The edge weight №2
     */
    private int w2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge edge)) return false;
        return Objects.equals(getSource(), edge.getSource()) && Objects.equals(getTarget(), edge.getTarget());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSource(), getTarget());
    }
}
