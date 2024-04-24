package com.tallerwebi.dominio;

public class PartidaMultijugador extends Partida {
    private Jugador jugador;
    private Jugador jugador2;
    private Jugador ganador;

    public PartidaMultijugador(Juego juego, Jugador jugador, Jugador jugador2, Jugador ganador) {
        super(juego);
        this.jugador=jugador;
        this.jugador2=jugador2;
        this.ganador=ganador;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }
}
