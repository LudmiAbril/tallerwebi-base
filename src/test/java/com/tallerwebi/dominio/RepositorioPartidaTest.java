package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;

import javax.transaction.Transactional;

import com.tallerwebi.dominio.excepcion.BingoBotEsNullException;
import com.tallerwebi.dominio.excepcion.NoHayPartidasDeBingoException;
import com.tallerwebi.dominio.excepcion.PartidaConPuntajeNegativoException;

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

    // crear instancias de la clase Partida
    private Partida crearPartida(String nombre, Juego juego) {
        Partida partida = new Partida(nombre, juego);
        return partida;
    }

    private Partida crearPartida(long idJugador, Juego juego) {
        Partida partida = new Partida(idJugador, juego);
        return partida;
    }

    @Test
    public void queSeGuardeUnaPartida() throws PartidaConPuntajeNegativoException, IllegalArgumentException, BingoBotEsNullException {
        Partida p = crearPartida("jugador", Juego.BINGO);
        repositorio.guardar(p);
        assertThat(session.getCurrentSession().contains(p), equalTo(true));
    }

    @Test
    public void queSeObtengaUnRankingOrdenadoDePartidasParaElBingo()
            throws PartidasDelJuegoNoEncontradasException, PartidaConPuntajeNegativoException,
            IllegalArgumentException, BingoBotEsNullException {
        Long idJugador = 2L;
        Juego juego = Juego.BINGO;
        Set<Integer> casillerosMarcados = new HashSet<Integer>();
        casillerosMarcados.add(1);
        casillerosMarcados.add(3);
        casillerosMarcados.add(5);
        Boolean seHizoLinea = true;
        Boolean seHizoBingo = false;
        TipoPartidaBingo tipoPartidaBingo = TipoPartidaBingo.BINGO;
        Integer tirada = 50;
        Integer cantidadDeCasillerosMarcados = casillerosMarcados.size();

        Partida p1 = new PartidaBingo(idJugador, juego, casillerosMarcados, seHizoLinea, seHizoBingo, tipoPartidaBingo,
                tirada, cantidadDeCasillerosMarcados, false);

        casillerosMarcados.add(7);
        casillerosMarcados.add(6);
        // PARTIDA DOS
        Partida p2 = new PartidaBingo(idJugador, Juego.BINGO, casillerosMarcados, false, true, TipoPartidaBingo.BINGO,
                25, 9, false);

        List<Partida> partidasEsperadas = new ArrayList<Partida>();
        partidasEsperadas.add(p2);
        partidasEsperadas.add(p1);

        repositorio.guardar(p2);
        repositorio.guardar(p1);

        List<Partida> partidasObtenidas = new ArrayList<Partida>();

        try {
            partidasObtenidas.addAll(repositorio.generarRankingDePartidasDeBingo(idJugador));
        } catch (NoHayPartidasDeBingoException e) {

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
    public void queSePuedanObtenerLasPartidasDeUnJugador() throws PartidaConPuntajeNegativoException,
            IllegalArgumentException, BingoBotEsNullException {
        Long usuarioId = 000L;

        Partida partida1 = crearPartida("Prueba", Juego.BLACKJACK);
        partida1.setIdJugador(usuarioId);
        repositorio.guardar(partida1);

        Partida partida2 = crearPartida("Prueba", Juego.BLACKJACK);
        partida2.setIdJugador(usuarioId);
        repositorio.guardar(partida2);

        List<Partida> partidasObtenidas = new ArrayList<Partida>();
        try {
            partidasObtenidas = repositorio.obtenerPartidasUsuario((long) 000, Juego.BLACKJACK);
        } catch (PartidaDeUsuarioNoEncontradaException e) {
        }
        assertThat(partidasObtenidas.size(), equalTo(2));
    }

    @Test
    public void queAlNoEncontrarPartidasParaUnJugadorSeLanzeUnaException() {
        assertThrows(PartidaDeUsuarioNoEncontradaException.class, () -> {
            repositorio.obtenerPartidasUsuario((long) 000, Juego.AHORCADO);
        });

    }

    @Test
    public void queSePuedanObtenerLasUltimasPartidasOrdenadasPorFecha() throws PartidaConPuntajeNegativoException,
            IllegalArgumentException, BingoBotEsNullException {
        Long usuarioId = 000L;
        List<Partida> partidasEsperadas = new ArrayList<Partida>();
        Partida a = crearPartida("user", Juego.BINGO);
        a.setIdJugador(usuarioId);

        Partida b = crearPartida("user", Juego.BINGO);
        b.setIdJugador(usuarioId);
        Partida c = crearPartida("user", Juego.BINGO);
        c.setIdJugador(usuarioId);

        partidasEsperadas.add(c);
        partidasEsperadas.add(b);
        partidasEsperadas.add(a);

        repositorio.guardar(a);
        repositorio.guardar(b);
        repositorio.guardar(c);

        List<Partida> partidasObtenidas = new ArrayList<Partida>();
        try {
            partidasObtenidas.addAll(repositorio.obtenerPartidasUsuarioPorFecha((long) 000, Juego.BINGO));
        } catch (PartidaDeUsuarioNoEncontradaException e) {

        }

        assertThat(partidasObtenidas, containsInAnyOrder(partidasEsperadas.toArray()));
    }

    @Test
    public void queLanceUnaExceptionAlObtenerPartidasDeUsuarioConIdNulo() {
        assertThrows(PartidaDeUsuarioNoEncontradaException.class, () -> {
            repositorio.obtenerPartidasUsuario(null, Juego.BINGO);
        });
    }

    @Test
    public void queLanceUnaExceptionAlObtenerPartidasDeUsuarioConJuegoNulo() {
        assertThrows(PartidaDeUsuarioNoEncontradaException.class, () -> {
            repositorio.obtenerPartidasUsuario(000L, null);
        });
    }

    @Test
    public void queLanceUnaExceptionAlObtenerRankingParaJuegoSinPartidas() {
        assertThrows(PartidasDelJuegoNoEncontradasException.class, () -> {
            repositorio.listarPartidasPorJuego(Juego.BLACKJACK);
        });
    }

    @Test
    public void queLanceUnaExceptionAlGuardarPartidaConJuegoNulo() {
        Partida partida = new Partida("jugador", null);
        assertThrows(IllegalArgumentException.class, () -> {
            repositorio.guardar(partida);
        });
    }

    @Test
    public void queLanceExceptionAlObtenerPartidasDeUsuarioInexistente() {
        assertThrows(PartidaDeUsuarioNoEncontradaException.class, () -> {
            repositorio.obtenerPartidasUsuario(999L, Juego.BINGO);
        });
    }

    @Test
    public void queLanceUnaExceptionAlGuardarPartidaConPuntuacionNegativa() {

        Partida partida = new PartidaBlackJack(1L, -5, Juego.BLACKJACK, true, true, LocalTime.of(3, 00));
        assertThrows(PartidaConPuntajeNegativoException.class, () -> {
            repositorio.guardar(partida);
        });
    }

    @Test
    public void queLanceExceptionAlGuardarPartidaNula() {
        assertThrows(IllegalArgumentException.class, () -> {
            repositorio.guardar(null);
        });
    }

    @Test
    public void queLanceExceptionDePartidasNoEncontradasAlListarPartidasPorJuegoConJuegoNulo() {
        assertThrows(PartidasDelJuegoNoEncontradasException.class, () -> {
            repositorio.listarPartidasPorJuego(null);
        });
    }

    @Test
    public void queSeObtenganPartidasPorRangoDeFechas()
            throws PartidaConPuntajeNegativoException, PartidaDeUsuarioNoEncontradaException, IllegalArgumentException, BingoBotEsNullException {
        Long usuarioId = 000L;
        Juego juego = Juego.BINGO;
        Partida partida1 = crearPartida("jugador1", juego);
        partida1.setFechaYhora(LocalDateTime.of(2022, 1, 1, 0, 0));
        partida1.setIdJugador(usuarioId);
        repositorio.guardar(partida1);

        Partida partida2 = crearPartida("jugador2", juego);
        partida2.setFechaYhora(LocalDateTime.of(2023, 1, 1, 0, 0));
        partida2.setIdJugador(usuarioId);
        repositorio.guardar(partida2);

        List<Partida> partidas = repositorio.obtenerPartidasPorFechaRango(usuarioId, juego,
                LocalDateTime.of(2021, 1, 1, 0, 0), LocalDateTime.of(2022, 12, 31, 23, 59));
        assertThat(partidas.size(), equalTo(1));
        assertThat(partidas.get(0).getIdJugador(), equalTo(usuarioId));
        assertThat(partidas.get(0).getFechaYhora(), equalTo(LocalDateTime.of(2022, 1, 1, 0, 0)));
    }

    @Test
    public void queSeGuardeUnaPartidaSenku() throws PartidaConPuntajeNegativoException, IllegalArgumentException, BingoBotEsNullException {
        // GIVEN
        Long idJugador = 1L;
        Juego juego = Juego.SENKU;
        Boolean ganado = true;
        Integer cantidadMovimientos = 50;
        Partida partidaSenku = new PartidaSenku(idJugador, juego, ganado, cantidadMovimientos);
        // WHEN
        repositorio.guardar(partidaSenku);
        // THEN
        assertThat(session.getCurrentSession().contains(partidaSenku), equalTo(true));
    }

    @Test
    public void queLanceExceptionAlGuardarPartidaSenkuConValoresNoValidos() {
        // GIVEN
        PartidaSenku partidaSenku = new PartidaSenku();
        // VALORES NO VALIODS(SOLO SE GUARDA SI SE GANO Y SI HUBO MOVIMEINTOS)
        partidaSenku.setGanado(null);
        partidaSenku.setCantidadMovimientos(-1);
        // THEN
        assertThrows(IllegalArgumentException.class, () -> {
            repositorio.guardar(partidaSenku);
        });
    }

    @Test
    public void queSeGuardeUnaPartidaBingoDeBot() throws IllegalArgumentException, PartidaConPuntajeNegativoException, BingoBotEsNullException {
        Long idJugador = 2L;
        Juego juego = Juego.BINGO;
        Set<Integer> casillerosMarcados = new HashSet<Integer>();
        casillerosMarcados.add(1);
        casillerosMarcados.add(3);
        casillerosMarcados.add(5);
        TipoPartidaBingo tipoPartidaBingo = TipoPartidaBingo.BINGO;
        Integer tirada = 50;
        Integer cantidadDeCasillerosMarcados = casillerosMarcados.size();

        Partida p1 = new PartidaBingo(idJugador, juego, casillerosMarcados, false, false, tipoPartidaBingo,
                tirada, cantidadDeCasillerosMarcados, true);

        List<Partida> partidasEsperadas = new ArrayList<Partida>();

        partidasEsperadas.add(p1);

        repositorio.guardar(p1);

        List<Partida> partidasObtenidas = new ArrayList<Partida>();

        try {
            partidasObtenidas.addAll(repositorio.generarRankingDePartidasDeBingo(idJugador));
        } catch (NoHayPartidasDeBingoException e) {

        }
        assertNotNull(partidasObtenidas);
        assertThat(partidasObtenidas, equalTo(partidasEsperadas));
    }

    @Test
    public void queSeLanzeLaExcepcionAlIntentarGuardarUnaPartidaBingoDeBotSinQueElBotHayaHechoBingo() {
        Long idJugador = 2L;
        Juego juego = Juego.BINGO;
        Set<Integer> casillerosMarcados = new HashSet<Integer>();
        casillerosMarcados.add(1);
        casillerosMarcados.add(3);
        casillerosMarcados.add(5);
        TipoPartidaBingo tipoPartidaBingo = TipoPartidaBingo.BINGO;
        Integer tirada = 50;
        Integer cantidadDeCasillerosMarcados = casillerosMarcados.size();

        Partida p1 = new PartidaBingo(idJugador, juego, casillerosMarcados, false, false, tipoPartidaBingo,
                tirada, cantidadDeCasillerosMarcados, null);

        assertThrows(BingoBotEsNullException.class, () -> {
            repositorio.guardar(p1);
        });
    }
}
