package com.tallerwebi.dominio;

import java.util.List;
import java.util.function.BooleanSupplier;

public interface ServicioAhorcado {

    Integer intentarLetra(Character letra, String palabra, Integer partesAhorcado);

    Boolean buscarLetraEnPalabra(Character letra, String palabra);

    Boolean Perdio(Integer partesAhorcado);

    String entregarPalabra();

    Integer intentarPalabra(String intento, String palabra);

}