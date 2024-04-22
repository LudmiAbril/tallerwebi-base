package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.List;

public class ServicioBlackjack {
    private Baraja baraja;

    public ServicioBlackjack() {
        this.baraja = new Baraja();
    }

    public List<Carta> entregarCartasPrincipales() {
        List<Carta> cartasEntregadas = new ArrayList<>();
        cartasEntregadas.add(baraja.sacarCarta());
        cartasEntregadas.add(baraja.sacarCarta());
        return cartasEntregadas;
    }

    public boolean hayBlackjack(List<Carta> cartasJugador) {
        if (calcularPuntuacion(cartasJugador) == 21) {
            return true;
        }
        return false;
    }

    private Integer calcularPuntuacion(List<Carta> cartasJugador) {
        Integer puntuacion = 0;
        for (Carta c : cartasJugador) {
            puntuacion += c.getValor();
        }
        return puntuacion;
    }

    public boolean Perdio(List<Carta> cartasJugador) {
        if (calcularPuntuacion(cartasJugador) > 21) {
            return true;
        }
        return false;
    }

}
