package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Casillero;
import com.tallerwebi.dominio.ServicioSenku;
import com.tallerwebi.dominio.Tablero;
import com.tallerwebi.dominio.excepcion.CasilleroInexistenteException;
import com.tallerwebi.dominio.excepcion.CasilleroVacio;

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
}
