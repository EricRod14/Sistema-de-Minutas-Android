package com.example.minutas;

import java.io.Serializable;

public class Carrera implements Serializable {
    private String carrera;
    private int id;

    public Carrera() {
    }

    public Carrera(String carrera) {
        this.carrera = carrera;
    }

    public String getCarrera() {
        return carrera;
    }

    @Override
    public String toString() {
        return carrera;
    }

    public int getId() {
        return id;
    }

}