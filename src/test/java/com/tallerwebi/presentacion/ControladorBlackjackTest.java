package com.tallerwebi.presentacion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.PartidaConPuntajeNegativoException;
import com.tallerwebi.dominio.excepcion.PartidaDeBingoSinLineaNiBingoException;
import com.tallerwebi.dominio.excepcion.PartidaDeUsuarioNoEncontradaException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;

public class ControladorBlackjackTest {
    private ControladorBlackjack controladorBlackjack;
    private MockHttpSession session;
    @Mock
    private ServicioBlackjack servicioBlackjackMock;
    @Mock
    private ServicioPlataforma servicioPlataformaMock;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.controladorBlackjack = new ControladorBlackjack(servicioBlackjackMock, servicioPlataformaMock);
        this.session = new MockHttpSession();
        Usuario jugador = new Usuario();
        jugador.setNombre("nombre");
        jugador.setId((long) 1);
        session.setAttribute("jugadorActual", jugador);
    }

    @Test
    public void queSeDevuelvaLaVistaInicialDeBlackjackConElTiempoConfiguradoDelUsuarioPorDefecto() {
        // simulamos un usuario logueado en la sesion con valores por defecto
        Usuario jugador = new Usuario();
        session.setAttribute("jugadorActual", jugador);

        // ingresamos a inicio blackjack
        ModelAndView modelAndView = controladorBlackjack.inicioBlackjack(session);

        // obtenemos el nombre de la vista y el tiempo del modelo
        String viewname = modelAndView.getViewName();
        Integer duracionPartida = (Integer) modelAndView.getModel().get("tiempoDefault");

        // comprobamos la vista y el valor del tiempo
        assertThat(viewname, equalToIgnoringCase("irAlBlackjack"));
        assertThat(duracionPartida, equalTo(5));
    }

    @Test
    public void queSeDevuelvaLaVistaDelJuego() {
        ModelAndView mav = controladorBlackjack.comenzarBlackjack(session, false, 3);
        assertThat(mav.getViewName(), equalTo("blackjack"));
    }

    @Test
    public void queAlIniciarseElJuegoSeGuardenEnLaSesionLasManosInicialesDelJugadorYelCrupier() {
        // Preparación de los datos esperados
        Carta ca = new Carta("A", 11, Palo.CORAZON);
        Carta cb = new Carta("3", 3, Palo.CORAZON);
        Carta cc = new Carta("6", 6, Palo.DIAMANTE);
        Carta cd = new Carta("9", 9, Palo.TREBOL);

        List<Carta> manoJugadorEsperada = new ArrayList<>();
        manoJugadorEsperada.add(cc);
        manoJugadorEsperada.add(ca);

        List<Carta> manoCrupierEsperada = new ArrayList<>();
        manoCrupierEsperada.add(cd);
        manoCrupierEsperada.add(cb);

        when(servicioBlackjackMock.entregarCartasPrincipales())
                .thenReturn(manoJugadorEsperada)
                .thenReturn(manoCrupierEsperada);

        // datos previos de la session
        Usuario jugador = new Usuario();
        session.setAttribute("jugadorActual", jugador);

        // Ejecución del metodo en el controlador
        controladorBlackjack.comenzarBlackjack(session, false, 5);

        // obtenemos los nuevos datos de la session
        List<Carta> manoJugadorObtenida = (List<Carta>) session.getAttribute("cartasJugador");
        List<Carta> manoCrupierObtenida = (List<Carta>) session.getAttribute("cartasCasa");

        // Verificación de que los datos se guardaron correctamente en la session
        assertNotNull(manoJugadorObtenida);
        assertNotNull(manoCrupierObtenida);
        assertThat(manoJugadorObtenida, equalTo(manoJugadorEsperada));
        assertThat(manoCrupierObtenida, equalTo(manoCrupierEsperada));
    }

    @Test
    public void queAlIniciarseElJuegoSeGuardeElPuntajeInicialDelJugadorEnLaSesion() {
        // datos esperados
        Integer puntajeEsperado = 15;
        List<Carta> mano = new ArrayList<>();
        mano.add(new Carta("A", 11, Palo.TREBOL));
        mano.add(new Carta("4", 4, Palo.CORAZON));

        when(servicioBlackjackMock.entregarCartasPrincipales()).thenReturn(mano);
        when(servicioBlackjackMock.calcularPuntuacion(mano)).thenReturn(puntajeEsperado);

        // datos previos
        session.setAttribute("jugadorActual", new Usuario());

        // ejecucion
        controladorBlackjack.comenzarBlackjack(session, false, 5);

        // recupero datos
        Integer puntajeObtenido = (Integer) session.getAttribute("puntaje");

        assertNotNull(puntajeObtenido);
        assertThat(puntajeObtenido, equalTo(puntajeEsperado));
    }

    @Test
    public void queSeAlIniciarseElJuegoSeGuardeElEstadoDeLaPartidaEnLaSesion() {
        // datos esperados
        EstadoPartida estadoEsperado = EstadoPartida.EN_CURSO;
        List<Carta> manoMock = new ArrayList<>();
        manoMock.add(mock(Carta.class));
        manoMock.add(mock(Carta.class));

        when(servicioBlackjackMock.entregarCartasPrincipales())
                .thenReturn(manoMock);

        when(servicioBlackjackMock.estadoPartida(manoMock, manoMock, false)).thenReturn(estadoEsperado);

        // datos previos
        session.setAttribute("jugadorActual", new Usuario());

        // ejecucion
        controladorBlackjack.comenzarBlackjack(session, false, 5);

        // recupero datos de la session
        EstadoPartida estadoObtenido = (EstadoPartida) session.getAttribute("estadoPartida");

        // verificacion
        assertNotNull(estadoObtenido);
        assertThat(estadoObtenido, equalTo(estadoEsperado));
    }

    @Test
    public void queSeAlIniciarseElJuegoSeGuardeElGanadorEnLaSesion() {
        // datos esperados
        String ganadorEsperado = "ninguno";
        List<Carta> manoMock = new ArrayList<>();
        manoMock.add(mock(Carta.class));
        manoMock.add(mock(Carta.class));

        when(servicioBlackjackMock.entregarCartasPrincipales())
                .thenReturn(manoMock).thenReturn(manoMock);

        when(servicioBlackjackMock.ganador(manoMock, manoMock, "nombre", false)).thenReturn(ganadorEsperado);

        Usuario jugador = new Usuario();
        jugador.setNombre("nombre");
        session.setAttribute("jugadorActual", jugador);

        controladorBlackjack.comenzarBlackjack(session, false, 5);

        String ganadorObtenido = (String) session.getAttribute("ganador");
        assertThat(ganadorObtenido, equalTo(ganadorEsperado));
    }

    @Test
    public void queAlIniciarseElJuegoSeGuardeElTiempoLimiteEnLaSessionSiEsUnaPartidaContraReloj() {
        // tiempo esperado (calculo formateado)
        Integer minutosConfigurados = 3;
        long tiempoLimiteMilisegundos = minutosConfigurados * 60 * 1000;
        long tiempoExpiracion = System.currentTimeMillis() + tiempoLimiteMilisegundos;
        Date fechaExpiracion = new Date(tiempoExpiracion);
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm");
        String tiempoEsperado = formato.format(fechaExpiracion);

        session.setAttribute("jugadorActual", new Usuario());

        controladorBlackjack.comenzarBlackjack(session, true, minutosConfigurados);

        String tiempoObtenido = (String) session.getAttribute("tiempoLimite");
        Boolean hayContrareloj = (Boolean) session.getAttribute("contrareloj");

        assertThat(tiempoObtenido, equalTo(tiempoEsperado));
        assertThat(hayContrareloj, equalTo(true));

    }

    @Test
    public void queAlIniciarseElJuegoNoSeGuardeUnTiempoLimiteEnLaSessionSiNoEsUnaPartidaContraReloj() {
        // ejecuto con el valor del contrareloj en false
        controladorBlackjack.comenzarBlackjack(session, false, 5);
        Boolean valorContrarelojObtenido = (Boolean) session.getAttribute("contrareloj");

        assertNull(session.getAttribute("tiempoLimite"));
        assertThat(valorContrarelojObtenido, equalTo(false));

    }

    @Test
    public void queAlIniciarseElJuegoSeInicializeLaBarajaConElValorDelAsConfiguradoEnElUsuario() {
        // valores esperados
        Integer valorAs = 1;

        // simulo un jugador en la sesion con el valor del as en 1
        Usuario jugador = new Usuario();
        jugador.getConfig().setValorDelAs(valorAs);
        session.setAttribute("jugadorActual", jugador);

        controladorBlackjack.comenzarBlackjack(session, false, 5);

        verify(servicioBlackjackMock, times(1)).inicializarBaraja(valorAs);
    }

    @Test
    public void queAlIniciarseElJuegoGuardenLasPartidasAnterioresEnLaSesion() {
        // simulo que ya hay partidas guardadas del usuario
        List<Partida> partidasEsperadas = new ArrayList<>();
        partidasEsperadas.add(new Partida());
        partidasEsperadas.add(new Partida());

        Usuario jugador = new Usuario();
        jugador.setId((long) 1);
        session.setAttribute("jugadorActual", jugador);

        try {
            when(servicioPlataformaMock.obtenerUltimasPartidasDelUsuario((long) 1, Juego.BLACKJACK))
                    .thenReturn(partidasEsperadas);
        } catch (PartidaDeUsuarioNoEncontradaException e) {

        }

        controladorBlackjack.comenzarBlackjack(session, false, 3);

        List<Partida> partidasObtenidas = (List<Partida>) session.getAttribute("partidas");

        assertNotNull(partidasObtenidas);
        assertThat(partidasObtenidas, equalTo(partidasEsperadas));

    }

    @Test
    public void queSeGuardeUnMensajeDeErrorEnElModeloSiAunNoHayPartidasAnterioresDelUsuario()
            throws PartidaDeUsuarioNoEncontradaException {
        String mensajeEsperado = "aun no hay partidas registradas.";

        when(servicioPlataformaMock.obtenerUltimasPartidasDelUsuario((long) 1, Juego.BLACKJACK))
                .thenThrow(PartidaDeUsuarioNoEncontradaException.class);

        ModelAndView mav = controladorBlackjack.comenzarBlackjack(session, false, 3);
        String mensajeObtenido = (String) mav.getModel().get("mensajePartidas");

        assertNotNull(mensajeObtenido);
        assertThat(mensajeObtenido, equalTo(mensajeEsperado));

    }

    @Test
    public void queSePuedaObtenerUnaRespuestaConTodosLosDatosIniciales() throws PartidaDeUsuarioNoEncontradaException {
        // datos esperados
        List<Carta> cartasJugadorEsperadas = new ArrayList<>();
        cartasJugadorEsperadas.add(new Carta("A", 11, Palo.CORAZON));
        cartasJugadorEsperadas.add(new Carta("2", 2, Palo.DIAMANTE));
        List<Carta> cartasCrupierEsperadas = new ArrayList<>();
        cartasCrupierEsperadas.add(new Carta("A", 11, Palo.TREBOL));
        cartasCrupierEsperadas.add(new Carta("7", 7, Palo.DIAMANTE));
        List<Partida> partidasEsperadas = new ArrayList<>();
        partidasEsperadas.add(mock(Partida.class));
        Integer puntajeEsperado = 13;
        EstadoPartida estadoEsperado = EstadoPartida.EN_CURSO;
        String ganadorEsperado = "ninguno";
        Integer minutosConfigurados = 3;
        long tiempoLimiteMilisegundos = minutosConfigurados * 60 * 1000;
        long tiempoExpiracion = System.currentTimeMillis() + tiempoLimiteMilisegundos;
        Date fechaExpiracion = new Date(tiempoExpiracion);
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm");
        String tiempoEsperado = formato.format(fechaExpiracion);
        Boolean contrarelojEsperado = true;
        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");

        when(servicioBlackjackMock.entregarCartasPrincipales()).thenReturn(cartasJugadorEsperadas)
                .thenReturn(cartasCrupierEsperadas);
        when(servicioBlackjackMock.calcularPuntuacion(cartasJugadorEsperadas)).thenReturn(puntajeEsperado);
        when(servicioPlataformaMock.obtenerUltimasPartidasDelUsuario((long) 1, Juego.BLACKJACK))
                .thenReturn(partidasEsperadas);
        when(servicioBlackjackMock.estadoPartida(cartasJugadorEsperadas, cartasCrupierEsperadas, false))
                .thenReturn(estadoEsperado);
        when(servicioBlackjackMock.ganador(cartasJugadorEsperadas, cartasCrupierEsperadas, jugador.getNombre(), false))
                .thenReturn(ganadorEsperado);

        controladorBlackjack.comenzarBlackjack(session, contrarelojEsperado, minutosConfigurados);
        // invoco el metodo recupera y devuelve los datos en una respuesta
        Map<String, Object> respuesta = controladorBlackjack.comenzarJuego(session);
        List<Carta> masoInicialJugador = (List<Carta>) respuesta.get("cartasJugador");
        List<Carta> masoInicialCrupier = (List<Carta>) respuesta.get("cartasCasa");
        List<Partida> partidas = (List<Partida>) respuesta.get("partidas");
        Boolean contrareloj = (Boolean) respuesta.get("contrareloj");
        String tiempoLimite = (String) respuesta.get("tiempoLimite");
        Integer puntaje = (Integer) respuesta.get("puntaje");
        String ganador = (String) respuesta.get("ganador");
        EstadoPartida estado = (EstadoPartida) respuesta.get("estadoPartida");

        assertNotNull(respuesta);
        assertThat(masoInicialJugador, equalTo(cartasJugadorEsperadas));
        assertThat(masoInicialCrupier, equalTo(cartasCrupierEsperadas));
        assertThat(partidas, equalTo(partidasEsperadas));
        assertThat(contrareloj, equalTo(contrarelojEsperado));
        assertThat(tiempoLimite, equalTo(tiempoEsperado));
        assertThat(puntaje, equalTo(puntajeEsperado));
        assertThat(ganador, equalTo(ganadorEsperado));
        assertThat(estado, equalTo(estadoEsperado));
    }

    @Test
    public void queSePuedaPedirUnaCarta() {
        // preparacion
        Carta cartaEsperada = new Carta("2", 2, Palo.DIAMANTE);
        when(servicioBlackjackMock.pedirCarta())
                .thenReturn(cartaEsperada);

        when(servicioBlackjackMock.entregarCartasPrincipales())
                .thenReturn(Arrays.asList(mock(Carta.class), mock(Carta.class)));

        List<Carta> cartasJugador = servicioBlackjackMock.entregarCartasPrincipales();
        List<Carta> cartasCasa = servicioBlackjackMock.entregarCartasPrincipales();
        session.setAttribute("cartasJugador", cartasJugador);
        session.setAttribute("cartasCasa", cartasCasa);
        session.setAttribute("jugadorActual", "jugador");

        // ejecucion- le paso la sesion con todo los datos nuevos.Actualiza la sesion y
        // retorna un mapa
        Map<String, Object> datosSalida = controladorBlackjack.pedirCarta(session);

        // validacion
        assertThat(datosSalida.get("cartaNueva"), equalTo(cartaEsperada));
    }

    @Test
    public void QueAlPlantarseSeActualizeElMazoDelCrupierYloEnvieEnLaRespuesta() {
        // preparacion
        Carta carta = new Carta("2", 2, Palo.CORAZON);
        List<Carta> mano = new ArrayList<>();
        mano.add(carta);
        mano.add(carta);
        List<Carta> manoFinal = new ArrayList<>();
        manoFinal.add(carta);
        manoFinal.add(carta);
        manoFinal.add(carta);
        when(servicioBlackjackMock.entregarCartasPrincipales())
                .thenReturn(Arrays.asList(carta, carta));
        when(servicioBlackjackMock.plantarse(mano)).thenReturn(manoFinal);

        controladorBlackjack.comenzarBlackjack(session, false, 0);

        // ejecucion
        Map<String, Object> datosSalida = controladorBlackjack.plantarse(session);
        List<Carta> manoFinalCRupier = (List<Carta>) datosSalida.get("manoFinalCrupier");

        assertThat(manoFinalCRupier.size(), greaterThan(2));

    }

    @Test
    public void queAlFinalizarSeGuardeLaPartidaYseVuelvaALaVistaInicial() throws PartidaConPuntajeNegativoException, IllegalArgumentException, PartidaDeBingoSinLineaNiBingoException {
        // simulamos datos de jugada
        List<Carta> mano = new ArrayList<>();
        mano.add(mock(Carta.class));
        session.setAttribute("cartasJugador", mano);
        session.setAttribute("puntaje", 12);
        session.setAttribute("ganador", "empate");
        when(servicioBlackjackMock.hayBlackjack(mano)).thenReturn(false);

        // Creamos un ArgumentCaptor para capturar la partida que se pasa al método
        // agregarPartida()
        ArgumentCaptor<PartidaBlackJack> partidaCaptor = ArgumentCaptor.forClass(PartidaBlackJack.class);

        PartidaBlackJack partidaEsperada = new PartidaBlackJack((long) 1, 12, Juego.BLACKJACK, false, true,
                LocalTime.of(3, 0));

        // Hacemos la verificación utilizando el captor
        ModelAndView mav = controladorBlackjack.finalizar(session);

        verify(servicioPlataformaMock, times(1)).agregarPartida(partidaCaptor.capture());

        PartidaBlackJack partidaCapturada = partidaCaptor.getValue();

        assertThat(partidaCapturada.getPuntaje(), equalTo(partidaEsperada.getPuntaje()));
        assertThat(partidaCapturada.getIdJugador(), equalTo((long) 1));
        assertThat(partidaCapturada.getGano(), equalTo(true));
        assertThat(mav.getViewName(), equalTo("redirect:/inicio-blackjack"));
    }

    @Test
    public void queAlReiniciarSeSeRestablescanLosValoresInicialesYSeReinicieLaVista()
            throws PartidaDeUsuarioNoEncontradaException, PartidaConPuntajeNegativoException, IllegalArgumentException, PartidaDeBingoSinLineaNiBingoException {
        // datos en sesion
        List<Carta> mano = new ArrayList<>();
        mano.add(mock(Carta.class));
        Integer minutos = 3;
        session.setAttribute("cartasJugador", mano);
        session.setAttribute("puntaje", 12);
        session.setAttribute("ganador", "empate");
        session.setAttribute("contrareloj", true);
        session.setAttribute("minutos", minutos);
        when(servicioBlackjackMock.hayBlackjack(mano)).thenReturn(false);

        // datos reiniciados esperados
        List<Partida> partidas = new ArrayList<>();
        partidas.add(mock(Partida.class));
        List<Carta> manoReiniciada = new ArrayList<>();
        manoReiniciada.add(mock(Carta.class));
        manoReiniciada.add(mock(Carta.class));
        Integer puntajeReiniciado = 12;
        EstadoPartida estadoReiniciado = EstadoPartida.EN_CURSO;
        String ganadorReiniciado = "ninguno";
        long tiempoLimiteMilisegundos = minutos * 60 * 1000;
        long tiempoExpiracion = System.currentTimeMillis() + tiempoLimiteMilisegundos;
        Date fechaExpiracion = new Date(tiempoExpiracion);
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm");
        String tiempoExpiracionReiniciado = formato.format(fechaExpiracion);

        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
        when(servicioBlackjackMock.hayBlackjack(mano)).thenReturn(false);
        when(servicioBlackjackMock.entregarCartasPrincipales()).thenReturn(manoReiniciada);
        when(servicioBlackjackMock.calcularPuntuacion(manoReiniciada)).thenReturn(puntajeReiniciado);
        when(servicioPlataformaMock.obtenerUltimasPartidasDelUsuario((long) 1, Juego.BLACKJACK)).thenReturn(partidas);
        when(servicioBlackjackMock.estadoPartida(manoReiniciada, manoReiniciada, false)).thenReturn(estadoReiniciado);
        when(servicioBlackjackMock.ganador(manoReiniciada, manoReiniciada, jugador.getNombre(), false)).thenReturn("ninguno");

        ModelAndView mav = controladorBlackjack.reiniciar(session);
        assertThat(mav.getViewName(), equalTo("blackjack"));
        assertThat(session.getAttribute("cartasJugador"), equalTo(manoReiniciada));
        assertThat(session.getAttribute("cartasCasa"), equalTo(manoReiniciada));
        assertThat(session.getAttribute("puntaje"), equalTo(puntajeReiniciado));
        assertThat(session.getAttribute("partidas"), equalTo(partidas));
        assertThat(session.getAttribute("estadoPartida"), equalTo(estadoReiniciado));
        assertThat(session.getAttribute("ganador"), equalTo(ganadorReiniciado));
        assertThat(session.getAttribute("partidas"), equalTo(partidas));
        assertThat(session.getAttribute("tiempoLimite"), equalTo(tiempoExpiracionReiniciado));
    }

}