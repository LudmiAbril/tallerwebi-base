package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Juego;
import com.tallerwebi.dominio.Partida;
import com.tallerwebi.dominio.ServicioPlataforma;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("servicioPlataforma")
public class ServicioPlataformaImpl implements ServicioPlataforma {
    @Override
    public List<Partida> generarRanking(Juego juego) {
        return List.of();
    }

    @Override
    public void agregarPartida(Partida partida) {

    }
}
