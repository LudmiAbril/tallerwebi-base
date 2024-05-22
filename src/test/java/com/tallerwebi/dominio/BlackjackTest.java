package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tallerwebi.infraestructura.ServicioBlackjackImpl;

public class BlackjackTest {
    private ServicioBlackjack servicio;
    List<Carta> cartasJugador;
    List<Carta> cartasCrupier;

    @BeforeEach
    public void init() {
        this.servicio = new ServicioBlackjackImpl();
        this.cartasJugador = new ArrayList<>();
        this.cartasCrupier = new ArrayList<>();
    }

    @Test
    public void entregarCartasIniciales() {
        cartasJugador = servicio.entregarCartasPrincipales();
        assertThat(cartasJugador.size(), equalTo(2));
    }

    @Test
    public void calcularPuntaje() {
        cartasJugador.add(new Carta("A", 11, Palo.PICA));
        cartasJugador.add(new Carta("4", 4, Palo.DIAMANTE));
        assertThat(servicio.calcularPuntuacion(cartasJugador), equalTo(15));
    }

    @Test
    public void pedirUnaCarta() {
        cartasJugador.add(servicio.pedirCarta());
        assertThat(cartasJugador.size(), equalTo(1));
    }

    @Test
    public void queAlPlantarseSeActualizeElMazoDelCrupier() {
        cartasCrupier.add(new Carta("3", 3, Palo.CORAZON));
        cartasCrupier.add(new Carta("3", 3, Palo.DIAMANTE));
        Integer valorMano1 = servicio.calcularPuntuacion(cartasCrupier);
        cartasCrupier.addAll(servicio.plantarse(cartasCrupier));
        Integer valorMano = servicio.calcularPuntuacion(cartasCrupier);
        assertThat(valorMano, greaterThanOrEqualTo(17));
    }

    @Test
    public void queUnaManoSeaBlackJack() {
        cartasJugador.add(new Carta("J", 10, Palo.CORAZON));
        cartasJugador.add(new Carta("A", 11, Palo.PICA));
        assertThat(servicio.hayBlackjack(cartasJugador), is(true));
    }

    @Test
    public void queFinalizeLaPartidaSiSePasaElPuntajeDelJugador() {
        cartasJugador.add(new Carta("J", 10, Palo.CORAZON));
        cartasJugador.add(new Carta("A", 11, Palo.PICA));
        cartasJugador.add(new Carta("5", 5, Palo.CORAZON));
        cartasCrupier.add(new Carta("5", 5, Palo.CORAZON));
        cartasCrupier.add(new Carta("3", 3, Palo.DIAMANTE));
        EstadoPartida estado = servicio.estadoPartida(cartasJugador, cartasCrupier, false);
        assertThat(estado, equalTo(EstadoPartida.FINALIZADA));
    }

    @Test
    public void queFinalizeLaPartidaAlPlantarse() {
        cartasJugador.add(new Carta("5", 5, Palo.CORAZON));
        cartasCrupier.add(new Carta("5", 5, Palo.CORAZON));
        Boolean plantado = true;
        EstadoPartida estado = servicio.estadoPartida(cartasJugador, cartasCrupier, plantado);
        assertThat(estado, equalTo(EstadoPartida.FINALIZADA));
    }

    @Test
    public void queGaneElJugadorAlPlantarsePorPuntajeMayor() {
        cartasJugador.add(new Carta("A", 11, Palo.DIAMANTE));
        cartasJugador.add(new Carta("6", 6, Palo.DIAMANTE));
        cartasCrupier.add(new Carta("A", 11, Palo.PICA));
        cartasCrupier.add(new Carta("2", 2, Palo.PICA));
        Boolean plantado = true;
        String ganador = servicio.ganador(cartasJugador, cartasCrupier, "jugador", plantado);
        assertThat(ganador, equalToIgnoringCase("jugador"));
    }

    @Test
    public void queGaneElCrupierAlPlantarseElJugadorConUnPuntajeMenor() {
        cartasJugador.add(new Carta("3", 3, Palo.DIAMANTE));
        cartasJugador.add(new Carta("6", 6, Palo.DIAMANTE));
        cartasCrupier.add(new Carta("8", 8, Palo.PICA));
        cartasCrupier.add(new Carta("2", 2, Palo.PICA));
        cartasCrupier.add(new Carta("5", 5, Palo.PICA));
        cartasCrupier.add(new Carta("2", 3, Palo.PICA));
        Boolean plantado = true;
        String ganador = servicio.ganador(cartasJugador, cartasCrupier, "jugador", plantado);
        assertThat(ganador, equalToIgnoringCase("casa"));
    }}
