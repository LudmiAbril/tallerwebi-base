package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioPlataforma {

    List<Partida> generarRanking(Juego juego);
    void agregarPartida(Partida partida);

}
