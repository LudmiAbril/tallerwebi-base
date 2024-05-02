package com.tallerwebi.infraestructura;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.Baraja;
import com.tallerwebi.dominio.Carta;
import com.tallerwebi.dominio.EstadoPartida;
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
    public boolean sePaso(List<Carta> cartasJugador) {
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

    @Override
    public Carta pedirCarta() {
        this.baraja.barajar();
        return this.baraja.sacarCarta();
    }

    @Override
    public EstadoPartida estadoPartida(List<Carta> cartasJugador, List<Carta> cartasCasa) {
        if (sePaso(cartasJugador) || sePaso(cartasCasa) || hayBlackjack(cartasJugador) || hayBlackjack(cartasCasa)) {
            return EstadoPartida.FINALIZADA;
        }
        return EstadoPartida.EN_CURSO;
    }

    @Override
    public String ganador(List<Carta> cartasJugador, List<Carta> cartasCasa, String nombreJugador) {
        String ganador = "ninguno";
        // logica para sacar un gaandor o un empate
        // no importa, la logica que estoy siguiendo esta bien!!!
        if (hayBlackjack(cartasJugador) && !hayBlackjack(cartasCasa)) {
            ganador = nombreJugador;
        }
        if (hayBlackjack(cartasCasa) && !hayBlackjack(cartasJugador)) {
            ganador = "casa";
        }
        if (hayBlackjack(cartasJugador) && hayBlackjack(cartasCasa)) {
            ganador = "empate";
        }
        if (sePaso(cartasJugador) && !sePaso(cartasCasa)) {
            ganador = "casa";
        }
        if (sePaso(cartasCasa) && !sePaso(cartasJugador) ) {
            ganador = nombreJugador;
        }
        if (sePaso(cartasJugador) && sePaso(cartasCasa)) {
            ganador = "empate";
        }

        return ganador;
    }

}
