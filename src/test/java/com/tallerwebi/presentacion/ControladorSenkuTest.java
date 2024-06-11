package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Casillero;
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.ServicioPlataforma;
import com.tallerwebi.dominio.ServicioSenku;
import com.tallerwebi.dominio.Tablero;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.CasilleroInexistenteException;
import com.tallerwebi.dominio.excepcion.CasilleroVacio;
import com.tallerwebi.dominio.excepcion.MovimientoInvalidoException;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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

@Test
public void queAlMoverOSeleccionarRealiceMovimientoValido() throws CasilleroInexistenteException, CasilleroVacio, MovimientoInvalidoException {
    // GIVEN
    HttpSession session = mock(HttpSession.class);
    ServicioSenku servicioSenku = mock(ServicioSenku.class);
    ServicioPlataforma servicioPlataforma = mock(ServicioPlataforma.class);
    ControladorSenku controladorSenku = new ControladorSenku(servicioSenku, servicioPlataforma);
    Tablero tablero = new Tablero(5);
    //CREO LOS CASILLEROS Y LOS SETEO PARA HACER UN MOV VALIDO
    Casillero casilleroOrigen = new Casillero(0, 2);
    casilleroOrigen.setOcupado(true); // LO MOCKEO EN OCUPADO --TRUE 
    Casillero casilleroDestino = new Casillero(2, 2);
    casilleroDestino.setOcupado(false); // MOCKEO A VACIO --FALSE

    when(servicioSenku.seleccionarCasillero(tablero, 0, 2)).thenReturn(casilleroOrigen);
    when(servicioSenku.getCasillero(tablero, 2, 2)).thenReturn(casilleroDestino);
    doNothing().when(servicioSenku).realizarMovimiento(tablero, casilleroOrigen, casilleroDestino);

    when(session.getAttribute("tablero")).thenReturn(tablero);
    when(session.getAttribute("casilleroSeleccionado")).thenReturn(casilleroOrigen);
    when(session.getAttribute("contadorMovimientos")).thenReturn(0);

    // WHEN
    Map<String, Object> respuesta = controladorSenku.moverOSeleccionar(2, 2, session);

    // THEN --SE HACE ELMOVIMIENTO Y LA LOGICA DEL SUCCES
    assertNotNull(respuesta);
    assertTrue((Boolean) respuesta.get("success"));
    assertEquals("Movimiento realizado con éxito.", respuesta.get("mensaje"));
    verify(session).removeAttribute("casilleroSeleccionado");
    verify(session).setAttribute(eq("contadorMovimientos"), eq(1));
}


@Test
public void queAlMoverOSeleccionarLanceExcepcionCuandoMovimientoNoEsValido() throws CasilleroInexistenteException, CasilleroVacio, MovimientoInvalidoException {
    // GIVEN
    HttpSession session = mock(HttpSession.class);
    ServicioSenku servicioSenku = mock(ServicioSenku.class);
    ServicioPlataforma servicioPlataforma = mock(ServicioPlataforma.class);
    ControladorSenku controladorSenku = new ControladorSenku(servicioSenku, servicioPlataforma);
    Tablero tablero = new Tablero(5);
    // Creo los casilleros y los seteo para un movimiento inválido
    Casillero casilleroOrigen = new Casillero(0, 2);
    casilleroOrigen.setOcupado(true); // MOCKEO A OCUPADO --FALSE
    Casillero casilleroDestino = new Casillero(2, 2);
    casilleroDestino.setOcupado(true); // MOCKEO A OCUPADO --FALSE

    when(servicioSenku.seleccionarCasillero(tablero, 0, 2)).thenReturn(casilleroOrigen);
    when(servicioSenku.getCasillero(tablero, 2, 2)).thenReturn(casilleroDestino);
    // SIMULAMOS LA EXCEPCION YA QUE EL MOVIMIENTO NO ES CORRECTO
    doThrow(new MovimientoInvalidoException("Movimiento inválido")).when(servicioSenku).realizarMovimiento(tablero, casilleroOrigen, casilleroDestino);

    when(session.getAttribute("tablero")).thenReturn(tablero);
    when(session.getAttribute("casilleroSeleccionado")).thenReturn(casilleroOrigen);

    // WHEN
    Map<String, Object> respuesta = controladorSenku.moverOSeleccionar(2, 2, session);

    // THEN -- SINO SEMOVIO,NO SINREMENTA Y SE LIMPIA LA SELECCCION EN LA SESSION
    assertNotNull(respuesta);
    assertFalse((Boolean) respuesta.get("success"));
    assertEquals("El casillero de destino debe estar vacío.", respuesta.get("mensaje"));
    verify(session).removeAttribute("casilleroSeleccionado"); 
    verify(session, never()).setAttribute(eq("contadorMovimientos"), any(Integer.class)); 
}



}