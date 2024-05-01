package ru.kotb.lno.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The class describes a highway on the logistics network
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roads")
public class MotorRoad extends AbstractEntity {

    /**
     * The notation
     */
    @Column(name = "name")
    private String name;

    /**
     * The notional amount of all expenses incurred during the trip
     */
    @Column(name = "cost")
    private int cost;

    /**
     * Expected travel time
     */
    @Column(name = "time")
    private int time;

    /**
     * One of the cities connected to the road
     */
    @OneToOne
    @JoinColumn(name = "first_city_id")
    private City firstCity;

    /**
     * The second of the cities connected to the road
     */
    @OneToOne
    @JoinColumn(name = "second_city_id")
    private City secondCity;
}
