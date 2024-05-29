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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Part;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.PartidaDeUsuarioNoEncontradaException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    public void queSeAlIniciarseElJuegoSeGuardenEnLaSesionLasManosInicialesDelJugadorYelCrupier() {
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
        Baraja barajaEsperada = new Baraja(valorAs);

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

    // otro si no hay en el modelo verificar mensaje de error

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
    public void QueAlPlantarseSeActualizeElMazoDelCrupier() {
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

}