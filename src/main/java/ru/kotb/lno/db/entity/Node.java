package ru.kotb.lno.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The class describes node on the logistics network
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "nodes")
public class Node extends AbstractEntity {

    /**
     * The node name
     */
    @Column(name = "name")
    private String name;
}
