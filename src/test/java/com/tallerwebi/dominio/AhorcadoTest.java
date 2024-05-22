package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

import com.tallerwebi.dominio.RepositorioPalabra;
import com.tallerwebi.infraestructura.RepositorioPalabraImpl;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.tallerwebi.infraestructura.ServicioAhorcadoImpel;

class AhorcadoTest {
    private Palabra palabraMock;
    private ServicioAhorcado servicio;
    private RepositorioPalabra repositorioPalabra;
    private SessionFactory sessionFactory;

    @BeforeEach
    public void init() {
        palabraMock = mock(Palabra.class);
        //sessionFactory = (SessionFactory) sessionFactory.getCurrentSession();
        repositorioPalabra = new RepositorioPalabraImpl(sessionFactory);
        this.servicio = new ServicioAhorcadoImpel(this.repositorioPalabra);
    }


    @Test
    public void queSePuedaIngresarUnaLetraCorrecta() {

        Integer partesAhorcado=6;
        String palabraParaAdivinar= "perro";
        Character letra= 'e';

        partesAhorcado=servicio.intentarLetra(letra, palabraParaAdivinar, partesAhorcado);

       assertThat(partesAhorcado, equalTo(6) );
    }

    @Test
    public void queSeReduscanLosIntentosSiIngresamosUnaLetraIncorrecta() {
        Integer partesAhorcado=6;
        String palabraParaAdivinar= servicio.entregarPalabra();
        Character letra= 'x';

        partesAhorcado=servicio.intentarLetra(letra, palabraParaAdivinar, partesAhorcado);

        assertThat(partesAhorcado, equalTo(5) );
    }

    @Test
    public void queSeAcerteLaPalabra() {




    }
/*
    @Test
    public void quesePonganEnLaPosicionCorrectaLasLetrasAlAdivinar() {
        String[] palabras = {"java"};
        ServicioAhorcado juego = new ServicioAhorcadoImpel(palabras);

        assertTrue(juego.intentarLetra('a'));
        assertEquals("_a_a", juego.getPalabraAdivinada().toString());
        assertTrue(juego.intentarLetra('j'));
        assertEquals("ja_a", juego.getPalabraAdivinada().toString());

        assertTrue(juego.intentarLetra('v'));
        assertEquals("java", juego.getPalabraAdivinada().toString());
    }

    @Test
    public void perderJuegoAhorcado() {
        String[] palabras = {"java"};
        ServicioAhorcado juego = new ServicioAhorcadoImpel(palabras);

        assertFalse(juego.intentarLetra('x'));
        assertFalse(juego.intentarLetra('y'));
        assertTrue(juego.intentarLetra('j'));
        assertFalse(juego.intentarLetra('w'));
        assertFalse(juego.intentarLetra('q'));
        assertFalse(juego.intentarLetra('h'));
        assertFalse(juego.intentarLetra('p'));

        assertTrue(juego.isPerdido());
    }
    @Test
 public void queSeActualiceLaPalabrasDisponiblesDespuesDeAdivinar() {
       String[] palabras = {"java"};
       ServicioAhorcado juego = new ServicioAhorcadoImpel(palabras);

       assertTrue(juego.intentarLetra('a'));
       assertTrue(juego.intentarLetra('j'));
       assertTrue(juego.intentarLetra('v'));

       assertFalse(Arrays.asList(juego.getPalabras()).contains("java"));
       assertTrue(Arrays.asList(juego.getPalabrasAdivinadas()).contains("java"));

    }*/
}
