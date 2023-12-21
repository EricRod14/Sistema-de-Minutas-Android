package com.example.minutas;

import java.io.Serializable;

public class Usuario implements Serializable {
    private int id;
    private String nombre;
    private String apellidoP;
    private String apellidoM;
    private String correo;
    private String clave;
    private Carrera carrera;
    private Rol rol;

    public Usuario() {
    }

    public Usuario(int id, String nombre, String apellidoP, String apellidoM, String correo, String clave, Carrera carrera, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.correo = correo;
        this.clave = clave;
        this.carrera = carrera;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public Rol getRol() {
        return rol;
    }
}
