package com.tallerwebi.infraestructura;

import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.Casillero;
import com.tallerwebi.dominio.ServicioSenku;
import com.tallerwebi.dominio.Tablero;
import com.tallerwebi.dominio.excepcion.CasilleroInexistenteException;
import com.tallerwebi.dominio.excepcion.CasilleroVacio;
import com.tallerwebi.dominio.excepcion.MovimientoInvalidoException;

@Service("servicioSenku")
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

    @Override
    public Boolean validarQueHayaMovimientosValidosDisponibles(Tablero tablero) throws MovimientoInvalidoException {
        Casillero[][] casillerosEnRevision = tablero.getCasilleros();
        int cantidadFilasYColumnas = tablero.getCantidadFilasYColumnas();


        for (int i = 0; i < cantidadFilasYColumnas; i++) {
            for (int j = 0; j < cantidadFilasYColumnas; j++) {
                Casillero casilleroActual = casillerosEnRevision[i][j];

                //REVISO SI ES POSIBLE REALIZAR MOVIMIENTO DESDE ESTE CASILLERO
                if (hayMovimientosValidosDisponiblesDesdeCasillero(tablero, casilleroActual)) {
                    return true; //SI HAY AL MENOS 1 MOV, YA ES TRUE
                }
            }
        }


        return false;
    }

    @Override
    public Boolean hayMovimientosValidosDisponiblesDesdeCasillero(Tablero tablero, Casillero casilleroActual) throws MovimientoInvalidoException {
        return tablero.hayMovimientoDisponibleEnTablero(casilleroActual);
    }

    public void validarMovimiento(Tablero tablero, Casillero seleccionado, Casillero destino) throws MovimientoInvalidoException {
        tablero.validarMovimiento(seleccionado, destino);
    }

    private void ejecutarMovimiento(Tablero tablero, Casillero seleccionado, Casillero destino) {
        tablero.realizarMovimiento(seleccionado, destino);
    }

    public Boolean seGano(Tablero tablero) {
       return tablero.ganar();
    }
}
