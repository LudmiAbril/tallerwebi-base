package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.List;

public class SalaEspera {
    private List<Usuario> jugadores;
    private boolean partidaIniciada;

    public SalaEspera() {
        this.jugadores = new ArrayList<>();
        this.partidaIniciada = false;
    }

    public List<Usuario> getJugadores() {
        return jugadores;
    }

    public void agregarJugador(Usuario jugador) {
        this.jugadores.add(jugador);
    }

    public boolean isPartidaIniciada() {
        return partidaIniciada;
    }

    public void setPartidaIniciada(boolean partidaIniciada) {
        this.partidaIniciada = partidaIniciada;
    }

    public boolean hayDosJugadores() {
        return this.jugadores.size() == 2;
    }

    public Usuario getJugador1() {
        return jugadores.size() > 0 ? jugadores.get(0) : null;
    }

    public Usuario getJugador2() {
        return jugadores.size() > 1 ? jugadores.get(1) : null;
    }
}
