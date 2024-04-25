package com.tallerwebi.dominio.excepcion;

public class CasilleroVacio extends Exception {
    public CasilleroVacio(String noHayFichaParaMover) {
        super(noHayFichaParaMover);
    }
}
