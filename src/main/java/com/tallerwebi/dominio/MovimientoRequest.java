package com.tallerwebi.dominio;

public class MovimientoRequest {
    private String jugador;
    private Integer numeroCasillero;

    // Getters y Setters
    public String getJugador() {
        return jugador;
    }

    public void setJugador(String jugador) {
        this.jugador = jugador;
    }

    public Integer getNumeroCasillero() {
        return numeroCasillero;
    }

    public void setNumeroCasillero(Integer numeroCasillero) {
        this.numeroCasillero = numeroCasillero;
    }
}
