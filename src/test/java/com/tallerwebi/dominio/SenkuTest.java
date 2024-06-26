package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.CasilleroInexistenteException;
import com.tallerwebi.dominio.excepcion.CasilleroVacio;
import com.tallerwebi.dominio.excepcion.MovimientoInvalidoException;
import com.tallerwebi.infraestructura.ServicioSenkuImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SenkuTest {


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
        //THEN
        assertEquals(5, tablero.getCantidadFilasYColumnas());
    }

    @Test
    public void queSePuedaCrearUnSenkuCuyoCasilleroCentralEsteVacio() throws CasilleroInexistenteException, CasilleroVacio {
        //Given
        Senku nuevo = new Senku(5);
        Tablero tablero = nuevo.getTablero();
        ServicioSenkuImpl servicio= new ServicioSenkuImpl();
        //WHEN
        //(0, 0), (0, 1), (0, 2), (0, 3), (0, 4)
        //(1, 0), (1, 1), (1, 2), (1, 3), (1, 4)
        //(2, 0), (2, 1),*(2, 2)*, (2, 3), (2, 4)
        //(3, 0), (3, 1), (3, 2), (3, 3), (3, 4)
        //(4, 0), (4, 1), (4, 2), (4, 3), (4, 4)
        Casillero seleccionadoOcupado=servicio.seleccionarCasillero(tablero,4,2);
        Casillero seleccionadoVacio=servicio.getCasillero(tablero,2,2);
        //THEN
        assertTrue(seleccionadoOcupado.getOcupado());
        assertFalse(seleccionadoVacio.getOcupado());

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
        Senku nuevo = new Senku(5);
        Tablero tablero = nuevo.getTablero();
        ServicioSenkuImpl servicio= new ServicioSenkuImpl();
        Casillero esperado=new Casillero(4,2);
    //WHEN
        Casillero seleccionado=servicio.seleccionarCasillero(tablero,4,2);
    //THEN
        assertEquals(esperado.getCoordenadaX(),seleccionado.getCoordenadaX());
    }

    @Test
    public void queNoSePuedaSeleccionarUnCasilleroMedianteCoordenadasInvalidas() {
        // GIVEN
        // TENGO UN TABLERO
        Senku nuevo = new Senku(5);
        Tablero tablero = nuevo.getTablero();
        ServicioSenkuImpl servicio = new ServicioSenkuImpl();

        // WHEN-//THEN
        // SELECCIONO FICHA INVALIDA QUE SE VAYA DE LAS COORDENADAS
        assertThrows(CasilleroInexistenteException.class, () -> {
            servicio.seleccionarCasillero(tablero, 4, 8);
        });
    }
    @Test
    public void queNoSePuedaSeleccionarUnCasilleroSiEstaVacio() {
        // GIVEN
        // TENGO UN TABLERO
        Senku nuevo = new Senku(5);
        Tablero tablero = nuevo.getTablero();
        ServicioSenkuImpl servicio = new ServicioSenkuImpl();

        // WHEN-//THEN
        //(0, 0), (0, 1), (0, 2), (0, 3), (0, 4)
        //(1, 0), (1, 1), (1, 2), (1, 3), (1, 4)
        //(2, 0), (2, 1),*(2, 2)*, (2, 3), (2, 4)
        //(3, 0), (3, 1), (3, 2), (3, 3), (3, 4)
        //(4, 0), (4, 1), (4, 2), (4, 3), (4, 4)
        // SELECCIONO FICHA INVALIDA QUE SE VAYA DE LAS COORDENADAS
        assertThrows(CasilleroVacio.class, () -> {
            servicio.seleccionarCasillero(tablero, 2, 2);
        });
    }

    @Test
    public void queSePuedaRealizarUnMovimiento() throws CasilleroInexistenteException, CasilleroVacio, MovimientoInvalidoException {
        // GIVEN
        // TENGO UN TABLERO
        Senku nuevo = new Senku(5);
        Tablero tablero = nuevo.getTablero();
        ServicioSenkuImpl servicio = new ServicioSenkuImpl();
        // WHEN-
        //(0, 0), (0, 1), (0, 2), (0, 3), (0, 4)
        //(1, 0), (1, 1), (1, 2), (1, 3), (1, 4)
        //(2, 0), (2, 1),*(2, 2)*, (2, 3), (2, 4)
        //(3, 0), (3, 1), (3, 2), (3, 3), (3, 4)
        //(4, 0), (4, 1), (4, 2), (4, 3), (4, 4)
        Casillero seleccionado=servicio.seleccionarCasillero(tablero,4,2);
        Casillero destino= servicio.getCasillero(tablero,2,2);
        servicio.realizarMovimiento(tablero,seleccionado,destino);

        //THEN
        //SI MOVI LA (4,2) A LA (2,2) ENTONCES AHORA (3,2) VA A ESTAR VACIO Y (2,2) OCUPADO Y (4,2) VACIO
        assertTrue(servicio.seleccionarCasillero(tablero,2,2).getOcupado());
        assertFalse(servicio.getCasillero(tablero,3,2).getOcupado());
        assertFalse(servicio.getCasillero(tablero,4,2).getOcupado());

    }
    @Test
    public void queNoSePuedaRealizarUnMovimientoSiElCasilleroDestinoNoEstaVacio() throws CasilleroInexistenteException, CasilleroVacio, MovimientoInvalidoException {
        // GIVEN
        // TENGO UN TABLERO
        Senku nuevo = new Senku(5);
        Tablero tablero = nuevo.getTablero();
        ServicioSenkuImpl servicio = new ServicioSenkuImpl();
        // WHEN-
        //(0, 0), (0, 1), (0, 2), (0, 3), (0, 4)
        //(1, 0), (1, 1), (1, 2), (1, 3), (1, 4)
        //(2, 0), (2, 1),*(2, 2)*, (2, 3), (2, 4)
        //(3, 0), (3, 1), (3, 2), (3, 3), (3, 4)
        //(4, 0), (4, 1), (4, 2), (4, 3), (4, 4)
        Casillero seleccionado=servicio.seleccionarCasillero(tablero,0,4);
        Casillero destino= servicio.getCasillero(tablero,2,4);

        //THEN
        assertThrows(MovimientoInvalidoException.class, () -> {
            servicio.realizarMovimiento(tablero,seleccionado,destino);
        });

    }
    @Test
    public void queNoSePuedaRealizarUnMovimientoSiElCasilleroDelMedioEstaVacio() throws CasilleroInexistenteException, CasilleroVacio, MovimientoInvalidoException {
        // GIVEN
        // TENGO UN TABLERO
        Senku nuevo = new Senku(5);
        Tablero tablero = nuevo.getTablero();
        ServicioSenkuImpl servicio = new ServicioSenkuImpl();
        // WHEN-
        //(0, 0), (0, 1), (0, 2), (0, 3), (0, 4)
        //(1, 0), (1, 1), (1, 2), (1, 3), (1, 4)
        //(2, 0), (2, 1),*(2, 2)*, (2, 3), (2, 4)
        //(3, 0), (3, 1), (3, 2), (3, 3), (3, 4)
        //(4, 0), (4, 1), (4, 2), (4, 3), (4, 4)
        Casillero seleccionado=servicio.seleccionarCasillero(tablero,4,2);
        Casillero destino= servicio.getCasillero(tablero,2,2);
        servicio.realizarMovimiento(tablero,seleccionado,destino);
        //SI MOVI LA (4,2) A LA (2,2) ENTONCES AHORA (3,2) VA A ESTAR VACIO Y (2,2) OCUPADO Y (4,2) VACIO
        Casillero seleccionado2=servicio.seleccionarCasillero(tablero,2,2);
        Casillero destino2= servicio.getCasillero(tablero,4,2);
        assertThrows(MovimientoInvalidoException.class, () -> {
            servicio.realizarMovimiento(tablero,seleccionado2,destino2);
        });

    }

    @Test
    public void queElJuegoAviseQueYaNoHayMovimientosValidos() throws MovimientoInvalidoException, CasilleroInexistenteException, CasilleroVacio {
        // GIVEN
        Tablero tablero = new Tablero(3);
         //(0, 0), (0, 1), (0, 2)
        //(1, 0), (1, 1), (1, 2)
        //(2, 0), (2, 1),(2, 2)
        
        ServicioSenkuImpl servicioSenku = new ServicioSenkuImpl();
        Casillero seleccionado, destino;
        // WHEN
        boolean hayMovimientosValidos = tablero.hayMovimientoDisponibleEnTablero();
    
        // THEN
        assertFalse(hayMovimientosValidos);
    }
    
    
    @Test
    public void queElJuegoSepaQueYaGanaste() throws CasilleroInexistenteException, CasilleroVacio, MovimientoInvalidoException {
        // GIVEN
        Senku nuevo = new Senku(5);
        Tablero tablero = nuevo.getTablero();
        ServicioSenkuImpl servicio = new ServicioSenkuImpl();
        // WHEN-"NO HAY FICHAS""
        resetearCasilleros(tablero);
        //THEN
        Boolean ganaste=servicio.seGano(tablero);
        assertTrue(ganaste);
    }
    private void resetearCasilleros(Tablero tablero) {
        Casillero[][] casilleros = tablero.getCasilleros();
        for (Casillero[] casillero : casilleros) {
            for (Casillero value : casillero) {
                value.setOcupado(false);
            }
        }
    }
    @Test
    public void queElJuegoSepaQueNoGanaste() throws CasilleroInexistenteException, CasilleroVacio, MovimientoInvalidoException {
        // GIVEN
        Senku nuevo = new Senku(5);
        Tablero tablero = nuevo.getTablero();
        ServicioSenkuImpl servicio = new ServicioSenkuImpl();
        // WHEN-" HAY FICHAS""

        //THEN
        Boolean ganaste=servicio.seGano(tablero);
        assertFalse(ganaste);
    }

}
