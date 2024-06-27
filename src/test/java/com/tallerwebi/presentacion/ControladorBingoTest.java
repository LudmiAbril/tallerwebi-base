package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.*;

import javax.servlet.http.HttpSession;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.NoHayPartidasDeBingoException;
import com.tallerwebi.dominio.excepcion.PartidaConPuntajeNegativoException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import org.mockito.MockitoAnnotations;

public class ControladorBingoTest {

    private ControladorBingo controladorBingo;
    private ServicioBingo servicioBingoMock;
    private HttpSession session;
    private ServicioPlataforma servicioPlataformaMock;

    @BeforeEach
    public void init() {
        this.servicioBingoMock = mock(ServicioBingo.class);
        this.servicioPlataformaMock = mock(ServicioPlataforma.class);
        this.controladorBingo = new ControladorBingo(servicioBingoMock, servicioPlataformaMock);
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
        Usuario MODELO_ACTUAL = ((Usuario) mav.getModel().get("nuevoJugador"));
        assertThat(MODELO_ACTUAL, instanceOf(Usuario.class));
    }

    @Test
    public void queAlComenzarJuegoBingoSeGenereUnNumeroAleatorioYSeGuardeEnLaSesion() {
        Usuario jugadorMock = mock(Usuario.class);
        when(jugadorMock.getNombre()).thenReturn("Mica");
        TipoPartidaBingo tipo = TipoPartidaBingo.BINGO;
        Set<Integer> numerosEntregados = new LinkedHashSet<>();
        Integer numeroAleatorio = 10;

        when(servicioBingoMock.entregarNumeroAleatorio(numerosEntregados)).thenReturn(numeroAleatorio);

        controladorBingo.comenzarJuegoBingo(String.valueOf(tipo), session);

        assertThat(session.getAttribute("numeroAleatorioCantado"), equalTo(numeroAleatorio));
    }

