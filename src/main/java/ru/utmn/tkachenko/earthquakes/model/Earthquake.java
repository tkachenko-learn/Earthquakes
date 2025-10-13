package ru.utmn.tkachenko.earthquakes.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Earthquake {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column
    Integer deep;

    @Column
    String type;

    @Column
    Double magnitude;

    @Column(length = 100)
    String state;

    @Column(name = "date_event")
    LocalDateTime dateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDeep() {
        return deep;
    }

    public void setDeep(Integer deep) {
        this.deep = deep;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Double magnitude) {
        this.magnitude = magnitude;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
