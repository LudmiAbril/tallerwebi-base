package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.LinkedHashSet;
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
    public void queAlComenzarJuegoBingoSeGenereUnNumeroAleatorioYSeGuardeEnLaSesion() {
        Jugador jugadorMock = mock(Jugador.class);
        when(jugadorMock.getNombre()).thenReturn("Mica");

        Set<Integer> numerosEntregados = new LinkedHashSet<>();
        Integer numeroAleatorio = 10;

        when(servicioBingoMock.entregarNumeroAleatorio(numerosEntregados)).thenReturn(numeroAleatorio);

        controladorBingo.comenzarJuegoBingo(jugadorMock, session);

        assertThat(session.getAttribute("numeroAleatorioCantado"), equalTo(numeroAleatorio));
    }

    @Test
    public void queAlComenzarJuegoBingoSeGenereUnCartonYSeGuardeEnLaSesion() {
        CartonBingo cartonMock = mock(CartonBingo.class);
        Jugador jugadorMock = mock(Jugador.class);
        when(this.servicioBingoMock.generarCarton()).thenReturn(cartonMock);
        controladorBingo.comenzarJuegoBingo(jugadorMock, session);

        assertThat(cartonMock, equalTo(session.getAttribute("carton")));
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
    // Test Negativos

    @Test
    public void queAlComenzarJuegoBingoNoSeGenereUnNumeroAleatorioNiSeGuardeEnLaSesion() {
        Jugador jugadorMock = mock(Jugador.class);
        when(jugadorMock.getNombre()).thenReturn("Mica");

        Set<Integer> numerosEntregados = new LinkedHashSet<>();
        Integer numeroAleatorio = 10;

        when(servicioBingoMock.entregarNumeroAleatorio(numerosEntregados)).thenReturn(null);

        controladorBingo.comenzarJuegoBingo(jugadorMock, session);

        assertThat(session.getAttribute("numeroAleatorioCantado"), equalTo(null));
    }

    @Test
    public void queNoSePuedaHacerBingoSiElNumeroEntregadoNoFueMarcado() {
        // necesito un carton
        CartonBingo cartonMock = mock(CartonBingo.class);
        when(this.servicioBingoMock.generarCarton()).thenReturn(cartonMock);
        // necesito que el servicio me de un numero aleatorio
        Set<Integer> numerosEntregados = new LinkedHashSet<>();
        Integer numeroAleatorio = 10;
        when(this.servicioBingoMock.entregarNumeroAleatorio(numerosEntregados)).thenReturn(numeroAleatorio);
        // ahora ese numero no lo voy a marcar, es decir, no lo voy a agregar a los
        // numeros marcados
        Map<String, Object> respuesta = controladorBingo.hacerBingo(session);
        Set<Integer> numerosMarcadosDeLaSesion = (Set<Integer>) session.getAttribute("numerosMarcadosDeLaSesion");
        // el bingo del mapa de la respuesta tiene que ser false
        Boolean seHizoBingo = (Boolean) respuesta.get("seHizoBingo");
        // los numeros marcados de la sesion tienen que ser nulos porque no se marco
        // ninguno
        assertThat(seHizoBingo, is(false));
        assertThat(numerosMarcadosDeLaSesion, is(nullValue()));
    }

    @Test
    public void queAlComenzarJuegoBingoNoSeGenereUnCartonNiSeGuardeEnLaSesion() {
        Jugador jugadorMock = mock(Jugador.class);
        when(this.servicioBingoMock.generarCarton()).thenReturn(null);
        controladorBingo.comenzarJuegoBingo(jugadorMock, session);

        assertThat(session.getAttribute("carton"), equalTo(null));
    }

    @Test
    public void queSePuedaHacerBingo() {
        Set<Integer> numerosMarcadosDeLaSesion = new HashSet<>();
        when(servicioBingoMock.bingo(numerosMarcadosDeLaSesion)).thenReturn(true);
        session.setAttribute("numerosMarcadosDeLaSesion", numerosMarcadosDeLaSesion);

        Map<String, Object> respuesta = controladorBingo.hacerBingo(session);

        assertTrue(respuesta.containsKey("seHizoBingo"));
        assertTrue((boolean) respuesta.get("seHizoBingo"));
    }

    @Test
    public void queNoSePuedaMarcarUnNumeroQueNoEsIgualAlNumeroEntregado() {
        // Arrange
        CartonBingo cartonMock = mock(CartonBingo.class);
        Integer numeroAleatorio = 5;
        Integer numeroCasillero = 20;

        Set<Integer> numerosEntregados = new LinkedHashSet<>();
        when(this.servicioBingoMock.generarCarton()).thenReturn(cartonMock);
        when(this.servicioBingoMock.entregarNumeroAleatorio(numerosEntregados)).thenReturn(numeroAleatorio);

        Set<Integer> numerosMarcadosDeLaSesion = new HashSet<>();
        session.setAttribute("numerosMarcadosDeLaSesion", numerosMarcadosDeLaSesion);
        session.getAttribute("numerosMarcadosDeLaSesion");

        // Act
        controladorBingo.marcarCasillero(numeroCasillero, session);

        // Assert
        // Verificar que el número no se haya marcado en la sesión
        Map<String, Object> respuesta = controladorBingo.obtenerLosNumerosMarcados(session);
        Set<Integer> numerosMarcadosDeLaSesionRespuesta = (Set<Integer>) respuesta.get("numerosMarcadosDeLaSesion");

        assertFalse(numerosMarcadosDeLaSesionRespuesta.contains(numeroCasillero));
    }

    @Test
	public void queMuestreLosUltimos5NumerosEntregados() {
	}
	// que los numeros entregados se guarden correctamente
	@Test
	public void queLosNumerosEntregadosSeGuardenCorrectamente() {

	}

	// gue los num marcados se guarda
	@Test
	public void queLosNumerosMarcadosSeGuarden() {
	}

    @Test
    public void queSePuedaElegirElTipoPartidaBingoYSeGuardeSuEleccionEnLaSesion(){
        // necesito tipo partida
        // tiene que llegar la eleccion por el form 
        // 
    }
}
