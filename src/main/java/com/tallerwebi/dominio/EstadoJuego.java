package com.tallerwebi.dominio;

public enum EstadoJuego {
    WAITING_FOR_PLAYER("Esperando por un jugador..."),
    PLAYER1_TURN("Turno del jugador 1."),
    PLAYER2_TURN("Turno del jugador 2."),
    PLAYER1_WON("Ganó el jugador 1."),
    PLAYER2_WON("Ganó el jugador 2."),
    TIE("Empate.");

    String description;

    EstadoJuego(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
