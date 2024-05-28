package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Juego;
import com.tallerwebi.dominio.Partida;
import com.tallerwebi.dominio.RepositorioPartida;
import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.ServicioPlataforma;
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
    public void agregarPartida(Partida partida) {
        this.repositorioPartida.guardar(partida);
    }

    @Override
    public List<Partida> obtenerPartidasUsuario(Long id, Juego juego) throws PartidaDeUsuarioNoEncontradaException {
        return repositorioPartida.obtenerPartidasUsuario(id, juego);
    }

    @Override
    public List<Partida> obtenerUltimasPartidasDelUsuario(Long id, Juego juego) throws PartidaDeUsuarioNoEncontradaException {
        return repositorioPartida.obtenerPartidasUsuarioPorFecha(id, juego);
    }

}