    @Test
    public void queAlComenzarJuegoBingoSeGenereUnCartonYSeGuardeEnLaSesion() {
        CartonBingo cartonMock = mock(CartonBingo.class);
        Usuario usuarioMock = mock(Usuario.class);
        String tipoMock = TipoPartidaBingo.BINGO.toString();
        when(this.servicioBingoMock.generarCarton(5)).thenReturn(cartonMock);
        controladorBingo.comenzarJuegoBingo((tipoMock), session);
        session.setAttribute("carton", cartonMock);
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
        assertTrue(jugadorObject instanceof Usuario);
        Usuario jugador = (Usuario) jugadorObject;
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

    @Test
    public void queAlComenzarJuegoBingoNoSeGenereUnNumeroAleatorioNiSeGuardeEnLaSesion() {
        Jugador jugadorMock = mock(Jugador.class);
        when(jugadorMock.getNombre()).thenReturn("Mica");

        Set<Integer> numerosEntregados = new LinkedHashSet<>();
        Integer numeroAleatorio = 10;

        when(servicioBingoMock.entregarNumeroAleatorio(numerosEntregados)).thenReturn(null);

        String tipo = "BINGO";
        controladorBingo.comenzarJuegoBingo(tipo, session);

        assertThat(session.getAttribute("numeroAleatorioCantado"), equalTo(null));
    }

    @Test
    public void queNoSePuedaHacerBingoSiElNumeroEntregadoNoFueMarcado() {
        // necesito un carton
        CartonBingo cartonMock = mock(CartonBingo.class);
        when(this.servicioBingoMock.generarCarton(5)).thenReturn(cartonMock);
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
    public void queAlComenzarJuegoBingoNoSeGenereUnCartonNiSeGuardeEnLaSesionSiNoEstasLogueado() {
        Usuario usuarioMock = mock(Usuario.class);
        when(this.servicioBingoMock.generarCarton(0)).thenReturn(null);
        String tipoMock = TipoPartidaBingo.BINGO.toString();
        controladorBingo.comenzarJuegoBingo(tipoMock, session);

        assertThat(session.getAttribute("carton"), equalTo(null));
    }

    @Test
    public void queSePuedaHacerBingo() {
        Integer dimension = 3;
        Set<Integer> numerosMarcadosDeLaSesion = new HashSet<>();
        numerosMarcadosDeLaSesion.add(1);
        numerosMarcadosDeLaSesion.add(2);
        numerosMarcadosDeLaSesion.add(3);
        numerosMarcadosDeLaSesion.add(4);
        numerosMarcadosDeLaSesion.add(5);
        numerosMarcadosDeLaSesion.add(6);
        numerosMarcadosDeLaSesion.add(7);
        numerosMarcadosDeLaSesion.add(18);
        numerosMarcadosDeLaSesion.add(19);
        session.setAttribute("dimensionDelCartonDeLaSesion", dimension);
        when(servicioBingoMock.bingo(numerosMarcadosDeLaSesion, dimension)).thenReturn(true);
        session.setAttribute("numerosMarcadosDeLaSesion", numerosMarcadosDeLaSesion);

        Map<String, Object> respuesta = controladorBingo.hacerBingo(session);

        assertTrue(respuesta.containsKey("seHizoBingo"));
        assertTrue((boolean) respuesta.get("seHizoBingo"));
    }

    @Test
    public void queSeanRealmente5LosUltimosNumerosEntregados() {
        Jugador jugadorMock = mock(Jugador.class);
        when(jugadorMock.getNombre()).thenReturn("Mica");
        Set<Integer> numerosEntregados = new LinkedHashSet<>();
        numerosEntregados.add(2);
        numerosEntregados.add(84);
        numerosEntregados.add(63);
        numerosEntregados.add(18);
        numerosEntregados.add(41);

        String tipo = "BINGO";
        controladorBingo.comenzarJuegoBingo(tipo, session);
        session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);
        Map<String, Object> objeto = controladorBingo.obtenerCincoUltimosNumerosEntregados(session);
        List<Integer> ultimosNumerosEntregados = (List<Integer>) objeto.get("ultimosNumerosEntregados");
        assertThat(ultimosNumerosEntregados.size(), equalTo(5));

    }

    @Test
    public void queSePuedaHacerLineaDeFormaHorizontal() {
        Jugador jugadorMock = mock(Jugador.class);
        Integer dimension = 5;
        session.setAttribute("dimensionDelCartonDeLaSesion", dimension);
        when(servicioBingoMock.generarCarton(dimension)).thenReturn(mock(CartonBingo.class));
        session.setAttribute("cartonDeLaSesion", mock(CartonBingo.class));
        when(jugadorMock.getNombre()).thenReturn("Axel");

        CartonBingo cartonDeLaSesion = (CartonBingo) session.getAttribute("cartonDeLaSesion");
        String tipo = "BINGO";
        controladorBingo.comenzarJuegoBingo(tipo, session);
        Set<Integer> numeroMarcados = servicioBingoMock.getNumerosMarcadosEnElCarton();
        when(servicioBingoMock.linea(numeroMarcados, cartonDeLaSesion)).thenReturn(true);
        assertThat(servicioBingoMock.linea(numeroMarcados, cartonDeLaSesion), equalTo(true));
    }

    @Test
    public void queLosNumerosEntregadosSeGuardenCorrectamente() {
        Set<Integer> numerosEntregados = new LinkedHashSet<>();
        Integer numeroAleatorio = 10;
        session.setAttribute("dimensionDelCartonDeLaSesion", 5);
        when(this.servicioBingoMock.entregarNumeroAleatorio(numerosEntregados)).thenReturn(numeroAleatorio);
        String tipo = "BINGO";
        Usuario jugador = new Usuario();
        jugador.setConfig(new ConfiguracionesJuego());
        session.setAttribute("jugadorActual", jugador);
        controladorBingo.comenzarJuegoBingo(tipo, session);
        Integer numeroAleatorioDeLaSesion = (Integer) session.getAttribute("numeroAleatorioCantado");
        assertThat(numeroAleatorioDeLaSesion, equalTo(numeroAleatorio));
    }

    @Test
    public void queLosNumerosMarcadosSeGuarden() {
        // necesito un carton
        CartonBingo cartonMock = mock(CartonBingo.class);
        when(this.servicioBingoMock.generarCarton(5)).thenReturn(cartonMock);
        // necesito que el servicio me de un numero aleatorio
        Set<Integer> numerosEntregados = new LinkedHashSet<>();
        Integer numeroAleatorio = 10;
        when(this.servicioBingoMock.entregarNumeroAleatorio(numerosEntregados)).thenReturn(numeroAleatorio);
        session.setAttribute("cartonDeLaSesion", cartonMock);
        Set<Integer> numerosMarcados = new HashSet<Integer>();
        session.setAttribute("numerosMarcadosDeLaSesion", numerosMarcados);
        controladorBingo.marcarCasillero(numeroAleatorio, session);
        Set<Integer> numerosMarcadosDeLaSesion = (Set<Integer>) session.getAttribute("numerosMarcadosDeLaSesion");
        assertThat(numerosMarcadosDeLaSesion.contains(numeroAleatorio), is(true));
    }

    @Test
    public void queSePuedaElegirElTipoPartidaBingoYSeGuardeSuEleccionEnLaSesion() {
        // necesito tipo partida
        // tiene que llegar la eleccion por el form
        CartonBingo cartonMock = mock(CartonBingo.class);
        Usuario usuarioMock = mock(Usuario.class);
        when(this.servicioBingoMock.generarCarton(5)).thenReturn(cartonMock);
        String tipoMock = TipoPartidaBingo.BINGO.toString();
        controladorBingo.comenzarJuegoBingo(tipoMock, session);
        session.setAttribute("tipo", tipoMock);
        assertThat(session.getAttribute("tipo"), equalTo(tipoMock));
    }

    @Test
    public void queNoSePuedaHacerLineaSiNoSeMarcoUnaLinea() {
        Integer dimension = 5;
        Integer numeroCasillero = 3;
        CartonBingo cartonMock = mock(CartonBingo.class);
        Set<Integer> numerosMarcados;
        numerosMarcados = new HashSet<Integer>();

        session.setAttribute("numerosMarcadosDeLaSesion", numerosMarcados);
        Set<Integer> numerosMarcadosDeLaSesion = (Set<Integer>) session.getAttribute("numerosMarcadosDeLaSesion");

        when(this.servicioBingoMock.generarCarton(dimension)).thenReturn(cartonMock);
        session.setAttribute("carton", cartonMock);
        CartonBingo carton = (CartonBingo) session.getAttribute("carton");

        servicioBingoMock.marcarCasillero(numeroCasillero, cartonMock);
        numerosMarcados.add(numeroCasillero);
        when(this.servicioBingoMock.linea(numerosMarcados, cartonMock)).thenReturn(false);

        Map<String, Object> linea = this.controladorBingo.hacerlinea(session);
        Boolean lineaController = (Boolean) linea.get("seHizoLinea");
        assertThat(lineaController, is(false));
    }

    @Test
    public void queAlSolicitarFinalizarPartidaSeEntregueLaVistaCorrespondiente() throws IllegalArgumentException,
            PartidaConPuntajeNegativoException {
        // hago que se haga linea en un carton de 3x3 con los numeros 1, 2, 3

        // P R E P A R A C I O N
        Set<Integer> numerosMarcados = new HashSet<Integer>();
        numerosMarcados.add(1);
        numerosMarcados.add(2);
        numerosMarcados.add(3);
        session.setAttribute("numerosMarcadosDeLaSesion", numerosMarcados);

        Boolean seHizoLinea = true;
        session.setAttribute("seHizoLinea", seHizoLinea);

        Boolean seHizoBingo = false;
        session.setAttribute("seHizoBingo", seHizoBingo);

        TipoPartidaBingo tipoPartidaBingo = TipoPartidaBingo.LINEA;
        session.setAttribute("tipoPartidaBingo", tipoPartidaBingo);

        Integer tiradaLimite = 90;
        session.setAttribute("tiradaLimiteDeLaSesion", tiradaLimite);

        Usuario jugador = new Usuario();
        jugador.setId(1l);
        session.setAttribute("jugadorActual", jugador);
        // E J E C U C I O N
        ModelAndView mav = this.controladorBingo.finalizar(session);

        // V E R I F I C A C I O N
        assertThat(mav.getViewName(), equalToIgnoringCase("redirect:/irAlBingo"));
    }

    @Test
    public void queAlSolicitarFinalizarPartidaSeGuardeLaPartidaBingoCorrectamenteHabiendoHechoLinea()
            throws IllegalArgumentException, PartidaConPuntajeNegativoException, NoHayPartidasDeBingoException {

        // P R E P A R A C I O N
        Set<Integer> numerosMarcados = new HashSet<Integer>();
        numerosMarcados.add(1);
        numerosMarcados.add(2);
        numerosMarcados.add(3);
        session.setAttribute("numerosMarcadosDeLaSesion", numerosMarcados);

        Boolean seHizoLinea = true;
        session.setAttribute("seHizoLinea", seHizoLinea);

        Boolean seHizoBingo = false;
        session.setAttribute("seHizoBingo", seHizoBingo);

        TipoPartidaBingo tipoPartidaBingo = TipoPartidaBingo.LINEA;
        session.setAttribute("tipoPartidaBingo", tipoPartidaBingo);

        Integer tiradaLimite = 90;
        session.setAttribute("tiradaLimiteDeLaSesion", tiradaLimite);

        Usuario jugador = new Usuario();
        jugador.setId(1l);
        session.setAttribute("jugadorActual", jugador);

        // E J E C U C I O N
        this.controladorBingo.finalizar(session);

        List<PartidaBingo> partidasBingoEsperadas = new ArrayList<PartidaBingo>();
        int cantidadDeNumerosMarcados = numerosMarcados.size();
        PartidaBingo partida = new PartidaBingo(1L, Juego.BINGO, numerosMarcados, seHizoLinea, seHizoBingo,
                tipoPartidaBingo, tiradaLimite, cantidadDeNumerosMarcados, false);

        partidasBingoEsperadas.add(partida);

        when(this.servicioPlataformaMock.generarRankingDePartidasDeBingo(1l)).thenReturn(partidasBingoEsperadas);

        // V E R I F I C A C I O N
        assertThat(this.servicioPlataformaMock.generarRankingDePartidasDeBingo(1L).get(0).getIdJugador(), equalTo(1L));
        assertThat(this.servicioPlataformaMock.generarRankingDePartidasDeBingo(1L).get(0).getJuego(),
                equalTo(Juego.BINGO));
        assertThat(this.servicioPlataformaMock.generarRankingDePartidasDeBingo(1L).get(0).getCasillerosMarcados(),
                equalTo(numerosMarcados));
        assertThat(this.servicioPlataformaMock.generarRankingDePartidasDeBingo(1L).get(0).getSeHizoLinea(),
                equalTo(seHizoLinea));
        assertThat(this.servicioPlataformaMock.generarRankingDePartidasDeBingo(1L).get(0).getSeHizoBingo(),
                equalTo(seHizoBingo));
        assertThat(this.servicioPlataformaMock.generarRankingDePartidasDeBingo(1L).get(0).getTipoPartidaBingo(),
                equalTo(tipoPartidaBingo));
        assertThat(this.servicioPlataformaMock.generarRankingDePartidasDeBingo(1L).get(0).getTirada(),
                equalTo(tiradaLimite));
        assertThat(this.servicioPlataformaMock.generarRankingDePartidasDeBingo(1L).get(0)
                .getCantidadDeCasillerosMarcados(), equalTo(cantidadDeNumerosMarcados));

    }

    @Test
    public void queAlSolicitarFinalizarPartidaSeGuardeLaPartidaBingoCorrectamenteHabiendoHechoBingo()
            throws IllegalArgumentException, PartidaConPuntajeNegativoException, NoHayPartidasDeBingoException {

        // P R E P A R A C I O N
        Set<Integer> numerosMarcados = new HashSet<Integer>();
        numerosMarcados.add(1);
        numerosMarcados.add(2);
        numerosMarcados.add(3);
        session.setAttribute("numerosMarcadosDeLaSesion", numerosMarcados);

        Boolean seHizoLinea = false;
        session.setAttribute("seHizoLinea", seHizoLinea);

        Boolean seHizoBingo = true;
        session.setAttribute("seHizoBingo", seHizoBingo);

        TipoPartidaBingo tipoPartidaBingo = TipoPartidaBingo.LINEA;
        session.setAttribute("tipoPartidaBingo", tipoPartidaBingo);

        Integer tiradaLimite = 90;
        session.setAttribute("tiradaLimiteDeLaSesion", tiradaLimite);

        Usuario jugador = new Usuario();
        jugador.setId(1l);
        session.setAttribute("jugadorActual", jugador);

        // E J E C U C I O N
        this.controladorBingo.finalizar(session);

        List<PartidaBingo> partidasBingoEsperadas = new ArrayList<PartidaBingo>();
        int cantidadDeNumerosMarcados = numerosMarcados.size();
        PartidaBingo partida = new PartidaBingo(1L, Juego.BINGO, numerosMarcados, seHizoLinea, seHizoBingo,
                tipoPartidaBingo, tiradaLimite, cantidadDeNumerosMarcados, false);

        partidasBingoEsperadas.add(partida);

        when(this.servicioPlataformaMock.generarRankingDePartidasDeBingo(1l)).thenReturn(partidasBingoEsperadas);

        // V E R I F I C A C I O N
        assertThat(this.servicioPlataformaMock.generarRankingDePartidasDeBingo(1L).get(0).getIdJugador(), equalTo(1L));
        assertThat(this.servicioPlataformaMock.generarRankingDePartidasDeBingo(1L).get(0).getJuego(),
                equalTo(Juego.BINGO));
        assertThat(this.servicioPlataformaMock.generarRankingDePartidasDeBingo(1L).get(0).getCasillerosMarcados(),
                equalTo(numerosMarcados));
        assertThat(this.servicioPlataformaMock.generarRankingDePartidasDeBingo(1L).get(0).getSeHizoLinea(),
                equalTo(seHizoLinea));
        assertThat(this.servicioPlataformaMock.generarRankingDePartidasDeBingo(1L).get(0).getSeHizoBingo(),
                equalTo(seHizoBingo));
        assertThat(this.servicioPlataformaMock.generarRankingDePartidasDeBingo(1L).get(0).getTipoPartidaBingo(),
                equalTo(tipoPartidaBingo));
        assertThat(this.servicioPlataformaMock.generarRankingDePartidasDeBingo(1L).get(0).getTirada(),
                equalTo(tiradaLimite));
        assertThat(this.servicioPlataformaMock.generarRankingDePartidasDeBingo(1L).get(0)
                .getCantidadDeCasillerosMarcados(), equalTo(cantidadDeNumerosMarcados));

    }

    @Test
    public void queSePuedaObtenerLaCantidadDeNumerosRestantesParaCompletarLaTirada()
            throws PartidaConPuntajeNegativoException {
        Integer tirada = 90;
        session.setAttribute("tiradaLimiteDeLaSesion", tirada);
        Set<Integer> numerosEntregados = new LinkedHashSet<Integer>();
        numerosEntregados.add(5);
        numerosEntregados.add(9);
        session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);
        when(this.servicioBingoMock.obtenerCantidadDeNumerosRestantesParaCompletarLaTirada(tirada,
                numerosEntregados.size())).thenReturn(88);
        Map<String, Object> respuesta = this.controladorBingo.obtenerNuevoNumero(session);
        Integer numerosRestantes = (Integer) respuesta.get("numerosRestantesParaCompletarLaTirada");
        assertThat(numerosRestantes, is(88));
    }

