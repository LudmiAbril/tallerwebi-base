package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioBlackjack {

    List<Carta> entregarCartasPrincipales();

    boolean hayBlackjack(List<Carta> cartasJugador);

    Integer calcularPuntuacion(List<Carta> cartasJugador);

    boolean sePaso(List<Carta> cartasJugador);

    Carta pedirCarta();

    EstadoPartida estadoPartida(List<Carta> cartasJugador, List<Carta> cartasCasa);

    String ganador(List<Carta> cartasJugador, List<Carta> cartasCasa, String nombreJugador);

}
