package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;

import java.util.Arrays;
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
import com.tallerwebi.infraestructura.ServicioPlataformaImpl;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateTestConfig.class })
public class PlataformaTest {

    @Autowired
    SessionFactory session;

    RepositorioPartida repositorio;
    ServicioPlataforma servicio;

    @BeforeEach
    public void init() {
        repositorio = mock(RepositorioPartida.class);
        servicio = new ServicioPlataformaImpl(repositorio);
    }

    @Test
    public void QueSePuedaObtenerUnRankingDeUnJuego() throws PartidasDelJuegoNoEncontradasException {
        when(repositorio.listarPartidasPorJuego(Juego.BINGO))
                .thenReturn(Arrays.asList(mock(Partida.class), mock(Partida.class)));

        List<Partida> obtenidas = servicio.generarRanking(Juego.BINGO);

        assertThat(obtenidas, notNullValue());
        assertThat(obtenidas.size(), equalTo(2));
    }

    @Test
    public void QueSeAlNoHaberPartidasDeUnJuegoSeLanzeUnaExceptionAlGenerarElRanking()
            throws PartidasDelJuegoNoEncontradasException {
        when(repositorio.listarPartidasPorJuego(Juego.BINGO))
                .thenThrow(PartidasDelJuegoNoEncontradasException.class);

        try {
            servicio.generarRanking(Juego.BINGO);
        } catch (PartidasDelJuegoNoEncontradasException e) {
            assertThat(e, notNullValue());
        }
    }

    @Test
    public void QueSeAlNoHaberPartidasDeUnJugadorSeLanzeUnaException() throws PartidaDeUsuarioNoEncontradaException {
        when(repositorio.obtenerPartidasUsuario("usuario inexistente", Juego.BINGO))
                .thenThrow(PartidaDeUsuarioNoEncontradaException.class);

        try {
            servicio.obtenerPartidasUsuario("usuario inexistente", Juego.BINGO);
        } catch (PartidaDeUsuarioNoEncontradaException e) {
            assertThat(e, notNullValue());
        }
    }

    @Test
    public void QueSeAlNoHaberPartidasAnterioresJugadorSeLanzeUnaException() throws PartidaDeUsuarioNoEncontradaException {
        when(repositorio.obtenerPartidasUsuarioPorFecha("usuario", Juego.BINGO))
                .thenThrow(PartidaDeUsuarioNoEncontradaException.class);

        try {
            servicio.obtenerPartidasUsuario("usuario", Juego.BINGO);
        } catch (PartidaDeUsuarioNoEncontradaException e) {
            assertThat(e, notNullValue());
        }
    }
}
