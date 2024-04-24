package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BlackjackTest {

    @Test
    public void queSeaBlackJack() {
        // given cartas,servicioblackjack
        Carta carta1 = new Carta("J", 10, Palo.CORAZON);
        Carta carta2 = new Carta("A", 11, Palo.PICA);

        ServicioBlackjack serviciobj = new ServicioBlackjack();

        List<Carta> cartasJugador = new ArrayList<Carta>();

        cartasJugador.add(carta1);
        cartasJugador.add(carta2);

        boolean blackJack = serviciobj.hayBlackjack(cartasJugador);

        assertTrue(blackJack);

    }

    @Test
    public void queSePierdaLaPartidaSiSePasaElPuntaje() {
        Carta carta1 = new Carta("J", 10, Palo.CORAZON);
        Carta carta2 = new Carta("Q", 10, Palo.PICA);
        Carta carta3 = new Carta("4", 4, Palo.DIAMANTE);

        ServicioBlackjack serviciobj = new ServicioBlackjack();

        List<Carta> cartasJugador = new ArrayList<Carta>();

        cartasJugador.add(carta1);
        cartasJugador.add(carta2);
        cartasJugador.add(carta3);

        Boolean perdio = serviciobj.Perdio(cartasJugador);

        assertTrue(perdio);

    }

    @Test
    public void entregarCartasInciales() {
        List<Carta> cartas = new ArrayList<Carta>();
        ServicioBlackjack serviciobj = new ServicioBlackjack();
        cartas = serviciobj.entregarCartasPrincipales();
        assertEquals(2, cartas.size());
    }

    

}
