package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Carta;
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.ServicioBlackjack;
import com.tallerwebi.dominio.ServicioPlataforma;

public class ControladorBlackjackTest {
    private ServicioBlackjack servicioBlackjackMock;
    private ServicioPlataforma servicioPlataformaMock;
    private ControladorBlackjack controladorBlackjack;

    @BeforeEach
    public void init() {
        this.servicioBlackjackMock = mock(ServicioBlackjack.class);
        this.servicioPlataformaMock = mock(ServicioPlataforma.class);
        this.controladorBlackjack = new ControladorBlackjack(servicioBlackjackMock, servicioPlataformaMock);
    }

    @Test
    public void queSeDevuelvaLaVistaInicialBlackjackYObjetoJugadorVacio() {

        ModelAndView modelAndView = controladorBlackjack.inicioBlackjack();
        String viewname = modelAndView.getViewName();

        assertThat(viewname, equalToIgnoringCase("inicio-blackjack"));
        assertThat(modelAndView.getModel().get("nuevoJugador"), instanceOf(Jugador.class));
        assertThat(((Jugador) modelAndView.getModel().get("nuevoJugador")).getNombre(), nullValue());
    }

    @Test
    public void queSeAlIniciarseElJuegoSeRepartanDosCartas() {
        // preparacion
        MockHttpSession session = new MockHttpSession();
        when(servicioBlackjackMock.entregarCartasPrincipales())
                .thenReturn(Arrays.asList(mock(Carta.class), mock(Carta.class)));

        // ejec
        controladorBlackjack.comenzarBlackjack(mock(Jugador.class), session);
        List<Carta> cartasJugador = (List<Carta>) session.getAttribute("cartasJugador");

        // validacion
        assertThat(cartasJugador.size(), equalTo(2));
    }
}