package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Compra;
import com.tallerwebi.dominio.Juego;
import com.tallerwebi.dominio.Partida;
import com.tallerwebi.dominio.PartidaBingo;
import com.tallerwebi.dominio.RepositorioPartida;
import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.ServicioPlataforma;
import com.tallerwebi.dominio.excepcion.BingoBotEsNullException;
import com.tallerwebi.dominio.excepcion.NoHayCompras;
import com.tallerwebi.dominio.excepcion.NoHayComprasParaEseJuego;
import com.tallerwebi.dominio.excepcion.NoHayComprasParaEseUsuario;
import com.tallerwebi.dominio.excepcion.NoHayPartidasDeBingoException;
import com.tallerwebi.dominio.excepcion.PartidaConPuntajeNegativoException;
import com.tallerwebi.dominio.excepcion.PartidaDeUsuarioNoEncontradaException;
import com.tallerwebi.dominio.excepcion.PartidasDelJuegoNoEncontradasException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("servicioPlataforma")
public class ServicioPlataformaImpl implements ServicioPlataforma {
    private RepositorioPartida repositorioPartida;

    @Autowired
    public ServicioPlataformaImpl(RepositorioPartida repositorioPartida) {
        this.repositorioPartida = repositorioPartida;
    }

    @Override
    public List<Partida> generarRanking(Juego juego) throws PartidasDelJuegoNoEncontradasException {
        List<Partida> partidas = repositorioPartida.listarPartidasPorJuego(juego);
        if (partidas.isEmpty()) {
            throw new PartidasDelJuegoNoEncontradasException();
        }
        return partidas;
    }

    @Override
    public void agregarPartida(Partida partida) throws PartidaConPuntajeNegativoException, IllegalArgumentException, BingoBotEsNullException {
        this.repositorioPartida.guardar(partida);
    }

    @Override
    public List<Partida> obtenerPartidasUsuario(Long id, Juego juego) throws PartidaDeUsuarioNoEncontradaException {
        return repositorioPartida.obtenerPartidasUsuario(id, juego);
    }

    @Override
    public List<Partida> obtenerUltimasPartidasDelUsuario(Long id, Juego juego)
            throws PartidaDeUsuarioNoEncontradaException {
        return repositorioPartida.obtenerPartidasUsuarioPorFecha(id, juego);
    }

    @Override
    public List<PartidaBingo> generarRankingDePartidasDeBingo(Long userId) throws NoHayPartidasDeBingoException {
        return this.repositorioPartida.generarRankingDePartidasDeBingo(userId);
    }

    @Override
    public List<Compra> obtenerCompras(Long id, Juego juego)
            throws NoHayCompras {
        return this.repositorioPartida.obtenerCompras(id, juego);
    }

}
