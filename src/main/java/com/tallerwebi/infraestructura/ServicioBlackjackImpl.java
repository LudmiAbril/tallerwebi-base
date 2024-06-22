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
    }

    @Override
    public List<Carta> entregarCartasPrincipales() {
        List<Carta> cartasEntregadas = new ArrayList<>();
        if (baraja.getSize() >= 2) {
            cartasEntregadas.add(baraja.sacarCarta());
            cartasEntregadas.add(baraja.sacarCarta());
        } else {
            reponerBaraja();
            cartasEntregadas.add(baraja.sacarCarta());
            cartasEntregadas.add(baraja.sacarCarta());
        }
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
        if (baraja.getSize() == 0) {
            reponerBaraja();
        }
        this.baraja.barajar();
        return this.baraja.sacarCarta();
    }

    @Override
    public EstadoPartida estadoPartida(List<Carta> cartasJugador, List<Carta> cartasCasa, Boolean plantado) {
        if (plantado || sePaso(cartasJugador) || hayBlackjack(cartasJugador)) {
            return EstadoPartida.FINALIZADA;
        }
        return EstadoPartida.EN_CURSO;
    }

    @Override
    public String ganador(List<Carta> cartasJugador, List<Carta> cartasCasa, String nombreJugador, Boolean plantado) {
        String ganador = "ninguno";

        if (hayBlackjack(cartasJugador)) {
            ganador = nombreJugador;
        }
        if (sePaso(cartasJugador)) {
            ganador = "casa";
        }

        if (plantado) {

            if (sePaso(cartasCasa) && !sePaso(cartasJugador)) {
                ganador = nombreJugador;
            }
            if (sePaso(cartasJugador) && !sePaso(cartasCasa)) {
                ganador = "casa";
            }
            if (sePaso(cartasJugador) && sePaso(cartasCasa)) {
                ganador = "empate";
            }
            if (hayBlackjack(cartasCasa) && !hayBlackjack(cartasJugador)) {
                ganador = "casa";
            }
            if (hayBlackjack(cartasJugador) && hayBlackjack(cartasCasa)) {
                ganador = "empate";
            }
            if (calcularPuntuacion(cartasJugador) > calcularPuntuacion(cartasCasa) && !sePaso(cartasJugador)) {
                ganador = nombreJugador;
            }
            if (calcularPuntuacion(cartasJugador) < calcularPuntuacion(cartasCasa) && !sePaso(cartasCasa)) {
                ganador = "casa";
            }
            if (calcularPuntuacion(cartasJugador) == calcularPuntuacion(cartasCasa)) {
                ganador = "empate";
            }

        }

        return ganador;
    }

    @Override
    public List<Carta> plantarse(List<Carta> cartasCasa) {
        List<Carta> manoFinalCrupier = new ArrayList<>();
        List<Carta> manoConteo = new ArrayList<>(cartasCasa);
        while (calcularPuntuacion(manoConteo) < 17) {
            Carta nuevaCarta = pedirCarta();
            manoConteo.add(nuevaCarta);
            manoFinalCrupier.add(nuevaCarta);
        }
        return manoFinalCrupier;
    }

    @Override
    public void reponerBaraja() {
        this.baraja = new Baraja(this.baraja.getValorAs());

    }

    @Override
    public void inicializarBaraja(Integer valorAs) {
        this.baraja = new Baraja(valorAs);

    }

    @Override
    public Baraja getBaraja() {
        return this.baraja;
    }

}
