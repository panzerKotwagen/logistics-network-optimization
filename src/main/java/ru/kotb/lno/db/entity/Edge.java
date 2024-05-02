package ru.kotb.lno.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


/**
 * The class describes an edge with multiple weights on the logistics
 * network
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "edges")
public class Edge {

    @EmbeddedId
    private EdgePK edgePK;

    /**
     * The edge name
     */
    @Column(name = "name")
    private String name;

    /**
     * Expected costs
     */
    @Column(name = "cost")
    private int cost;

    /**
     * Expected travel time
     */
    @Column(name = "time")
    private int time;

    public Edge(EdgePK edgePK) {
        this.edgePK = edgePK;
    }

    public Edge(Node previousNode, Node secondNode) {
        this.edgePK = new EdgePK(previousNode, secondNode);
    }

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EdgePK implements Serializable {

        @OneToOne
        @JoinColumn(name = "previous_node_id")
        private Node previousNode;

        @OneToOne
        @JoinColumn(name = "next_node_id")
        private Node nextNode;

    }
}
