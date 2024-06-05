package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tallerwebi.dominio.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.ServicioAhorcado;

public class ControladorAhorcadoTest {

    private ControladorAhorcado controlador;
    private ServicioAhorcado servicioAhorcado;
    private HttpSession session;

    @BeforeEach
    public void init() {
        this.servicioAhorcado = mock(ServicioAhorcado.class);
        this.controlador = new ControladorAhorcado(servicioAhorcado);
        this.session = mock(HttpSession.class);

        // Simulate request context
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(requestAttributes);
    }

    @Test
    public void queSeDevuelvaLaVistaInicialAhorcadoYObjetoJugadorVacio() {

        ModelAndView modelAndView = controlador.irAlAhorcado();
        String viewname = modelAndView.getViewName();

        assertThat(viewname, equalToIgnoringCase("ahorcado"));
        assertThat(modelAndView.getModel().get("jugador"), instanceOf(Usuario.class));
        assertThat(((Usuario) modelAndView.getModel().get("jugador")).getNombre(), nullValue());
    }
    @Test
    public void queSeDevuelvaUnaPalabraTapada(){

    }
    @Test
    public void queDevuelvaUnaExceptionSiNoHayPalabrasParaDevolver(){}
    @Test
    public void queDevuelvaLaLetraAcertadaEnLaPalabra(){

    }
    @Test
    public void queElJugadorGaneSiAdivinaLaPalabra(){

    }

}
