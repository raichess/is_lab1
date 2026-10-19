package com.raichess.model;

import jakarta.persistence.*;

@Entity
@Table(name = "coordinates")
public class Coordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coordinates_seq")
    @SequenceGenerator(name = "coordinates_seq", sequenceName = "coordinates_id_seq", allocationSize = 1)
    private Integer id;

    @Column(nullable = false)
    private double x;

    @Column(nullable = false)
    private double y;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
