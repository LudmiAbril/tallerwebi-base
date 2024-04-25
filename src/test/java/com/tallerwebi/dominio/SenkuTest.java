package com.tallerwebi.dominio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SenkuTest {

    @Test
    public void queSePuedaCrearUnSenku() {
    //TEST POR COSTUMBRE PARA EMPEZAR A MODELAR LAS CLASES PRINCIPALES

        //Given
        Senku nuevo = new Senku();
        assertNotNull(nuevo);
    }
}
