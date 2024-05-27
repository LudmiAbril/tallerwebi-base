package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Carta;
import com.tallerwebi.dominio.Juego;
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.Palo;
import com.tallerwebi.dominio.Partida;
import com.tallerwebi.dominio.ServicioBlackjack;
import com.tallerwebi.dominio.ServicioPlataforma;

public class ControladorBlackjackTest {
    private ControladorBlackjack controladorBlackjack;
    private MockHttpSession session;
    @Mock
    private ServicioBlackjack servicioBlackjackMock;
    @Mock
    private ServicioPlataforma servicioPlataformaMock;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.controladorBlackjack = new ControladorBlackjack(servicioBlackjackMock, servicioPlataformaMock);
        this.session = new MockHttpSession();
    }

    @Test
    public void queSeDevuelvaLaVistaInicialBlackjackYObjetoJugadorVacio() {

        ModelAndView modelAndView = controladorBlackjack.inicioBlackjack();
        String viewname = modelAndView.getViewName();

        assertThat(viewname, equalToIgnoringCase("irAlBlackjack"));
        assertThat(modelAndView.getModel().get("nuevoJugador"), instanceOf(Jugador.class));
        assertThat(((Jugador) modelAndView.getModel().get("nuevoJugador")).getNombre(), nullValue());
    }

    @Test
    public void queSeAlIniciarseElJuegoSeRepartanDosCartasAlJugadorYalCrupier() {
        // Preparación
        Carta ca = new Carta("A", 11, Palo.CORAZON);
        Carta cb = new Carta("3", 3, Palo.CORAZON);
        Carta cc = new Carta("6", 6, Palo.DIAMANTE);
        Carta cd = new Carta("9", 9, Palo.TREBOL);

        List<Carta> manoJugadorEsperada = new ArrayList<>();
        manoJugadorEsperada.add(cc);
        manoJugadorEsperada.add(ca);

        List<Carta> manoCrupierEsperada = new ArrayList<>();
        manoCrupierEsperada.add(cd);
        manoCrupierEsperada.add(cb);

        when(servicioBlackjackMock.entregarCartasPrincipales())
                .thenReturn(manoJugadorEsperada)
                .thenReturn(manoCrupierEsperada);

        // Ejecución
        controladorBlackjack.comenzarBlackjack(session, false, 0);

        // Verificación
        List<Carta> manoJugadorObtenida = (List<Carta>) session.getAttribute("cartasJugador");
        List<Carta> manoCrupierObtenida = (List<Carta>) session.getAttribute("cartasCasa");

        assertNotNull(manoJugadorObtenida);
        assertNotNull(manoCrupierObtenida);
        assertThat(manoJugadorObtenida, equalTo(manoJugadorEsperada));
        assertThat(manoCrupierObtenida, equalTo(manoCrupierEsperada));
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

        // ejecucion- le paso la sesion con todo los datos nuevos.Actualiza la sesion y
        // retorna un mapa
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
        controladorBlackjack.comenzarBlackjack(session, false, 0);

        // ejecucion
        Map<String, Object> datosSalida = controladorBlackjack.plantarse(session);
        List<Carta> manoFinalCRupier = (List<Carta>) datosSalida.get("manoFinalCrupier");

        assertThat(manoFinalCRupier.size(), greaterThan(2));

    }

}