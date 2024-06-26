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
    public void queSePuedaInicializarLaBarajaConUnValorDeAsDeterminado() {
        Integer valorAs = 11;
        servicio.inicializarBaraja(valorAs);
        assertThat(servicio.getBaraja().getValorAs(), equalTo(valorAs));
    }

    @Test
    public void queSeentreguenDosCartasIniciales() {
        Integer valorAs = 11;
        servicio.inicializarBaraja(valorAs);
        cartasJugador = servicio.entregarCartasPrincipales();
        assertThat(cartasJugador.size(), equalTo(2));
    }


    @Test
    public void queSeentreguenUnaManoDeVeinte() {
        Integer valorAs = 1;
        servicio.inicializarBaraja(valorAs);
        cartasCrupier = servicio.entregarManoDeVeinte();
        assertThat(cartasCrupier.size(), equalTo(2));
        assertThat(servicio.calcularPuntuacion(cartasCrupier), equalTo(20));;
    }

    @Test
    public void queSePuedacalcularElPuntajeDeUnaMano() {
        cartasJugador.add(new Carta("A", 11, Palo.PICA));
        cartasJugador.add(new Carta("4", 4, Palo.DIAMANTE));
        assertThat(servicio.calcularPuntuacion(cartasJugador), equalTo(15));
    }

    @Test
    public void queSePuedaPedirUnaCarta() {
        servicio.inicializarBaraja(11);
        cartasJugador.add(servicio.pedirCarta());
        assertThat(cartasJugador.size(), equalTo(1));
    }

    @Test
    public void queAlPlantarseElJugadorSeActualizeElMazoDelCrupier() {
        servicio.inicializarBaraja(11);
        cartasCrupier.add(new Carta("3", 3, Palo.CORAZON));
        cartasCrupier.add(new Carta("3", 3, Palo.DIAMANTE));
        List<Carta> manoDelCrupierAntesDePlantarse = new ArrayList<Carta>(cartasCrupier);

        cartasCrupier.addAll(servicio.plantarse(cartasCrupier));
        List<Carta> manoDelCrupierDespuesDePlantarse = new ArrayList<Carta>(cartasCrupier);

        assertThat(servicio.calcularPuntuacion(manoDelCrupierAntesDePlantarse), lessThan(17));
        assertThat(servicio.calcularPuntuacion(manoDelCrupierDespuesDePlantarse), greaterThanOrEqualTo(17));
        ;
    }

    @Test
    public void queSePuedaVerificarQueUnaManoSeaBlackJack() {
        cartasJugador.add(new Carta("J", 10, Palo.CORAZON));
        cartasJugador.add(new Carta("A", 11, Palo.PICA));
        assertThat(servicio.hayBlackjack(cartasJugador), is(true));
    }

    @Test
    public void queElEstadoDeLaPartidaSeaFinalizadoSiLaManoDelJugadorSePasaAntesDePlantarse() {
        cartasJugador.add(new Carta("J", 10, Palo.CORAZON));
        cartasJugador.add(new Carta("J", 10, Palo.CORAZON));
        cartasJugador.add(new Carta("5", 5, Palo.CORAZON));
        cartasCrupier.add(new Carta("5", 5, Palo.CORAZON));
        cartasCrupier.add(new Carta("3", 3, Palo.DIAMANTE));
        EstadoPartida estado = servicio.estadoPartida(cartasJugador, cartasCrupier, false);
        assertThat(estado, equalTo(EstadoPartida.FINALIZADA));
    }

    @Test
    public void queElEstadoDeLaPartidaSeaFinalizadaSiElUsuarioHaceBlackjackAntesDePlantarse() {
        cartasJugador.add(new Carta("10", 10, Palo.PICA));
        cartasJugador.add(new Carta("5", 5, Palo.CORAZON));
        cartasJugador.add(new Carta("6", 6, Palo.CORAZON));
        Boolean plantado = false;
        EstadoPartida estado = servicio.estadoPartida(cartasJugador, cartasCrupier, plantado);
        assertThat(estado, equalTo(EstadoPartida.FINALIZADA));
    }

    @Test
    public void queElEstadoDeLaPartidaSeaFinalizadaSiElUsuarioSePlanta() {
        cartasJugador.add(new Carta("5", 5, Palo.PICA));
        cartasJugador.add(new Carta("2", 2, Palo.CORAZON));
        cartasCrupier.add(new Carta("6", 6, Palo.CORAZON));
        cartasJugador.add(new Carta("J", 10, Palo.DIAMANTE));
        Boolean plantado = true;
        EstadoPartida estado = servicio.estadoPartida(cartasJugador, cartasCrupier, plantado);
        assertThat(estado, equalTo(EstadoPartida.FINALIZADA));
    }

    @Test
    public void queElGanadorDeLaPartidaSeaElCrupierSiLaManoDelJugadorSePasaDelLimiteAntesDePlantarse() {
        cartasJugador.add(new Carta("4", 4, Palo.CORAZON));
        cartasJugador.add(new Carta("A", 11, Palo.PICA));
        cartasJugador.add(new Carta("A", 11, Palo.DIAMANTE));
        cartasCrupier.add(new Carta("5", 5, Palo.CORAZON));
        cartasCrupier.add(new Carta("3", 3, Palo.DIAMANTE));
        String ganador = servicio.ganador(cartasJugador, cartasCrupier, "jugador", false);
        assertThat(ganador, equalTo("casa"));
    }

    @Test
    public void queElGanadorDeLaPartidaSeaElJugadorAlPlantarseConUnPuntajeMayor() {
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
    }

    @Test
    public void queElGanadorSeaElUsuarioSiSuManoHaceBlackjackAntesDePlantarse() {
        cartasJugador.add(new Carta("A", 11, Palo.DIAMANTE));
        cartasJugador.add(new Carta("J", 10, Palo.CORAZON));
        cartasCrupier.add(new Carta("8", 8, Palo.PICA));
        cartasCrupier.add(new Carta("2", 2, Palo.CORAZON));
        Boolean plantado = false;
        String ganador = servicio.ganador(cartasJugador, cartasCrupier, "jugador", plantado);
        assertThat(ganador, equalToIgnoringCase("jugador"));
    }

    @Test
    public void queHayaEmpateSiElPuntajeDeAmbosJugadoreEsIgualAlPlantarseElUsuario() {
        cartasJugador.add(new Carta("8", 8, Palo.DIAMANTE));
        cartasJugador.add(new Carta("2", 2, Palo.TREBOL));
        cartasCrupier.add(new Carta("8", 8, Palo.PICA));
        cartasCrupier.add(new Carta("2", 2, Palo.CORAZON));
        Boolean plantado = true;
        String ganador = servicio.ganador(cartasJugador, cartasCrupier, "jugador", plantado);
        assertThat(ganador, equalToIgnoringCase("empate"));
    }

}
