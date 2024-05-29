package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioBlackjack {

    Baraja inicializarBaraja(Integer valorAs);

    List<Carta> entregarCartasPrincipales();

    boolean hayBlackjack(List<Carta> cartasJugador);

    Integer calcularPuntuacion(List<Carta> cartasJugador);

    boolean sePaso(List<Carta> cartasJugador);

    Carta pedirCarta();

    EstadoPartida estadoPartida(List<Carta> cartasJugador, List<Carta> cartasCasa, Boolean plantado);

    String ganador(List<Carta> cartasJugador, List<Carta> cartasCasa, String nombreJugador, Boolean plantado);

    List<Carta> plantarse(List<Carta> cartasCasa);

    void reponerBaraja();


}
