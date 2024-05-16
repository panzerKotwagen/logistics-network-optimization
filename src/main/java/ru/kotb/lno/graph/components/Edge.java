package ru.kotb.lno.graph.components;

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
@NoArgsConstructor
public class Edge extends DefaultEdge {

    private static final int WEIGHT_COUNT = 2;

    /**
     * Edge name
     */
    private String name;

    /**
     * Weight array
     */
    private final int[] weights = new int[WEIGHT_COUNT];

    public Edge(String name, int w1, int w2) {
        this.name = name;
        weights[0] = w1;
        weights[1] = w2;
    }

    public String getSourceNode() {
        return getSource().toString();
    }

    public String getTargetNode() {
        return getTarget().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Edge edge)) {
            return false;
        }
        return Objects.equals(getSource(), edge.getSource()) && Objects.equals(getTarget(), edge.getTarget());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSource(), getTarget());
    }
}
