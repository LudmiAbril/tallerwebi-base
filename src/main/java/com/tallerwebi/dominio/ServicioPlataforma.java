package com.tallerwebi.dominio;

import java.util.List;

import com.tallerwebi.dominio.excepcion.NoHayPartidasDeBingoException;
import com.tallerwebi.dominio.excepcion.PartidaConPuntajeNegativoException;
import com.tallerwebi.dominio.excepcion.PartidaDeUsuarioNoEncontradaException;
import com.tallerwebi.dominio.excepcion.PartidasDelJuegoNoEncontradasException;

public interface ServicioPlataforma {

    List<Partida> generarRanking(Juego juego) throws PartidasDelJuegoNoEncontradasException;

    void agregarPartida(Partida partida) throws PartidaConPuntajeNegativoException, IllegalArgumentException;

    List<Partida> obtenerPartidasUsuario(Long id, Juego juego) throws PartidaDeUsuarioNoEncontradaException;

    List<Partida> obtenerUltimasPartidasDelUsuario(Long id, Juego bingo) throws PartidaDeUsuarioNoEncontradaException;

    List<PartidaBingo> generarRankingDePartidasDeBingo(Long userId) throws NoHayPartidasDeBingoException;

}
