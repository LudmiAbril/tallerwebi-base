package com.tallerwebi.dominio;

import java.util.HashSet;
import java.util.Set;

public interface ServicioBingo {
    static final Integer MAX_NUMERO = 99;

    Integer entregarNumeroAleatorio(Set<Integer> numerosEntregados);

    void marcarCasillero(Integer numeroCasillero, CartonBingo carton);

    Boolean bingo(Set<Integer> numerosMarcadosEnElCarton, Integer dimension);

    CartonBingo generarCarton(Integer dimensionDelCartonDeLaSesion);

    Boolean linea(Set<Integer> numerosMarcadosEnElCarton, CartonBingo cartonDeLaSesion);

    Set<Integer> getNumerosMarcadosEnElCarton();

    Integer getDimension();

    Boolean getSeHizobingo();

}
