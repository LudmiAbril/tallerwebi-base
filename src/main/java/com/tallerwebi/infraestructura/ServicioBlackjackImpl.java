package com.tallerwebi.infraestructura;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.Baraja;
import com.tallerwebi.dominio.Carta;
import com.tallerwebi.dominio.ServicioBlackjack;

@Service("servicioBlackjack")
public class ServicioBlackjackImpl implements ServicioBlackjack {
    private Baraja baraja;

    @Autowired
    public ServicioBlackjackImpl() {
        this.baraja = new Baraja();
    }

    @Override
    public List<Carta> entregarCartasPrincipales() {
        List<Carta> cartasEntregadas = new ArrayList<>();
        cartasEntregadas.add(baraja.sacarCarta());
        cartasEntregadas.add(baraja.sacarCarta());
        return cartasEntregadas;
    }

    @Override
    public boolean hayBlackjack(List<Carta> cartasJugador) {
        if (calcularPuntuacion(cartasJugador) == 21) {
            return true;
        }
        return false;
    }

    @Override
    public boolean Perdio(List<Carta> cartasJugador) {
        if (calcularPuntuacion(cartasJugador) > 21) {
            return true;
        }
        return false;
    }

    @Override
    public Integer calcularPuntuacion(List<Carta> cartasJugador) {
        Integer puntuacion = 0;
        for (Carta c : cartasJugador) {
            puntuacion += c.getValor();
        }
        return puntuacion;
    }

}