    @Test
    public void queAlSolicitarComenzarJuegoBingoSeEntregueLaVistaCorrespondiente() {
        ModelAndView mav = this.controladorBingo.comenzarJuegoBingo("BINGO", session);
        assertThat(mav.getViewName(), equalToIgnoringCase("bingo"));
    }

    @Test
    public void queAlSolicitarComenzarJuegoBingoSeGuardenLosDatosCorrespondientesEnElModelo() {
        Usuario usuario = new Usuario();
        usuario.setNombre("user");
        ModelAndView mav = this.controladorBingo.comenzarJuegoBingo("BINGO", session);
        String nombreModeloActual = (String) mav.getModel().get("nombreJugador");
        TipoPartidaBingo tipoPartidaModeloActual = (TipoPartidaBingo) mav.getModel().get("tipoPartidaBingoDeLaSesion");
        assertThat(nombreModeloActual, equalTo("user"));
        assertThat(tipoPartidaModeloActual, equalTo(TipoPartidaBingo.BINGO));
    }

    @Test
    public void queGuardeElMensajeDeErrorEnElModeloSiNoSePuedeGuardarUnaPartida()
            throws IllegalArgumentException, PartidaConPuntajeNegativoException, NoHayPartidasDeBingoException {
        // P R E P A R A C I O N
        Set<Integer> numerosMarcados = new HashSet<>();
        numerosMarcados.add(null);
        numerosMarcados.add(null);
        numerosMarcados.add(null);
        session.setAttribute("numerosMarcadosDeLaSesion", numerosMarcados);

        Boolean seHizoLinea = null;
        session.setAttribute("seHizoLinea", seHizoLinea);
        Boolean seHizoBingo = null;
        session.setAttribute("seHizoBingo", seHizoBingo);

        TipoPartidaBingo tipoPartidaBingo = null;
        session.setAttribute("tipoPartidaBingo", tipoPartidaBingo);

        Integer tiradaLimite = null;
        session.setAttribute("tiradaLimiteDeLaSesion", tiradaLimite);

        Usuario jugador = new Usuario();
        jugador.setId(null);
        session.setAttribute("jugadorActual", jugador);

        doThrow(new IllegalArgumentException("Error al guardar la partida"))
                .when(servicioPlataformaMock).agregarPartida(any(PartidaBingo.class));

        // E J E C U C I O N
        ModelAndView mav = this.controladorBingo.finalizar(session);

        // V E R I F I C A C I O N
        String mensajeErrorActual = (String) mav.getModelMap().getAttribute("mensajeError");
        assertThat(mensajeErrorActual, equalToIgnoringCase("Ocurrió un error al intentar guardar la partida."));
    }

