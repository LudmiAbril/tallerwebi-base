package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tallerwebi.infraestructura.ServicioAhorcadoImpel;

public class AhorcadoTest {

    private ServicioAhorcadoImpel servicio;
    private RepositorioPalabra repositorioPalabra;

    @BeforeEach
    public void init() {
        repositorioPalabra = mock(RepositorioPalabra.class);
        servicio = new ServicioAhorcadoImpel(repositorioPalabra);
    }

    @Test
    public void queSePuedaIngresarUnaLetraCorrecta() {
      
        Integer partesAhorcado = 6;
        String palabraParaAdivinar = "perro";
        Character letra = 'e';
        partesAhorcado = servicio.intentarLetra(letra, palabraParaAdivinar, partesAhorcado);
        assertThat(partesAhorcado, equalTo(6));
    }

    @Test
    public void queSeReduzcanLosIntentosSiIngresamosUnaLetraIncorrecta() {
        Integer partesAhorcado = 6;
        String palabraParaAdivinar = "perro";
        Character letra = 'x';
        partesAhorcado = servicio.intentarLetra(letra, palabraParaAdivinar, partesAhorcado);

        assertThat(partesAhorcado, equalTo(5));
    }

    @Test
    public void queSeAcerteLaPalabra() {
   
        Integer partesAhorcado = 6;
        String palabraParaAdivinar = "perro";
        Character letraCorrecta = 'p';
        Character letraIncorrecta = 'x';
        partesAhorcado = servicio.intentarLetra(letraCorrecta, palabraParaAdivinar, partesAhorcado);
        partesAhorcado = servicio.intentarLetra(letraIncorrecta, palabraParaAdivinar, partesAhorcado);

        String palabraOculta = mostrarPalabraOculta(palabraParaAdivinar, "p");
        assertThat(palabraOculta, equalTo("p____"));
        assertFalse(servicio.Perdio(partesAhorcado));
    }

    @Test
    public void queSePierdaElJuego() {
 
        Integer partesAhorcado = 1;
        String palabraParaAdivinar = "perro";
        Character letraIncorrecta = 'x';
        partesAhorcado = servicio.intentarLetra(letraIncorrecta, palabraParaAdivinar, partesAhorcado);
        assertTrue(servicio.Perdio(partesAhorcado));
    }

    private String mostrarPalabraOculta(String palabra, String letrasIntentadas) {
        StringBuilder palabraOculta = new StringBuilder(palabra.length());

        for (int i = 0; i < palabra.length(); i++) {
            char letra = palabra.charAt(i);

            if (letra == ' ') {
                palabraOculta.append(' '); 
            } else if (letrasIntentadas.indexOf(letra) >= 0) {
                palabraOculta.append(letra); 
            } else {
                palabraOculta.append('_'); 
            }
        }

        return palabraOculta.toString();
    }
}
