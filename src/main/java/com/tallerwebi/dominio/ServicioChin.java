package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.List;

public interface ServicioChin {

    void repartirTodasLasCartas(ArrayList<Carta> mazoJugador1, ArrayList<Carta> mazoJugador2);
    List<Carta> repartirCuatroCartasDeFrente(ArrayList<Carta> mazoJugador1, ArrayList<Carta> manoJugador1);
    boolean hayChin(ArrayList<Carta> descarte1, ArrayList<Carta> descarte2);
    Carta pedirUnaCartaExtra();
    void ponerCartaEnPilaDeDescarte(Carta carta, ArrayList<Carta> descarte1, ArrayList<Carta> descarte2);
    void guardarResultadoDePartida(Juego CHIN, Integer puntaje);
    void sacarDelMazoYPonerEnMano(ArrayList<Carta> mazoJugador1, ArrayList<Carta> manoJugador1);
    boolean sePuedenAgregarCartasAlDescarte(ArrayList<Carta> descarte1, ArrayList<Carta> descarte2, ArrayList<Carta> manoJugador1, ArrayList<Carta> manoJugador2);

    Boolean chequearGanador(ArrayList<Carta> mazoJugador1);
}
