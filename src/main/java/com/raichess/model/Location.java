package com.raichess.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_seq")
    @SequenceGenerator(name = "location_seq", sequenceName = "location_id_seq", allocationSize = 1)
    private Integer id;

    @NotNull(message = "x не может быть null")
    @Column(nullable = false)
    private Double x;

    @NotNull(message = "y не может быть null")
    @Column(nullable = false)
    private Double y;

    @Column(nullable = false)
    private long z;

    @Pattern(regexp = ".*\\S.*", message = "Название не может быть пустой строкой")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public long getZ() {
        return z;
    }

    public void setZ(long z) {
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
