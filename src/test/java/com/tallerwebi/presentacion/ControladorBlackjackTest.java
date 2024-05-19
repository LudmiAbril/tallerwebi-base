package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Carta;
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.Palo;
import com.tallerwebi.dominio.ServicioBlackjack;
import com.tallerwebi.dominio.ServicioPlataforma;

public class ControladorBlackjackTest {
    private ServicioBlackjack servicioBlackjackMock;
    private ServicioPlataforma servicioPlataformaMock;
    private ControladorBlackjack controladorBlackjack;
    private HttpSession session;

    @BeforeEach
    public void init() {
        this.servicioBlackjackMock = mock(ServicioBlackjack.class);
        this.servicioPlataformaMock = mock(ServicioPlataforma.class);
        this.controladorBlackjack = new ControladorBlackjack(servicioBlackjackMock, servicioPlataformaMock);
        this.session = new MockHttpSession();
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
    public void queSeAlIniciarseElJuegoSeRepartanDosCartasAlJugadorYalCrupier() {
        // preparacion
        when(servicioBlackjackMock.entregarCartasPrincipales())
                .thenReturn(Arrays.asList(mock(Carta.class), mock(Carta.class)));

        // ejecucion
        controladorBlackjack.comenzarBlackjack(mock(Jugador.class), session);
        List<Carta> cartasJugador = (List<Carta>) session.getAttribute("cartasJugador");
        List<Carta> cartasCasa = (List<Carta>) session.getAttribute("cartasCasa");

        // validacion
        assertThat(cartasJugador.size(), equalTo(2));
        assertThat(cartasCasa.size(), equalTo(2));
    }

    @Test
    public void queSePuedaPedirUnaCarta() {
        // preparacion
        Carta cartaEsperada = new Carta("2", 2, Palo.DIAMANTE);
        when(servicioBlackjackMock.pedirCarta())
                .thenReturn(cartaEsperada);

        when(servicioBlackjackMock.entregarCartasPrincipales())
                .thenReturn(Arrays.asList(mock(Carta.class), mock(Carta.class)));

        List<Carta> cartasJugador = servicioBlackjackMock.entregarCartasPrincipales();
        List<Carta> cartasCasa = servicioBlackjackMock.entregarCartasPrincipales();
        session.setAttribute("cartasJugador", cartasJugador);
        session.setAttribute("cartasCasa", cartasCasa);
        session.setAttribute("jugadorActual", "jugador");

        // ejecucion
        Map<String, Object> datosSalida = controladorBlackjack.pedirCarta(session);

        // validacion
        assertThat(datosSalida.get("cartaNueva"), equalTo(cartaEsperada));
    }

    @Test
    public void QueAlPlantarseSeActualizeElMazoDelCrupier() {
        // preparacion 
        Carta carta = new Carta("2", 2, Palo.CORAZON);
        List<Carta> mano = new ArrayList<>();
        mano.add(carta);
        mano.add(carta);
        List<Carta> manoFinal = new ArrayList<>();
        manoFinal.add(carta);
        manoFinal.add(carta);
        manoFinal.add(carta);
        when(servicioBlackjackMock.entregarCartasPrincipales())
                .thenReturn(Arrays.asList(carta, carta));
        when(servicioBlackjackMock.plantarse(mano)).thenReturn(manoFinal);
        session.setAttribute("jugadorActual", "jugador");
        controladorBlackjack.comenzarBlackjack(mock(Jugador.class), session);

        // ejecucion
        Map<String, Object> datosSalida = controladorBlackjack.plantarse(session);
        List<Carta> manoFinalCRupier = (List<Carta>) datosSalida.get("manoFinalCrupier");

        assertThat(manoFinalCRupier.size(), greaterThan(2));

    }

}