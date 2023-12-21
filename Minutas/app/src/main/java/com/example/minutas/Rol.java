package com.example.minutas;

import java.io.Serializable;

public class Rol implements Serializable {
    private String rol;
    private int id;

    public Rol() {
    }

    public Rol(String rol) {
        this.rol = rol;
    }

    public String getRol() {
        return rol;
    }

    @Override
    public String toString() {
        return rol;
    }

    public int getId() {
        return id;
    }
}