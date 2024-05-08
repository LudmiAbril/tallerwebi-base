package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Juego;
import com.tallerwebi.dominio.Partida;
import com.tallerwebi.dominio.RepositorioPartida;
import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.ServicioPlataforma;

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
    public List<Partida> generarRanking(Juego juego) {
        return List.of();
    }

    @Override
    public void agregarPartida(Partida partida) {
        this.repositorioPartida.guardar(partida);
    }
}
