package com.tallerwebi.dominio.excepcion;

public class CasilleroInexistenteException extends Throwable {
    public CasilleroInexistenteException(String mensajeError) {
        super( mensajeError);
    }
}
