package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.CasilleroInexistenteException;
import com.tallerwebi.dominio.excepcion.CasilleroVacio;

public interface ServicioSenku {

    Casillero seleccionarCasillero(Tablero tablero,Integer x,Integer y) throws CasilleroVacio, CasilleroInexistenteException;

}