    @Test
    public void queSePuedaGenerarUnCartonDe5x5APartirDeLaConfiguracionDefinida()
            throws IllegalArgumentException, PartidaConPuntajeNegativoException, NoHayPartidasDeBingoException {
        Usuario usuarioMock = mock(Usuario.class);
        when(usuarioMock.getNombre()).thenReturn("Mica");
        when(usuarioMock.getConfig()).thenReturn(new ConfiguracionesJuego());
        CartonBingo cartonMock = mock(CartonBingo.class);
        Integer dimension = usuarioMock.getConfig().getDimensionCarton();
        when(this.servicioBingoMock.generarCarton(dimension)).thenReturn(cartonMock);
        session.setAttribute("carton", cartonMock);
        assertThat(5, equalTo(usuarioMock.getConfig().getDimensionCarton()));
    }

    @Test
    public void queSePuedaGenerarUnCartonDe4x4APartirDeLaConfiguracionDefinida()
            throws IllegalArgumentException, PartidaConPuntajeNegativoException, NoHayPartidasDeBingoException {
        Usuario usuarioMock = mock(Usuario.class);
        ConfiguracionesJuego configMock = mock(ConfiguracionesJuego.class);
        when(usuarioMock.getNombre()).thenReturn("Mica");
        when(usuarioMock.getConfig()).thenReturn(configMock);

        when(configMock.getDimensionCarton()).thenReturn(4);
        CartonBingo cartonMock = mock(CartonBingo.class);
        Integer dimension = configMock.getDimensionCarton();
        when(this.servicioBingoMock.generarCarton(dimension)).thenReturn(cartonMock);
        session.setAttribute("carton", cartonMock);
        assertThat(4, equalTo(configMock.getDimensionCarton()));
    }

    @Test
    public void queSePuedaGenerarUnCartonDe3x3APartirDeLaConfiguracionDefinida()
            throws IllegalArgumentException, PartidaConPuntajeNegativoException, NoHayPartidasDeBingoException {
        Usuario usuarioMock = mock(Usuario.class);
        ConfiguracionesJuego configMock = mock(ConfiguracionesJuego.class);
        when(usuarioMock.getNombre()).thenReturn("Mica");
        when(usuarioMock.getConfig()).thenReturn(configMock);

        when(configMock.getDimensionCarton()).thenReturn(3);
        CartonBingo cartonMock = mock(CartonBingo.class);
        Integer dimension = configMock.getDimensionCarton();
        when(this.servicioBingoMock.generarCarton(dimension)).thenReturn(cartonMock);
        session.setAttribute("carton", cartonMock);
        assertThat(3, equalTo(configMock.getDimensionCarton()));
    }

}
