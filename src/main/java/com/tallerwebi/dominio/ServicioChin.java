package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.List;

public interface ServicioChin {

    List<Carta> repartirTodasLasCartas();
    List<Carta> repartirCuatroCartasDeFrente(Jugador jugador);
    boolean hayChin(ArrayList descarte1, ArrayList descarte2);
    Carta pedirUnaCartaExtra();
    void guardarResultadoDePartida(Juego CHIN, Integer puntaje);
}
