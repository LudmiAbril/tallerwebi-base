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
        return repositorioPartida.listarPartidasPorJuego(juego);
    }

    @Override
    public void agregarPartida(Partida partida) {
        this.repositorioPartida.guardar(partida);
    }

    @Override
    public List<Partida> obtenerPartidasUsuario(String nombre, Juego juego) throws PartidaDeUsuarioNoEncontradaException {
        return repositorioPartida.obtenerPartidasUsuario(nombre, juego);
    }

    @Override
    public List<Partida> obtenerUltimasPartidasDelUsuario(String nombre, Juego juego) throws PartidaDeUsuarioNoEncontradaException {
        return repositorioPartida.obtenerPartidasUsuarioPorFecha(nombre, juego);
    }

    
}
