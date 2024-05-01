package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.List;

public interface ServicioChin {

    //List<Carta>
    void repartirTodasLasCartas(ArrayList<Carta> mazoJugador1, ArrayList<Carta> mazoJugador2);
    List<Carta> repartirCuatroCartasDeFrente(Jugador jugador);
    boolean hayChin(ArrayList<Carta> descarte1, ArrayList<Carta> descarte2);
    Carta pedirUnaCartaExtra();
    void ponerCartaEnPilaDeDescarte(Carta carta, ArrayList<Carta> descarte1, ArrayList<Carta> descarte2);
    void guardarResultadoDePartida(Juego CHIN, Integer puntaje);
    void sacarDelMazoYPonerEnMano(ArrayList<Carta> mazoJugador1, ArrayList<Carta> manoJugador1);
}