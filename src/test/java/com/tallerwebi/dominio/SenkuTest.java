package com.tallerwebi.dominio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SenkuTest {

    @Test
    public void queSePuedaCrearUnSenku() {
    //TEST POR COSTUMBRE PARA EMPEZAR A MODELAR LAS CLASES PRINCIPALES
        //Given
        Senku nuevo = new Senku(7);
        assertNotNull(nuevo);
    }

    @Test
    public void queSePuedaCrearUnSenkuCuyoTableroSea5x5() {

        //Given
        Senku nuevo = new Senku(5);
        Tablero tablero = nuevo.getTablero();
        assertEquals(5, tablero.getCantidadFilasYColumnas());
    }


    @Test(expected= IllegalArgumentException.class)
    public void queNoSePuedaCrearUnSenkuCuyoTableroSea5x5() {
        //Given
        Senku nuevo = new Senku(5);
        Tablero tablero = nuevo.getTablero();
        assertEquals(5, tablero.getCantidadFilasYColumnas());
    }
}
