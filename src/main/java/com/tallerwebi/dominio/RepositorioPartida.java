package com.tallerwebi.dominio;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import com.tallerwebi.dominio.excepcion.BingoBotEsNullException;
import com.tallerwebi.dominio.excepcion.NoHayPartidasDeBingoException;
import com.tallerwebi.dominio.excepcion.PartidaConPuntajeNegativoException;
import com.tallerwebi.dominio.excepcion.PartidaDeUsuarioNoEncontradaException;
import com.tallerwebi.dominio.excepcion.PartidasDelJuegoNoEncontradasException;

public interface RepositorioPartida {
    void guardar(Partida partida) throws PartidaConPuntajeNegativoException, IllegalArgumentException, BingoBotEsNullException;

    List<Partida> listarPartidasPorJuego(Juego juego) throws PartidasDelJuegoNoEncontradasException;

    List<Partida> obtenerPartidasUsuario(Long id, Juego juego)
            throws PartidaDeUsuarioNoEncontradaException;

    List<Partida> obtenerPartidasUsuarioPorFecha(Long id, Juego juego) throws PartidaDeUsuarioNoEncontradaException;

    List<PartidaBingo> generarRankingDePartidasDeBingo(Long userId) throws NoHayPartidasDeBingoException;

    List<Partida> obtenerPartidasPorFechaRango(Long usuarioId, Juego juego, LocalDateTime of, LocalDateTime of2) throws PartidaDeUsuarioNoEncontradaException;
}
