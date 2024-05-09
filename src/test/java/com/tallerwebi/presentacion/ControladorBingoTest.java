package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.CartonBingo;
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.ServicioBingo;

public class ControladorBingoTest {

    private ControladorBingo controladorBingo;
    private ServicioBingo servicioBingoMock;
    private HttpSession session;

    @BeforeEach
    public void init() {
        this.servicioBingoMock = mock(ServicioBingo.class);
        this.controladorBingo = new ControladorBingo(servicioBingoMock);
        this.session = new MockHttpSession();
    }

    @Test
    public void queAlSolicitarIrAlBingoSeEntregueLaVistaIrAlBingo() {
        ModelAndView mav = this.controladorBingo.irAlBingo();
        assertThat(mav.getViewName(), equalToIgnoringCase("irAlBingo"));
    }

    @Test
    public void queAlSolicitarIrAlBingoSeGuardeElModeloCorrespondiente() {
        ModelAndView mav = this.controladorBingo.irAlBingo();
        Jugador MODELO_ACTUAL = ((Jugador) mav.getModel().get("nuevoJugador"));
        assertThat(MODELO_ACTUAL, instanceOf(Jugador.class));
    }

    @Test
    public void queAlComenzarJuegoBingoSeRendericeLaVistaBingoGenerandoUnNumeroAleatorioEnLaSesion() {
        //GIVEN
        CartonBingo cartonMock = mock(CartonBingo.class);
        //WHEN
        when(servicioBingoMock.generarCarton()).thenReturn(cartonMock);
        Jugador jugador = new Jugador();
        jugador.setNombre("NombreDePrueba");
        ModelAndView modelAndView = controladorBingo.comenzarJuegoBingo(jugador, session);
        //THEN
        assertEquals("bingo", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("nombreJugador"));
        assertSame(cartonMock, session.getAttribute("carton"));
        assertNotNull(session.getAttribute("numeroAleatorioCantado"));
    }

    @Test
    public void queAlIrAVistaBingoSeRendericeLaVistaCorrectaYSeGuardeCorrectamenteUnJugador() {
        ModelAndView modelAndView = controladorBingo.irAlBingo();
        assertEquals("irAlBingo", modelAndView.getViewName());
        ModelMap modelMap = modelAndView.getModelMap();
        assertNotNull(modelMap);
        assertTrue(modelMap.containsAttribute("nuevoJugador"));
        Object jugadorObject = modelMap.get("nuevoJugador");
        assertTrue(jugadorObject instanceof Jugador);
        Jugador jugador = (Jugador) jugadorObject;
        assertNotNull(jugador);
    }

    @Test
    public void queAlObtenerDatosInicialesEncontremosUnCartonYElNumeroAleatorioEnLaSesion() {
        CartonBingo cartonMock = mock(CartonBingo.class);
        session.setAttribute("carton", cartonMock);
        Integer numeroCantadoAleatorio = 5;
        session.setAttribute("numeroAleatorioCantado", numeroCantadoAleatorio);
        Set<Integer> numerosEntregados = new HashSet<>();
        session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);
        Map<String, Object> respuesta = controladorBingo.obtenerDatosIniciales(session);
        assertNotNull(respuesta);
        assertTrue(respuesta.containsKey("carton"));
        assertTrue(respuesta.containsKey("numeroAleatorioCantado"));
        CartonBingo cartonObtenido = (CartonBingo) respuesta.get("carton");
        assertEquals(cartonMock, cartonObtenido);
        assertEquals(numeroCantadoAleatorio, respuesta.get("numeroAleatorioCantado"));
    }
}
