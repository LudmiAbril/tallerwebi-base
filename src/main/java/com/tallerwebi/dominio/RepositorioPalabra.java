package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioPalabra {
    Palabra obtenerUnaPalabraAleatoriaNoAdivinada();

    List<String> obtenerPalabras();
}
