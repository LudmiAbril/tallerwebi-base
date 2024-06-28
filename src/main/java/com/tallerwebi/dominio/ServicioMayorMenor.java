package com.tallerwebi.dominio;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public interface ServicioMayorMenor {


    Carta entregarCartaDelMedio();
    void inicializarBaraja();
    void barajar();
    Carta sacarCarta();
    void comenzarPartida();
    boolean evaluarCarta(Carta actual, Carta nueva, Valor valor);
    void actualizarEstadoPartida(HttpSession session, Carta nueva, Boolean evaluada);
    void reiniciarJuego(HttpSession session);
    Integer cantidadCartasRestantesEnMazo();
    Baraja getBaraja();
    Integer getPuntaje();
}
