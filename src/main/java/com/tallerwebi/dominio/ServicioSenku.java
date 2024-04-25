package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.CasilleroInexistenteException;
import com.tallerwebi.dominio.excepcion.CasilleroVacio;
import com.tallerwebi.dominio.excepcion.MovimientoInvalidoException;

public interface ServicioSenku {

    Casillero seleccionarCasillero(Tablero tablero,Integer x,Integer y) throws CasilleroVacio, CasilleroInexistenteException;

    Casillero getCasillero(Tablero tablero, Integer x, Integer y) throws CasilleroInexistenteException;
    void realizarMovimiento(Tablero tablero, Casillero seleccionado, Casillero destino) throws MovimientoInvalidoException;
    Boolean validarQueHayaMovimientosValidosDisponibles(Tablero tablero) throws MovimientoInvalidoException;
    Boolean hayMovimientosValidosDisponiblesDesdeCasillero(Tablero tablero, Casillero casilleroActual) throws MovimientoInvalidoException;
}
