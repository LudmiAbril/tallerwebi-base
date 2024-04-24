package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/*DUDAAS
 * Las palabras deberia ser un array o lista
 * 
 * */

/*IMPLEMENTAR
 * que las palabras adivinadas se guarden en un array y salgan del array de palabras para adivinar del el usuario
 * Se pueden elegir en vez de palabras frases.
 * 
 * */

class AhorcadoTest {
	
	 @Test
	    public void queSePuedaIngresarUnaLetraCorrecta() {
	        String[] palabras = {"java"};
	        Ahorcado juego = new Ahorcado(palabras);

	        assertTrue(juego.intentarLetra('a'));
	        assertEquals("_a_a", juego.getPalabraAdivinada().toString());
	    }

	 @Test
	    public void queSeReduscanLosIntentosSiIngresamosUnaLetraIncorrecta() {
	        String[] palabras = {"java"};
	        Ahorcado juego = new Ahorcado(palabras);

	        assertFalse(juego.intentarLetra('x'));
	        assertEquals(1, juego.getPartesAhorcado()); 
	    }

	 @Test
	    public void queSeAcerteLaPalabra() {
	        String[] palabras = {"java"};
	        Ahorcado juego = new Ahorcado(palabras);

	        assertTrue(juego.intentarLetra('a'));
	        assertTrue(juego.intentarLetra('j'));
	        assertTrue(juego.intentarLetra('v'));
	        assertEquals("java", juego.getPalabraAdivinada().toString());
	    }
	@Test
    public void quesePonganEnLaPosicionCorrectaLasLetrasAlAdivinar() {
		 String[] palabras = {"java"};
	        Ahorcado juego = new Ahorcado(palabras);

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
        Ahorcado juego = new Ahorcado(palabras);

        assertFalse(juego.intentarLetra('x'));
        assertFalse(juego.intentarLetra('y'));
        assertTrue(juego.intentarLetra('j'));
        assertFalse(juego.intentarLetra('w'));
        assertFalse(juego.intentarLetra('q'));
        assertFalse(juego.intentarLetra('h'));
        assertFalse(juego.intentarLetra('p'));

        assertTrue(juego.isPerdido());
    }
}
