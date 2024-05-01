package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

public class PlataformaTest {

    @Test
    public void queSePuedaCrearUnaPlataformaDeJuegos() {
        // - Array <Juego> : partidas
        // + generarRanking(Juego) : Array<Juego>

        Plataforma nuevaPlataforma = new Plataforma();
        assertNotNull(nuevaPlataforma);
    }

    @Test
    public void queSePuedanAgregarUnaPartida() {
        Plataforma nuevaPlataforma = new Plataforma();
        Jugador jugador = new Jugador();
        Jugador jugador2 = new Jugador();
        jugador.setNombre("Mica");
        jugador2.setNombre("Cele");
        Partida multijugador = new PartidaMultijugador(Juego.CHIN, jugador, jugador2);
    }

}