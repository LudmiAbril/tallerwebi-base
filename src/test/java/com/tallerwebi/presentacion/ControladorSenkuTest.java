package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Casillero;
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.PartidaSenku;
import com.tallerwebi.dominio.ServicioPlataforma;
import com.tallerwebi.dominio.ServicioSenku;
import com.tallerwebi.dominio.Tablero;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.CasilleroInexistenteException;
import com.tallerwebi.dominio.excepcion.CasilleroVacio;
import com.tallerwebi.dominio.excepcion.MovimientoInvalidoException;
import com.tallerwebi.dominio.excepcion.PartidaConPuntajeNegativoException;
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
import static org.mockito.Mockito.times;
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
        assertEquals("¡Bienvenido user!", modelMap.get("mensaje"));
        assertEquals("user", modelMap.get("nombreJugador"));
        assertEquals(0, modelMap.get("contadorMovimientos"));

        verify(session).setAttribute(eq("jugadorActual"), any());
        verify(session).setAttribute(eq("tablero"), any());
        verify(session).setAttribute(eq("contadorMovimientos"), eq(0));
    }

    @Test
    public void queAlobtenerTableroSeRetorneElTableroCorrectamente() {
        // GIVEN
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

        when(servicioSenku.seleccionarCasillero(tablero, x, y)).thenThrow(new CasilleroVacio(null));

        // WHEN
        Map<String, Object> respuesta = controladorSenku.marcarCasillero(x, y, session);

        // THEN
        assertNotNull(respuesta);
        assertFalse((Boolean) respuesta.get("success"));
        assertEquals("El casillero seleccionado está vacío.", respuesta.get("message"));
    }

    @Test
    public void queAlMoverOSeleccionarRealiceMovimientoValido()
            throws CasilleroInexistenteException, CasilleroVacio, MovimientoInvalidoException {
        // GIVEN
        HttpSession session = mock(HttpSession.class);
        ServicioSenku servicioSenku = mock(ServicioSenku.class);
        ServicioPlataforma servicioPlataforma = mock(ServicioPlataforma.class);
        ControladorSenku controladorSenku = new ControladorSenku(servicioSenku, servicioPlataforma);
        Tablero tablero = new Tablero(5);

        Casillero casilleroOrigen = new Casillero(0, 2);
        casilleroOrigen.setOcupado(true);
        Casillero casilleroDestino = new Casillero(2, 2);
        casilleroDestino.setOcupado(false);

        when(servicioSenku.seleccionarCasillero(tablero, 0, 2)).thenReturn(casilleroOrigen);
        when(servicioSenku.getCasillero(tablero, 2, 2)).thenReturn(casilleroDestino);
        doNothing().when(servicioSenku).realizarMovimiento(tablero, casilleroOrigen, casilleroDestino);

        when(session.getAttribute("tablero")).thenReturn(tablero);
        when(session.getAttribute("casilleroSeleccionado")).thenReturn(casilleroOrigen);
        when(session.getAttribute("contadorMovimientos")).thenReturn(0);

        // WHEN
        Map<String, Object> respuesta = controladorSenku.moverOSeleccionar(2, 2, session);

        // THEN
        assertNotNull(respuesta);
        assertTrue((Boolean) respuesta.get("success"));
        assertEquals("Movimiento realizado con éxito.", respuesta.get("mensaje"));
        verify(session).removeAttribute("casilleroSeleccionado");
        verify(session).setAttribute(eq("contadorMovimientos"), eq(1));
    }

    @Test
    public void queAlMoverOSeleccionarLanceExcepcionCuandoMovimientoNoEsValido()
            throws CasilleroInexistenteException, CasilleroVacio, MovimientoInvalidoException {
        // GIVEN
        HttpSession session = mock(HttpSession.class);
        ServicioSenku servicioSenku = mock(ServicioSenku.class);
        ServicioPlataforma servicioPlataforma = mock(ServicioPlataforma.class);
        ControladorSenku controladorSenku = new ControladorSenku(servicioSenku, servicioPlataforma);
        Tablero tablero = new Tablero(5);

        Casillero casilleroOrigen = new Casillero(0, 2);
        casilleroOrigen.setOcupado(true);
        Casillero casilleroDestino = new Casillero(2, 2);
        casilleroDestino.setOcupado(true);

        when(servicioSenku.seleccionarCasillero(tablero, 0, 2)).thenReturn(casilleroOrigen);
        when(servicioSenku.getCasillero(tablero, 2, 2)).thenReturn(casilleroDestino);

        doThrow(new MovimientoInvalidoException("Movimiento inválido")).when(servicioSenku).realizarMovimiento(tablero,
                casilleroOrigen, casilleroDestino);

        when(session.getAttribute("tablero")).thenReturn(tablero);
        when(session.getAttribute("casilleroSeleccionado")).thenReturn(casilleroOrigen);

        // WHEN
        Map<String, Object> respuesta = controladorSenku.moverOSeleccionar(2, 2, session);

        // THEN
        assertNotNull(respuesta);
        assertFalse((Boolean) respuesta.get("success"));
        assertEquals("El casillero de destino debe estar vacío.", respuesta.get("mensaje"));
        verify(session).removeAttribute("casilleroSeleccionado");
        verify(session, never()).setAttribute(eq("contadorMovimientos"), any(Integer.class));
    }

    @Test
    public void queAlReiniciarLaPartidaSeEstablezcanLosAtributosCorrectamente() {
        // GIVEN
        HttpSession session = mock(HttpSession.class);
        ControladorSenku controladorSenku = new ControladorSenku(mock(ServicioSenku.class),
                mock(ServicioPlataforma.class));

        Usuario usuarioMock = new Usuario();
        usuarioMock.setNombre("user");

        when(session.getAttribute("jugadorActual")).thenReturn(usuarioMock);

        // WHEN
        ModelAndView modelAndView = controladorSenku.reiniciarPartida(session);

        // THEN
        verify(session).setAttribute(eq("tablero"), any(Tablero.class));
        verify(session).setAttribute("contadorMovimientos", 0);
        verify(session).removeAttribute("casilleroSeleccionado");

        verify(session, never()).setAttribute(eq("jugadorActual"), any(Usuario.class));

        ModelMap model = modelAndView.getModelMap();
        assertNotNull(model);
        assertEquals("Partida reiniciada. ¡Buena suerte user!", model.get("mensaje"));
        assertEquals("user", model.get("nombreJugador"));
        assertEquals(0, model.get("contadorMovimientos"));
        assertEquals("senku", modelAndView.getViewName());
    }

    @Test
    public void queAlReiniciarLaPartidaSeCreeUnNuevoUsuarioSiNoExiste() {
        // GIVEN
        HttpSession session = mock(HttpSession.class);
        ControladorSenku controladorSenku = new ControladorSenku(mock(ServicioSenku.class),
                mock(ServicioPlataforma.class));

        when(session.getAttribute("jugadorActual")).thenReturn(null);

        // WHEN
        ModelAndView modelAndView = controladorSenku.reiniciarPartida(session);

        // THEN
        verify(session).setAttribute(eq("tablero"), any(Tablero.class));
        verify(session).setAttribute("contadorMovimientos", 0);
        verify(session).removeAttribute("casilleroSeleccionado");

        verify(session).setAttribute(eq("jugadorActual"), any(Usuario.class));

        ModelMap model = modelAndView.getModelMap();
        assertNotNull(model);
        assertEquals("Partida reiniciada. ¡Buena suerte user!", model.get("mensaje"));
        assertEquals("user", model.get("nombreJugador"));
        assertEquals(0, model.get("contadorMovimientos"));
        assertEquals("senku", modelAndView.getViewName());
    }

    @Test
    public void queAlFinalizarPartidaSeGuardeLaPartidaSiSeGano()
            throws IllegalArgumentException, PartidaConPuntajeNegativoException {
        // GIVEN
        HttpSession session = mock(HttpSession.class);
        ServicioSenku servicioSenku = mock(ServicioSenku.class);
        ServicioPlataforma servicioPlataforma = mock(ServicioPlataforma.class);
        ControladorSenku controladorSenku = new ControladorSenku(servicioSenku, servicioPlataforma);

        Tablero tablero = new Tablero(5);
        Usuario jugador = new Usuario();
        jugador.setId(1L);
        jugador.setNombre("user");

        when(session.getAttribute("jugadorActual")).thenReturn(jugador);
        when(session.getAttribute("tablero")).thenReturn(tablero);
        when(session.getAttribute("contadorMovimientos")).thenReturn(10);
        when(servicioSenku.seGano(tablero)).thenReturn(true);

        // WHEN
        ModelAndView modelAndView = controladorSenku.finalizarPartida(session);

        // THEN
        verify(servicioPlataforma).agregarPartida(any(PartidaSenku.class));
        assertEquals("redirect:/acceso-juegos", modelAndView.getViewName());
    }

    @Test
    public void queAlFinalizarPartidaNoSeGuardeLaPartidaSiNoSeGano() throws IllegalArgumentException,
            PartidaConPuntajeNegativoException {
        // GIVEN
        HttpSession session = mock(HttpSession.class);
        ServicioSenku servicioSenku = mock(ServicioSenku.class);
        ServicioPlataforma servicioPlataforma = mock(ServicioPlataforma.class);
        ControladorSenku controladorSenku = new ControladorSenku(servicioSenku, servicioPlataforma);

        Tablero tablero = new Tablero(5);
        Usuario jugador = new Usuario();
        jugador.setId(1L);
        jugador.setNombre("user");

        when(session.getAttribute("jugadorActual")).thenReturn(jugador);
        when(session.getAttribute("tablero")).thenReturn(tablero);
        when(session.getAttribute("contadorMovimientos")).thenReturn(10);
        when(servicioSenku.seGano(tablero)).thenReturn(false);

        // WHEN
        ModelAndView modelAndView = controladorSenku.finalizarPartida(session);

        // THEN
        verify(servicioPlataforma, never()).agregarPartida(any(PartidaSenku.class));
        assertEquals("redirect:/acceso-juegos", modelAndView.getViewName());
    }

    @Test
    public void queAlComprobarSiSeGanoSeEstablezcanLosAtributosCorrectamente() throws MovimientoInvalidoException {
        // GIVEN
        HttpSession session = mock(HttpSession.class);

        Tablero tablero = new Tablero(5);
        Usuario jugador = new Usuario();
        jugador.setNombre("user");
        when(session.getAttribute("jugadorActual")).thenReturn(jugador);
        when(session.getAttribute("tablero")).thenReturn(tablero);

        when(servicioSenku.seGano(tablero)).thenReturn(true);
        when(servicioSenku.validarQueHayaMovimientosValidosDisponibles(tablero)).thenReturn(true);

        // WHEN
        Map<String, Object> respuesta = controladorSenku.comprobarSiSeGano(session);

        // THEN
        assertNotNull(respuesta);
        assertTrue((Boolean) respuesta.get("seGano"));
        assertTrue((Boolean) respuesta.get("movimientosDisponibles"));
        assertEquals("user", respuesta.get("nombreJugador"));
    }

    @Test
    public void queAlComprobarSiSeGanoYNoEsAsiSeEstablezcanLosAtributosCorrectamenteSiNoSeGano()
            throws MovimientoInvalidoException {
        // GIVEN
        HttpSession session = mock(HttpSession.class);

        Tablero tablero = new Tablero(5);
        Usuario jugador = new Usuario();
        jugador.setNombre("user");
        when(session.getAttribute("jugadorActual")).thenReturn(jugador);
        when(session.getAttribute("tablero")).thenReturn(tablero);

        when(servicioSenku.seGano(tablero)).thenReturn(false);
        when(servicioSenku.validarQueHayaMovimientosValidosDisponibles(tablero)).thenReturn(true);

        // WHEN
        Map<String, Object> respuesta = controladorSenku.comprobarSiSeGano(session);

        // THEN
        assertNotNull(respuesta);
        assertFalse((Boolean) respuesta.get("seGano"));
        assertTrue((Boolean) respuesta.get("movimientosDisponibles"));
        assertEquals("user", respuesta.get("nombreJugador"));
    }

    @Test
    public void queSoloSeLlameAValidarMovimientosDispSiSeGanoEsFalse() throws MovimientoInvalidoException {
        HttpSession session = mock(HttpSession.class);
        Tablero tablero = new Tablero(5);
        Usuario jugador = new Usuario();
        jugador.setNombre("user");
        // WHEN
        when(session.getAttribute("jugadorActual")).thenReturn(jugador);
        when(session.getAttribute("tablero")).thenReturn(tablero);
        when(servicioSenku.seGano(any(Tablero.class))).thenReturn(false);
        when(servicioSenku.validarQueHayaMovimientosValidosDisponibles(any(Tablero.class))).thenReturn(false);

        Map<String, Object> respuesta = controladorSenku.comprobarSiSeGano(session);

        // THEN
        assertFalse((Boolean) respuesta.get("seGano"));
        assertFalse((Boolean) respuesta.get("movimientosDisponibles"));

        verify(servicioSenku, times(1)).validarQueHayaMovimientosValidosDisponibles(any(Tablero.class));
    }

}