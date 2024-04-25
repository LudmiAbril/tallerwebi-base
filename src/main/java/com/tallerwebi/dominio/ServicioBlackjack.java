package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioBlackjack {

    List<Carta> entregarCartasPrincipales();

    boolean hayBlackjack(List<Carta> cartasJugador);

    Integer calcularPuntuacion(List<Carta> cartasJugador);

    boolean Perdio(List<Carta> cartasJugador);

}
