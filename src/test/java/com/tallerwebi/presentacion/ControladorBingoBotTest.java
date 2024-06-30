package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.PartidaConPuntajeNegativoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.number.OrderingComparison.*;
import static org.mockito.Mockito.*;

public class ControladorBingoBotTest {
    private ControladorBingoBot controladorBingoBot;
    private MockHttpSession session;
    @Mock
    private ServicioBingo servicioBingoMock;
    @Mock
    private ServicioPlataforma servicioPlataformaMock;
    private Usuario jugadorMock;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.controladorBingoBot = new ControladorBingoBot(servicioBingoMock, servicioPlataformaMock);
        this.session = new MockHttpSession();
        this.jugadorMock = mock(Usuario.class);
    }

    @Test
    public void queSeDevuelvaLaVistaBingoBot() {
        TipoPartidaBingo tipoPartida = TipoPartidaBingo.BINGO;
        ModelAndView mav = controladorBingoBot.comenzarJuegoBingoBot( session);
        assertThat(mav.getViewName(), equalTo("bingoBot"));
    }

    @Test
    public void queLosCartonesDelUsuarioYDelBotSeanDistintos() {
        CartonBingo cartonUsuarioMock = mock(CartonBingo.class);
        CartonBingo cartonBotMock = mock(CartonBingo.class);

        Integer dimensionCompartida = (Integer) session.getAttribute("dimensionDelCartonDeLaSesion");
        when(this.servicioBingoMock.generarCarton(dimensionCompartida)).thenReturn(cartonUsuarioMock);
        when(this.servicioBingoMock.generarCarton(dimensionCompartida)).thenReturn(cartonBotMock);
        controladorBingoBot.comenzarJuegoBingoBot( session);
        assertThat(cartonUsuarioMock, notNullValue());
        assertThat(cartonBotMock, notNullValue());
        assertThat(cartonUsuarioMock, not(equalTo(cartonBotMock)));
    }

    @Test
    public void queLaTiradaSeaFijaParaLosDos(){
        controladorBingoBot.comenzarJuegoBingoBot(session);
        Integer tiradaLimite = (Integer) session.getAttribute("tiradaLimiteDeLaSesion");
        assertThat(tiradaLimite, equalTo(99));
    }

    @Test
    public void queSiempreCompartanLaMismaDimensionDelCarton() {
        controladorBingoBot.comenzarJuegoBingoBot( session);
        Integer dimensionUsuario = (Integer) session.getAttribute("dimensionDelCartonDeLaSesion");


        assertThat(dimensionUsuario, equalTo(5));
    }

    @Test
    public void queLosDatosSeGuardenCorrectamenteEnLaSession() {
        TipoPartidaBingo tipoPartida = TipoPartidaBingo.BINGO;
        controladorBingoBot.comenzarJuegoBingoBot( session);
        assertThat(session.getAttribute("jugadorActual"), is(notNullValue()));
        assertThat(session.getAttribute("numeroAleatorioCantado"), is(notNullValue()));
        assertThat(session.getAttribute("numerosEntregadosDeLaSesion"), is(notNullValue()));
        assertThat(session.getAttribute("dimensionDelCartonDeLaSesion"), is(notNullValue()));
        assertThat(session.getAttribute("tipoPartidaBingo"), is(notNullValue()));
    }

    @Test
    public void queLosCartonesNoSeanNulos() {
        CartonBingo cartonUsuarioMock = mock(CartonBingo.class);
        CartonBingo cartonBotMock = mock(CartonBingo.class);
        Integer dimensionCompartida = (Integer) session.getAttribute("dimensionDelCartonDeLaSesion");
        when(this.servicioBingoMock.generarCarton(dimensionCompartida)).thenReturn(cartonUsuarioMock);
        when(this.servicioBingoMock.generarCarton(dimensionCompartida)).thenReturn(cartonBotMock);
        session.setAttribute("carton", cartonUsuarioMock);
        session.setAttribute("cartonBot", cartonBotMock);
        assertThat(session.getAttribute("carton"), is(notNullValue()));
        assertThat(session.getAttribute("cartonBot"), is(notNullValue()));
    }

    @Test
    public void queSeObtenganDatosInicialesCorrectamente() {
        controladorBingoBot.comenzarJuegoBingoBot( session);
        Map<String, Object> datosIniciales = controladorBingoBot.obtenerDatosIniciales(session);
        assertThat(datosIniciales.get("numeroAleatorioCantado"), notNullValue());
        assertThat(datosIniciales.get("tipoPartidaBingo"), notNullValue());
        assertThat(datosIniciales.get("numerosRestantesParaCompletarLaTirada"), notNullValue());

    }

    @Test
    public void queSeMarqueCasilleroCorrectamente() {
        controladorBingoBot.comenzarJuegoBingoBot(session);
        CartonBingo carton = (CartonBingo) session.getAttribute("carton");
        Integer numeroCantado = (Integer) session.getAttribute("numeroAleatorioCantado");

        Boolean seMarco =servicioBingoMock.marcarCasilleroBot(numeroCantado, carton);
        assertThat(!seMarco, is(true));

    }

    @Test
    public void queSeObtengaNuevoNumeroCorrectamente() throws PartidaConPuntajeNegativoException {
        controladorBingoBot.comenzarJuegoBingoBot( session);
        Map<String, Object> respuesta = controladorBingoBot.obtenerNuevoNumero(session);

        assertThat(respuesta.get("nuevoNumero"), notNullValue());
        assertThat(respuesta.get("limiteAlcanzado"), equalTo(false));
        verify(servicioBingoMock, times(2)).entregarNumeroAleatorio(anySet());
    }


    @Test
    public void queSeObtenganLosCincoUltimosNumerosEntregadosCorrectamente(){
        controladorBingoBot.comenzarJuegoBingoBot( session);
        Set<Integer> numerosEntregados = new LinkedHashSet<>();
        for (int i = 1; i <= 10; i++) {
            numerosEntregados.add(i);
        }
        session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);

        Map<String, Object> ultimosNumeros =
                controladorBingoBot.obtenerCincoUltimosNumerosEntregados(session);

        List<Integer> numerosParaMostrar = (List<Integer>)
                ultimosNumeros.get("ultimosNumerosEntregados");
        assertThat(numerosParaMostrar.size(), equalTo(5));
        assertThat(numerosParaMostrar.get(0), equalTo(6));
    }



    @Test
    public void queSeObtenganLosNumerosFaltantesParaBingoCorrectamente() {
        controladorBingoBot.comenzarJuegoBingoBot( session);
        Map<String, Object> datosIniciales = controladorBingoBot.obtenerDatosIniciales(session);
        Integer tirada = (Integer) session.getAttribute("tiradaLimiteDeLaSesion");
        Integer faltantes = (Integer) datosIniciales.get("numerosRestantesParaCompletarLaTirada");

        Integer limiteMinimo = 0;
        assertThat(faltantes, is(org.hamcrest.Matchers.greaterThanOrEqualTo(limiteMinimo)));
    }

    @Test
    public void queSeObtenganLosNumerosFaltantesParaLineaCorrectamente() {
        controladorBingoBot.comenzarJuegoBingoBot( session);
        Map<String, Object> datosIniciales = controladorBingoBot.obtenerDatosIniciales(session);
        Integer tirada = (Integer) session.getAttribute("tiradaLimiteDeLaSesion");
        Integer faltantes = (Integer) datosIniciales.get("numerosRestantesParaCompletarLaTirada");

        Integer limiteMinimo = 0;
        assertThat(faltantes, is(org.hamcrest.Matchers.greaterThanOrEqualTo(limiteMinimo)));
    }

    @Test
    public void queSeObtengaLaDimensionDelCartonCorrectamente() {
        controladorBingoBot.comenzarJuegoBingoBot( session);
        ConfiguracionesJuego configuracionesJuegoMock = mock(ConfiguracionesJuego.class);
        when(this.jugadorMock.getConfig()).thenReturn(configuracionesJuegoMock);

        Integer dimension = (Integer) session.getAttribute("dimensionDelCartonDeLaSesion");
        when(configuracionesJuegoMock.getDimensionCarton()).thenReturn(dimension);
        assertThat(jugadorMock.getConfig().getDimensionCarton(),
                equalTo(session.getAttribute("dimensionDelCartonDeLaSesion")));
    }
}
 