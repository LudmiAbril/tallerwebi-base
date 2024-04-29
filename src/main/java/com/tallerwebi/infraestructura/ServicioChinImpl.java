package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("servicioChin")
public class ServicioChinImpl implements ServicioChin {
    private Baraja baraja;

    @Autowired
    public ServicioChinImpl(){
        this.baraja= new Baraja();
    }

    @Override
    public List<Carta> repartirTodasLasCartas() {
        return List.of();
    }

    @Override
    public List<Carta> repartirCuatroCartasDeFrente(Jugador jugador) {
        return List.of();
    }

    @Override
    public boolean hayChin(ArrayList descarte1, ArrayList descarte2) {
        return false;
    }

    @Override
    public Carta pedirUnaCartaExtra() {
        return null;
    }

    @Override
    public void guardarResultadoDePartida(Juego CHIN, Integer puntaje) {

    }
}
