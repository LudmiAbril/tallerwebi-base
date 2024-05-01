package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Casillero;
import com.tallerwebi.dominio.ServicioSenku;
import com.tallerwebi.dominio.Tablero;
import com.tallerwebi.dominio.excepcion.CasilleroInexistenteException;
import com.tallerwebi.dominio.excepcion.CasilleroVacio;
import com.tallerwebi.dominio.excepcion.MovimientoInvalidoException;

public class ServicioSenkuImpl implements ServicioSenku {



    @Override
    public Casillero seleccionarCasillero(Tablero tablero, Integer x, Integer y) throws CasilleroVacio, CasilleroInexistenteException {
        Casillero buscado = tablero.buscarCasilleroPorCoordenadas(x, y);
        boolean noHayFicha = !buscado.getOcupado();
        if (noHayFicha) {
            throw new CasilleroVacio("No hay ficha para mover");
        }

        return buscado;
    }

    @Override
    public Casillero getCasillero(Tablero tablero, Integer x, Integer y) throws CasilleroInexistenteException {
        Casillero buscado = tablero.buscarCasilleroPorCoordenadas(x, y);
        return buscado;
    }

    @Override
    public void realizarMovimiento(Tablero tablero, Casillero seleccionado, Casillero destino) throws MovimientoInvalidoException {
        validarMovimiento(tablero, seleccionado, destino);
        ejecutarMovimiento(tablero, seleccionado, destino);
    }

    private void validarMovimiento(Tablero tablero, Casillero seleccionado, Casillero destino) throws MovimientoInvalidoException {
        tablero.validarMovimiento(seleccionado, destino);
    }

    private void ejecutarMovimiento(Tablero tablero, Casillero seleccionado, Casillero destino) {
        tablero.realizarMovimiento(seleccionado, destino);
    }

}
