package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.CasilleroInexistenteException;
import com.tallerwebi.dominio.excepcion.CasilleroVacio;
import com.tallerwebi.infraestructura.ServicioSenkuImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SenkuTest {

    //TEST POR COSTUMBRE PARA EMPEZAR A MODELAR LAS CLASES PRINCIPALES
    @Test
    public void queSePuedaCrearUnSenku() {
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

    @Test
    public void queNoSePuedaCrearUnSenkuCuyoTableroNoSeaImpar() {
        assertThrows(IllegalArgumentException.class, () -> {
            Senku nuevo = new Senku(4);
        });
    }

    @Test
    public void queSePuedaSeleccionarUnCasilleroMedianteCoordenadas() throws CasilleroVacio, CasilleroInexistenteException {
     //GIVEN
        // TENGO UN TABLERO
        Senku nuevo = new Senku(5);
        Tablero tablero = nuevo.getTablero();
        ServicioSenkuImpl servicio= new ServicioSenkuImpl();
        //CREO CASILLERO
        Casillero esperado=new Casillero(4,2);
    //WHEN
        //SELECCIONO FICHA
        Casillero seleccionado=servicio.seleccionarCasillero(tablero,4,2);
        assertEquals(esperado.getCoordenadaX(),seleccionado.getCoordenadaX());
    }

    @Test
    public void queNoSePuedaSeleccionarUnCasilleroMedianteCoordenadasInvalidas() {
        // GIVEN
        // TENGO UN TABLERO
        Senku nuevo = new Senku(5);
        Tablero tablero = nuevo.getTablero();
        ServicioSenkuImpl servicio = new ServicioSenkuImpl();

        // WHEN
        // SELECCIONO FICHA INVALIDA QUE SE VAYA DE LAS COORDENADAS
        assertThrows(CasilleroInexistenteException.class, () -> {
            servicio.seleccionarCasillero(tablero, 4, 8);
        });
    }

}
