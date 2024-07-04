package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorMayorMenorTest {
    private ControladorMayorMenor controladorMayorMenor;
    private MockHttpSession session;
    @Mock
    private ServicioMayorMenor servicioMayorMenorMock;
    @Mock
    private ServicioPlataforma servicioPlataformaMock;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.controladorMayorMenor = new ControladorMayorMenor(servicioMayorMenorMock, servicioPlataformaMock);
        this.session = new MockHttpSession();
        Usuario jugador = mock(Usuario.class);
        ConfiguracionesJuego configMock = mock(ConfiguracionesJuego.class);
        jugador.setConfig(configMock);
        jugador.setNombre("nombre");
        jugador.setId((long) 1);
        session.setAttribute("jugadorActual", jugador);
    }


    @Test
    public void queSeDevuelvaLaVistaInicialDeMayorMenor() {
        Usuario jugador = new Usuario();
        session.setAttribute("jugadorActual", jugador);
        ModelAndView modelAndView = controladorMayorMenor.inicioMayorMenor(session);
        String viewname = modelAndView.getViewName();

        assertThat(viewname, equalToIgnoringCase("irAMayorMenor"));
    }
    /*@Test
    public void queSeDevuelvaLaVistaDelJuego() {
        ModelAndView mav = controladorMayorMenor.comenzarMayorMenor(session);
        assertThat(mav.getViewName(), equalTo("MayorMenor"));
    }*/

    @Test
    public void queAlIniciarseDevuelvaUnaCartaEnElMedio() {
        Carta cartaInicial = new Carta("6", 6, Palo.DIAMANTE);
        List<Carta> cartaEsperada = new ArrayList<>();
        cartaEsperada.add(cartaInicial);
        when(servicioMayorMenorMock.entregarCartaDelMedio()).thenReturn(cartaInicial);
        assertThat(cartaInicial, is(notNullValue()));
        assertThat(cartaEsperada.size(), is(1));

    }



}
