package com.tallerwebi.dominio.dto;
import java.util.List;
public class SalaEsperaMessage {

    private String message;
    private List<String> jugadores;

    public SalaEsperaMessage(String message, List<String> jugadores) {
        this.message = message;
        this.jugadores = jugadores;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String type) {
        this.message = message;
    }

    public List<String> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<String> jugadores) {
        this.jugadores = jugadores;
    }
}
