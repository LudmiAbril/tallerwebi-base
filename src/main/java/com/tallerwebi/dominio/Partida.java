package com.tallerwebi.dominio;

public abstract class Partida {
    private Integer puntaje;
    private Juego juego;

    public Partida(Juego juego){
        this.juego = juego;
    }

    public Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }
}
