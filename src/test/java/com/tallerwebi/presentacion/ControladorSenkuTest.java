package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Casillero;
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.ServicioPlataforma;
import com.tallerwebi.dominio.ServicioSenku;
import com.tallerwebi.dominio.Tablero;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.CasilleroInexistenteException;
import com.tallerwebi.dominio.excepcion.CasilleroVacio;
import com.tallerwebi.infraestructura.ServicioSenkuImpl;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.equalTo;
import javax.servlet.http.HttpSession;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

public class ControladorSenkuTest {

    private ControladorSenku controladorSenku;
    private ServicioSenku servicioSenku;
    private ServicioPlataforma servicioPlataforma;

    @BeforeEach
    public void init() {
        servicioSenku = mock(ServicioSenku.class);
        servicioPlataforma = mock(ServicioPlataforma.class);
        controladorSenku = new ControladorSenku(servicioSenku, servicioPlataforma);
    }

    @Test
    public void queSeDevuelvaElNombreDeLaVistaCorrectoDelInicioDelSenkuAlLlamarAInicioSenkuEnElController() {
        ModelAndView modelAndView = controladorSenku.inicioSenku();
        String viewname = modelAndView.getViewName();
        assertThat(viewname, equalToIgnoringCase("irAlSenku"));
    }
    @Test
    public void queAlcomenzarJuegoSenkuSeEstablezcanCorrectamenteLosDatosEnLaSesionYModelo() {
        // GIVEN
        HttpSession session = mock(HttpSession.class);
        ControladorSenku controladorSenku = new ControladorSenku(servicioSenku, servicioPlataforma);
        ModelAndView modelAndView = controladorSenku.comenzarJuegoSenku(session);

        // THEN-WHEN
        ModelMap modelMap = modelAndView.getModelMap();
        assertNotNull(modelMap);
        assertEquals("senku", modelAndView.getViewName());
        assertEquals("¡Bienvenido user! Comienza tu juego.", modelMap.get("mensaje"));
        assertEquals("user", modelMap.get("nombreJugador"));
        assertEquals(0, modelMap.get("contadorMovimientos"));


        verify(session).setAttribute(eq("jugadorActual"), any());
        verify(session).setAttribute(eq("tablero"), any());
        verify(session).setAttribute(eq("contadorMovimientos"), eq(0));
    }
 @Test
 public void queAlobtenerTableroSeRetorneElTableroCorrectamente() {
     //GIVEN
     HttpSession session = mock(HttpSession.class);
     ControladorSenku controladorSenku = new ControladorSenku(servicioSenku, servicioPlataforma);
     Tablero tablero = new Tablero(5);

     // WHEN
     when(session.getAttribute("tablero")).thenReturn(tablero);

     Map<String, Object> respuesta = controladorSenku.obtenerTablero(session);

     // THEN
     assertNotNull(respuesta);
     assertTrue(respuesta.containsKey("tablero"));
     assertEquals(tablero, respuesta.get("tablero"));
 }
    


 @Test
 public void queAlObtenerTableroRetorneNullSiNoHayTableroEnLaSesion() {
     // GIVEN
     HttpSession session = mock(HttpSession.class);
     ControladorSenku controladorSenku = new ControladorSenku(servicioSenku, servicioPlataforma);
     
     // WHEN
     when(session.getAttribute("tablero")).thenReturn(null);
     Map<String, Object> respuesta = controladorSenku.obtenerTablero(session);
 
     // THEN
     assertNotNull(respuesta);
     assertNull(respuesta.get("tablero"));
 }
 @Test
public void queAlMarcarCasilleroSeSeleccioneCorrectamenteYSeRetorneRespuestaExitosa() {
    // GIVEN
    HttpSession session = mock(HttpSession.class);
    ControladorSenku controladorSenku = new ControladorSenku(servicioSenku, servicioPlataforma);
    Tablero tablero = new Tablero(5);
    when(session.getAttribute("tablero")).thenReturn(tablero);
    int x = 2;
    int y = 3;

    // WHEN
    Map<String, Object> respuesta = controladorSenku.marcarCasillero(x, y, session);

    // THEN
    assertNotNull(respuesta);
    assertTrue(respuesta.containsKey("success"));
    assertTrue((Boolean) respuesta.get("success"));
    assertNull(respuesta.get("message"));
}
@Test
public void queAlMarcarCasilleroRetorneMensajeDeErrorSiElCasilleroEstaVacio()
        throws CasilleroVacio, CasilleroInexistenteException {
    // GIVEN
    HttpSession session = mock(HttpSession.class);
    ControladorSenku controladorSenku = new ControladorSenku(servicioSenku, servicioPlataforma);
    Tablero tablero = new Tablero(5);
    when(session.getAttribute("tablero")).thenReturn(tablero);
    int x = 2;
    int y = 3;

    //supuesto -casillero vacio- (* - *)
    when(servicioSenku.seleccionarCasillero(tablero, x, y)).thenThrow(new CasilleroVacio(null));

    // WHEN
    Map<String, Object> respuesta = controladorSenku.marcarCasillero(x, y, session);

    // THEN
    assertNotNull(respuesta);
    assertFalse((Boolean) respuesta.get("success"));
    assertEquals("El casillero seleccionado está vacío.", respuesta.get("message"));
}

/* 
@Test
public void queAlMoverOSeleccionarRealiceMovimientoValido() {
    // GIVEN
    HttpSession session = mock(HttpSession.class);
    ControladorSenku controladorSenku = new ControladorSenku(servicioSenku, servicioPlataforma);
    Tablero tablero = new Tablero(5);
    tablero.getCasilleros()[2][2].setOcupado(true);
    when(session.getAttribute("tablero")).thenReturn(tablero);
    when(session.getAttribute("casilleroSeleccionado")).thenReturn(tablero.getCasilleros()[2][2]);

    // WHEN
    Map<String, Object> respuesta = controladorSenku.moverOSeleccionar(2, 4, session);

    // THEN
    assertNotNull(respuesta);
    assertTrue((Boolean) respuesta.get("success"));
    assertEquals("Movimiento realizado con éxito.", respuesta.get("mensaje"));
}

*/

}