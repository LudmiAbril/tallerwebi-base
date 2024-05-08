package com.tallerwebi.dominio;

import java.util.HashSet;
import java.util.Set;

public interface ServicioBingo {
    static final Integer CANTIDAD_DE_FILAS = 5;
    static final Integer CANTIDAD_DE_COLUMNAS = 5;
    static final Integer MAX_NUMERO = 99;

    Integer entregarNumeroAleatorio(Set<Integer> numerosEntregados);

    void marcarCasillero(Integer numeroCasillero, CartonBingo carton);

    Boolean bingo(Set<Integer> numerosMarcadosEnElCarton);

    CartonBingo generarCarton();


}
