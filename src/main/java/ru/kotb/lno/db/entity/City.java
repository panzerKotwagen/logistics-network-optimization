package ru.kotb.lno.db.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


/**
 * The class describes city point on the logistics network
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cities")
public class City extends AbstractEntity {

    /**
     * The city name
     */
    @Column(name = "name")
    private String name;

    /**
     * Motor roads connected to the city
     */
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<MotorRoad> roads = new HashSet<>();
}
