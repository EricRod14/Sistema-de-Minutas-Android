package com.example.minutas;

public class Minuta {

    private int id;
    private String lugar;
    private String asunto;
    private String objetivo;
    private String fecha;
    private String acuerdo;
    private String participante;

    public Minuta() {
    }

    public Minuta(int id, String lugar, String asunto, String objetivo, String fecha, String acuerdo, String participante) {
        this.id = id;
        this.lugar = lugar;
        this.asunto = asunto;
        this.objetivo = objetivo;
        this.fecha = fecha;
        this.acuerdo = acuerdo;
        this.participante = participante;
    }

    public int getId() {
        return id;
    }

    public String getLugar() {
        return lugar;
    }

    public String getAsunto() {
        return asunto;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public String getFecha() {
        return fecha;
    }

    public String getAcuerdo() {
        return acuerdo;
    }

    public String getParticipante() {
        return participante;
    }
}