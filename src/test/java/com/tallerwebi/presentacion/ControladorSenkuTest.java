package com.tallerwebi.presentacion;


import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.ServicioPlataforma;
import com.tallerwebi.dominio.ServicioSenku;
import com.tallerwebi.dominio.Tablero;
import com.tallerwebi.infraestructura.ServicioSenkuImpl;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
            /* 

    @Test
    public void queElMetodoDeComenzarJuegoSenkuEsteSetenadoCorrectamenteLosAtributosDeLaSesionAlRenderizarLaVistaSenku() {
        HttpSession session = mock(HttpSession.class);
        Jugador nuevoJugador = new Jugador();
        ModelAndView modelAndView = controladorSenku.comenzarJuegoSenku(nuevoJugador, session, false, null);
        String viewName = modelAndView.getViewName();

        assertThat(viewName, equalTo("senku"));
        verify(session).setAttribute(eq("senku"), any());
        verify(session).setAttribute(eq("tablero"), any());
        verify(session).setAttribute(eq("jugadorActual"), any());
    }

    @Test
    public void queSePuedaObtenerElTableroQueSeGuardoEnLaSesionDesdeElControlador() {
        HttpSession session = mock(HttpSession.class);
        Tablero tablero = new Tablero(5);
        when(session.getAttribute("tablero")).thenReturn(tablero);

        Map<String, Object> respuesta = controladorSenku.obtenerTablero(session);

        assertThat(respuesta.get("tablero"), equalTo(tablero));
    }
    @Test
public void queElModoContrarelojSeteeCorrectamenteLosAtributosDeSesion() {
    HttpSession session = mock(HttpSession.class);
    Jugador nuevoJugador = new Jugador();
    nuevoJugador.setNombre("TestJugador");
    int tiempoLimiteMinutos = 5;

    ModelAndView modelAndView = controladorSenku.comenzarJuegoSenku(nuevoJugador, session, true, tiempoLimiteMinutos);
    String viewName = modelAndView.getViewName();

    assertThat(viewName, equalTo("senku"));
    verify(session).setAttribute(eq("senku"), any());
    verify(session).setAttribute(eq("tablero"), any());
    verify(session).setAttribute(eq("jugadorActual"), eq("TestJugador"));
    verify(session).setAttribute(eq("contrareloj"), eq(true));
    verify(session).setAttribute(eq("minutos"), eq(tiempoLimiteMinutos));
    verify(session).setAttribute(eq("tiempoLimite"), anyString());  // Verifica que se establece una cadena para el tiempo límite
}
@Test
public void queElModoContrarelojManejeCorrectamenteTiempoLimiteNull() {
    HttpSession session = mock(HttpSession.class);
    Jugador nuevoJugador = new Jugador();
    nuevoJugador.setNombre("TestJugador");

    ModelAndView modelAndView = controladorSenku.comenzarJuegoSenku(nuevoJugador, session, true, null);
    String viewName = modelAndView.getViewName();

    assertThat(viewName, equalTo("senku"));
    verify(session).setAttribute(eq("senku"), any());
    verify(session).setAttribute(eq("tablero"), any());
    verify(session).setAttribute(eq("jugadorActual"), eq("TestJugador"));
    verify(session).setAttribute(eq("contrareloj"), eq(true));
    verify(session).setAttribute(eq("minutos"), eq(0)); // Verifica que se maneje un valor por defecto para el tiempo límite
    verify(session).setAttribute(eq("tiempoLimite"), anyString()); // Verifica que se establece una cadena para el tiempo límite
}

*/
}