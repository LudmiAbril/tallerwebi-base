package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String jugador;
    private Integer puntaje;
    private Juego juego;
    // private String ganador;

    public Partida(String jugador, Integer puntaje, Juego juego) {
        this.jugador = jugador;
        this.puntaje = puntaje;
        this.juego = juego;
    }

    public Juego getJuego() {
        return juego;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    public Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }

    public String getJugador() {
        return jugador;
    }

    public void setJugador(String jugador) {
        this.jugador = jugador;
    }

}
