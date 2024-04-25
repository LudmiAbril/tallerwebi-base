package com.tallerwebi.dominio;

public interface ServicioBingo {
    static final Integer CANTIDAD_DE_FILAS = 5;
    static final Integer CANTIDAD_DE_COLUMNAS = 5;
    static final Integer MAX_NUMERO = 99;

    Integer entregarNumeroAleatorio();

    void completarCasillero();

    Boolean bingo();

    CartonBingo generarCarton();

}
