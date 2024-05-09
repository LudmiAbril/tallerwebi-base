package com.tallerwebi.presentacion;


import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.Tablero;
import com.tallerwebi.infraestructura.ServicioSenkuImpl;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.equalTo;
import javax.servlet.http.HttpSession;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

public class ControladorSenkuTest {


    private ControladorSenku controladorSenku;
    private ServicioSenkuImpl servicioSenku;

    @BeforeEach
    public void init() {
        this.servicioSenku = new ServicioSenkuImpl();
        this.controladorSenku= new ControladorSenku(servicioSenku);
    }
@Test
    public void queSeDevuelvaElNombreDeLaVistaCorrectoDelInicioDelSenkuAlLlamarAInicioSenkuEnElController() {

        ModelAndView modelAndView = controladorSenku.inicioSenku();
        String viewname = modelAndView.getViewName();

        assertThat(viewname, equalToIgnoringCase("senku-inicio"));
    
    }

     @Test
    public void queElMetodoDeComenzarJuegoSenkuEsteSetenadoCorrectamenteLosAtributosDeLaSesionAlRenderizarLaVistaSenku() {
        HttpSession session = mock(HttpSession.class);
        ModelAndView modelAndView = controladorSenku.comenzarJuegoSenku(new Jugador(), session);
        String viewName = modelAndView.getViewName();

        assertThat(viewName, equalTo("senku"));
        verify(session).setAttribute(eq("senku"), any());
        verify(session).setAttribute(eq("tablero"), any());
    }

    @Test
    public void queSePuedaObtenerElTableroQueSeGuardoEnLaSesionDesdeElControlador() {
        HttpSession session = mock(HttpSession.class);
        Tablero tablero = new Tablero(5);
        when(session.getAttribute("tablero")).thenReturn(tablero);
    
        Map<String, Object> respuesta = controladorSenku.obtenerTablero(session);
    
        assertThat(respuesta.get("tablero"), equalTo(tablero));
    }
  }
