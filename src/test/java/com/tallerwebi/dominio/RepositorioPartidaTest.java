package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.tallerwebi.config.HibernateTestConfig;
import com.tallerwebi.dominio.excepcion.PartidaDeUsuarioNoEncontradaException;
import com.tallerwebi.dominio.excepcion.PartidasDelJuegoNoEncontradasException;
import com.tallerwebi.infraestructura.RepositorioPartidaImpl;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateTestConfig.class })
public class RepositorioPartidaTest {
    private RepositorioPartida repositorio;
    @Autowired
    SessionFactory session;

    @BeforeEach
    public void init() {
        this.repositorio = new RepositorioPartidaImpl(session) {
        };
    }

    @Test
    public void queSeGuardeUnaPartida() {
        Partida p = crearPartida("jugador", Juego.BINGO);
        repositorio.guardar(p);
        assertThat(session.getCurrentSession().contains(p), equalTo(true));
    }

    @Test
    public void queSeObtengaUnRankingOrdenadoDePartidasParaUnJuegoParticular() {
        Partida p1 = new Partida("a", 25, Juego.BINGO);
        Partida p2 = new Partida("b", 24, Juego.BINGO);
        Partida p3 = new Partida("c", 23, Juego.BINGO);

        List<Partida> partidasEsperadas = new ArrayList<Partida>();
        partidasEsperadas.add(p1);
        partidasEsperadas.add(p2);
        partidasEsperadas.add(p3);

        // ejec desordenada para verificar orden
        repositorio.guardar(p2);
        repositorio.guardar(p1);
        repositorio.guardar(p3);

        List<Partida> partidasObtenidas = new ArrayList<Partida>();

        try {
            partidasObtenidas.addAll(repositorio.listarPartidasPorJuego(Juego.BINGO));
        } catch (PartidasDelJuegoNoEncontradasException e) {

        }

        assertNotNull(partidasObtenidas);
        assertThat(partidasObtenidas, equalTo(partidasEsperadas));
    }

    @Test
    public void queSeLanzeUnaExceptionSiNoHayPartidasDeEseJuego() {
        assertThrows(PartidasDelJuegoNoEncontradasException.class, () -> {
            repositorio.listarPartidasPorJuego(Juego.BINGO);
        });
    }

    @Test
    public void queObtenganLasPartidasDeUnJugador() {

        repositorio.guardar(crearPartida("Prueba", Juego.BLACKJACK));
        repositorio.guardar(crearPartida("Prueba", Juego.BLACKJACK));

        List<Partida> partidasObtenidas = new ArrayList<Partida>();
        try {
            partidasObtenidas = repositorio.obtenerPartidasUsuario("Prueba", Juego.BLACKJACK);
        } catch (PartidaDeUsuarioNoEncontradaException e) {
        }
        assertThat(partidasObtenidas.size(), equalTo(2));
    }

    @Test
    public void queAlNoEncontrarPartidasParaUnJugadorSeLanzeUnaException() {
        assertThrows(PartidaDeUsuarioNoEncontradaException.class, () -> {
            repositorio.obtenerPartidasUsuario("usuario a", Juego.AHORCADO);
        });

    }

    @Test
    public void queSePuedanObtenerLasUltimasPartidasOrdenadasPorFecha() {
        List<Partida> partidasEsperadas = new ArrayList<Partida>();
        Partida a = crearPartida("user", Juego.BINGO);
        Partida b = crearPartida("user", Juego.BINGO);
        Partida c = crearPartida("user", Juego.BINGO);

        partidasEsperadas.add(c);
        partidasEsperadas.add(b);
        partidasEsperadas.add(a);

        repositorio.guardar(a);
        repositorio.guardar(b);
        repositorio.guardar(c);

        List<Partida> partidasObtenidas = new ArrayList<Partida>();
        try {
            partidasObtenidas.addAll(repositorio.obtenerPartidasUsuarioPorFecha("user", Juego.BINGO));
        } catch (PartidaDeUsuarioNoEncontradaException e) {

        }

        assertThat(partidasObtenidas, containsInAnyOrder(partidasEsperadas.toArray()));
    }

    private Partida crearPartida(String nombre, Juego juego) {
        Partida partida = new Partida(nombre, 23, juego);
        return partida;
    }
}