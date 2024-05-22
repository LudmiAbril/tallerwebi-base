package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.sameInstance; 
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void queAlSolicitarIrAlAhorcadoSeEntrgueLaVistaCorrespondiente() {
        Jugador jugador = new Jugador();
        jugador.setNombre("Juan");
        when(session.getAttribute("jugador")).thenReturn(jugador);

        ModelAndView mav = this.controlador.irAlAhorcado(jugador);
        assertThat(mav.getViewName(), equalToIgnoringCase("ahorcado"));
        assertThat(mav.getModel().get("jugador"), sameInstance(jugador)); 
    }
}
