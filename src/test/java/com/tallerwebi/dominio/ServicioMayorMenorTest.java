package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.ServicioBingoImpl;
import com.tallerwebi.infraestructura.ServicioMayorMenorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioMayorMenorTest {
    private ServicioMayorMenor servicioMayorMenor;
    private HttpSession session;

    @BeforeEach
    public void init() {
        this.servicioMayorMenor = new ServicioMayorMenorImpl();
        session = new MockHttpSession();
    }

    @Test
    public void queLaPrimeraCartaNoSeaNula(){
        Carta recibida = servicioMayorMenor.entregarCartaDelMedio();
        assertThat(recibida, is(notNullValue()));
    }
    @Test
    public void queElMazoInicieCon56Cartas(){
        servicioMayorMenor.inicializarBaraja();
        Integer cantidadCartas = servicioMayorMenor.cantidadCartasRestantesEnMazo();
        assertThat(cantidadCartas, is(52));
    }
    @Test
    public void queSacarCartaNoArrojeNull(){
        servicioMayorMenor.inicializarBaraja();
        Carta nuevaCarta = servicioMayorMenor.sacarCarta();
        assertThat(nuevaCarta, is(notNullValue()));
    }
    @Test
    public void testBarajar() {
        servicioMayorMenor.inicializarBaraja();
        Baraja originalBaraja = servicioMayorMenor.getBaraja();
        List<Carta> cartasOriginales = new ArrayList<>(originalBaraja.getCartas());
        servicioMayorMenor.barajar();
        Baraja barajaBarajada = servicioMayorMenor.getBaraja();
        assertNotEquals(cartasOriginales, barajaBarajada.getCartas());
        boolean alMenosUnaDiferente = false;
        for (int i = 0; i < cartasOriginales.size(); i++) {
            if (!cartasOriginales.get(i).equals(barajaBarajada.getCartas().get(i))) {
                alMenosUnaDiferente = true;
                break;
            }
        }
        assertThat(alMenosUnaDiferente, is(true));
    }
    @Test
    public void queEvaluarCartaDevuelvaVerdaderoSiLaCartaNuevaEsAcertadaPorElegirMayor(){
        servicioMayorMenor.inicializarBaraja();
        Carta cartaDelMedio = new Carta("5", 5, Palo.DIAMANTE);
        Valor elegido = Valor.MAYOR;
        Carta cartaNueva = new Carta("8", 8, Palo.DIAMANTE);
        assertThat(servicioMayorMenor.evaluarCarta(cartaDelMedio, cartaNueva, elegido), is(true));
    }
    @Test
    public void queEvaluarCartaDevuelvaFalsoSiLaCartaNuevaNoEsAcertadaPorElegirMayor(){
        servicioMayorMenor.inicializarBaraja();
        Carta cartaDelMedio = new Carta("5", 5, Palo.DIAMANTE);
        Valor elegido = Valor.MAYOR;
        Carta cartaNueva = new Carta("3", 3, Palo.DIAMANTE);
        assertThat(servicioMayorMenor.evaluarCarta(cartaDelMedio, cartaNueva, elegido), is(false));
    }
    @Test
    public void queEvaluarCartaDevuelvaVerdaderoSiLaCartaNuevaEsAcertadaPorElegirMenor(){
        servicioMayorMenor.inicializarBaraja();
        Carta cartaDelMedio = new Carta("5", 5, Palo.DIAMANTE);
        Valor elegido = Valor.MENOR;
        Carta cartaNueva = new Carta("3", 3, Palo.DIAMANTE);
        assertThat(servicioMayorMenor.evaluarCarta(cartaDelMedio, cartaNueva, elegido), is(true));
    }
    @Test
    public void queEvaluarCartaDevuelvaFalsoSiLaCartaNuevaNoEsAcertadaPorElegirMenor(){
        servicioMayorMenor.inicializarBaraja();
        Carta cartaDelMedio = new Carta("5", 5, Palo.DIAMANTE);
        Valor elegido = Valor.MAYOR;
        Carta cartaNueva = new Carta("8", 8, Palo.DIAMANTE);
        assertThat(servicioMayorMenor.evaluarCarta(cartaDelMedio, cartaNueva, elegido), is(true));
    }

    @Test
    public void queLaBarajaContengaLasCartasCorrectas() {
        servicioMayorMenor.inicializarBaraja();
        List<Carta> cartas = servicioMayorMenor.getBaraja().getCartas();
        assertThat(cartas.size(), is(52));
        assertThat(cartas, hasItem(new Carta("A", 11, Palo.DIAMANTE)));
        assertThat(cartas, hasItem(new Carta("K", 10, Palo.PICA)));
        assertThat(cartas, hasItem(new Carta("Q", 10, Palo.TREBOL)));
        assertThat(cartas, hasItem(new Carta("J", 10, Palo.CORAZON)));
    }
    @Test
    public void queActualizarEstadoPartidaNoSeaNulo() {
        Carta nuevaCarta = new Carta("5", 5, Palo.DIAMANTE);
        servicioMayorMenor.actualizarEstadoPartida(session, nuevaCarta, true);
        assertThat(session.getAttribute("estadoPartida"), is(notNullValue()));
    }

    @Test
    public void queElPuntajeInicialSeaCero() {
        assertThat(servicioMayorMenor.getPuntaje(), is(0));
    }
    @Test
    public void queEvaluarCartaDevuelvaFalsoSiLaCartaNuevaEsIgual() {
        servicioMayorMenor.inicializarBaraja();
        Carta cartaDelMedio = new Carta("5", 5, Palo.DIAMANTE);
        Valor elegido = Valor.MAYOR;
        Carta cartaNueva = new Carta("5", 5, Palo.DIAMANTE);
        assertThat(servicioMayorMenor.evaluarCarta(cartaDelMedio, cartaNueva, elegido), is(false));
    }
    @Test
    public void queElMazoDisminuyaUnaCartaAlSacarUna() {
        servicioMayorMenor.inicializarBaraja();
        int tamanioInicial = servicioMayorMenor.cantidadCartasRestantesEnMazo();
        servicioMayorMenor.sacarCarta();
        int tamanioFinal = servicioMayorMenor.cantidadCartasRestantesEnMazo();
        assertThat(tamanioFinal, is(tamanioInicial - 1));
    }

}
