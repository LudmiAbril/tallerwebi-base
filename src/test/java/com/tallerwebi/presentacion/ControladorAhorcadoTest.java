package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.tallerwebi.dominio.ServicioAhorcado;
import com.tallerwebi.dominio.Usuario;

import static org.mockito.Mockito.*;

public class ControladorAhorcadoTest {

    private ControladorAhorcado controlador;
    private ServicioAhorcado servicioAhorcado;
    private HttpSession session;

    @BeforeEach
    public void init() {
        this.servicioAhorcado = mock(ServicioAhorcado.class);
        this.controlador = new ControladorAhorcado(servicioAhorcado);
        this.session = mock(HttpSession.class);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("jugadorActual")).thenReturn(new Usuario());
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(requestAttributes);
    }

    @Test
    public void queSeDevuelvaLaVistaInicialAhorcadoYElUsuarioEnLaSesion() {
        when(servicioAhorcado.entregarPalabra()).thenReturn("palabra");

        ModelAndView modelAndView = controlador.ahorcadoJuego(session);
        String viewname = modelAndView.getViewName();

        assertThat(viewname, equalToIgnoringCase("ahorcado"));
        assertThat(modelAndView.getModel().get("jugador"), instanceOf(Usuario.class));
        assertThat(((Usuario) modelAndView.getModel().get("jugador")).getNombre(), nullValue());
        assertThat((String) modelAndView.getModel().get("palabra"), not(isEmptyOrNullString()));
        assertThat((Integer) modelAndView.getModel().get("partesAhorcado"), is(6));
    }

    @Test
    public void queSeDevuelvaLaVistaErrorSiHayUnaExcepcion() {
        when(servicioAhorcado.entregarPalabra()).thenThrow(new RuntimeException());

        ModelAndView modelAndView = controlador.ahorcadoJuego(session);
        String viewname = modelAndView.getViewName();

        assertThat(viewname, equalToIgnoringCase("forward:/irAlAhorcado"));
        assertThat((String) modelAndView.getModel().get("mensajeError"),
                equalToIgnoringCase("No se pudo inicializar el jugador."));
    }

    @Test
    public void testMostrarPalabraOculta() {

        String palabraCompleta = "palabra";
        String letrasIntentadas1 = "pab";
        String resultadoEsperado1 = "pa_ab_a";
        String resultado1 = controlador.mostrarPalabraOculta(palabraCompleta, letrasIntentadas1);
        assertEquals(resultadoEsperado1, resultado1);

        String palabraVacia = "";
        String letrasIntentadas3 = "abc";
        String resultadoEsperado3 = "";
        String resultado3 = controlador.mostrarPalabraOculta(palabraVacia, letrasIntentadas3);
        assertEquals(resultadoEsperado3, resultado3);
    }


    @Test
    public void queSeActualiceElJuegoConLetraIncorrecta() {
        when(session.getAttribute("palabra")).thenReturn("palabra");
        when(session.getAttribute("partesAhorcado")).thenReturn(6);
        when(session.getAttribute("letrasIntentadas")).thenReturn(new StringBuilder());

        when(servicioAhorcado.intentarLetra('z', "palabra", 6)).thenReturn(5);
        when(servicioAhorcado.Perdio(5)).thenReturn(false);

        ModelAndView modelAndView = controlador.intentarLetra('z', session);
        String viewname = modelAndView.getViewName();

        assertThat(viewname, equalToIgnoringCase("ahorcado"));
        assertThat((String) modelAndView.getModel().get("palabra"), is("_______"));
        assertThat((Integer) modelAndView.getModel().get("partesAhorcado"), is(5));
        assertThat((String) modelAndView.getModel().get("letrasIntentadas"), is("z"));
    }

    @Test
    public void queSeDevuelvaLaVistaPerdisteSiPerdioElJuego() {
        when(session.getAttribute("palabra")).thenReturn("palabra");
        when(session.getAttribute("partesAhorcado")).thenReturn(1);
        when(session.getAttribute("letrasIntentadas")).thenReturn(new StringBuilder());

        when(servicioAhorcado.intentarLetra('z', "palabra", 1)).thenReturn(0);
        when(servicioAhorcado.Perdio(0)).thenReturn(true);

        ModelAndView modelAndView = controlador.intentarLetra('z', session);
        String viewname = modelAndView.getViewName();

        assertThat(viewname, equalToIgnoringCase("perdiste"));
        assertThat((String) modelAndView.getModel().get("mensajeError"), is("Perdiste. La palabra era: palabra"));
    }

    @Test
    public void queSeDevuelvaLaVistaGanasteSiAdivinoLaPalabra() {
        when(session.getAttribute("palabra")).thenReturn("palabra");
        when(session.getAttribute("partesAhorcado")).thenReturn(6);
        when(session.getAttribute("letrasIntentadas")).thenReturn(new StringBuilder("palabr"));

        when(servicioAhorcado.intentarLetra('a', "palabra", 6)).thenReturn(6);
        when(servicioAhorcado.Perdio(6)).thenReturn(false);

        ModelAndView modelAndView = controlador.intentarLetra('a', session);
        String viewname = modelAndView.getViewName();

        assertThat(viewname, equalToIgnoringCase("ganaste"));
        assertThat((String) modelAndView.getModel().get("mensajeExito"), is("¡Ganaste!"));
    }
}
