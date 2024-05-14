package ru.kotb.lno.graph.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;


/**
 * The class describes a graph node
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Node {

    /**
     * Node name
     */
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node node)) return false;
        return Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
