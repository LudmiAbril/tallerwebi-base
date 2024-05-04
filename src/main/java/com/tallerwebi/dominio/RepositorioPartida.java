package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioPartida {
    void guardar(Partida partida);
    List<Partida> listarPartidasPorJuego(Juego juego);
}
