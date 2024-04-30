// package com.tallerwebi.dominio;

// import static org.junit.jupiter.api.Assertions.*;


// import org.junit.jupiter.api.Test;

// import com.tallerwebi.infraestructura.ServicioAhorcadoImpel;

// /*DUDAAS
//  * Las palabras deberia ser un array o lista
//  * 
//  * */

// /*IMPLEMENTAR
//  * palabras BDD .  
//  * pongo una falsa palaba hasta que hagamos un repositorio
//  * que las palabras adivinadas se guarden en un array y salgan del array de palabras para adivinar del el usuario
//  * Se pueden elegir en vez de palabras frases.
//  * 
//  * */

// class AhorcadoTest {
	
// 	 @Test
// 	    public void queSePuedaIngresarUnaLetraCorrecta() {
// 	        String[] palabras = {"java"};
// 	        ServicioAhorcado juego = new ServicioAhorcadoImpel(palabras);

// 	        assertTrue(juego.intentarLetra('a'));
// 	        assertEquals("_a_a", juego.getPalabraAdivinada().toString());
// 	    }

// 	 @Test
// 	    public void queSeReduscanLosIntentosSiIngresamosUnaLetraIncorrecta() {
// 	        String[] palabras = {"java"};
// 	        ServicioAhorcado juego = new ServicioAhorcadoImpel(palabras);

// 	        assertFalse(juego.intentarLetra('x'));
// 	        assertEquals(1, juego.getPartesAhorcado()); 
// 	    }

// 	 @Test
// 	    public void queSeAcerteLaPalabra() {
// 	        String[] palabras = {"java"};
// 	        ServicioAhorcado juego = new ServicioAhorcadoImpel(palabras);

// 	        assertTrue(juego.intentarLetra('a'));
// 	        assertTrue(juego.intentarLetra('j'));
// 	        assertTrue(juego.intentarLetra('v'));
// 	        assertEquals("java", juego.getPalabraAdivinada().toString());
// 	    }
// 	@Test
//     public void quesePonganEnLaPosicionCorrectaLasLetrasAlAdivinar() {
// 		 String[] palabras = {"java"};
// 	        ServicioAhorcado juego = new ServicioAhorcadoImpel(palabras);

// 	        assertTrue(juego.intentarLetra('a'));
// 	        assertEquals("_a_a", juego.getPalabraAdivinada().toString());
// 	        assertTrue(juego.intentarLetra('j'));
// 	        assertEquals("ja_a", juego.getPalabraAdivinada().toString());

// 	        assertTrue(juego.intentarLetra('v'));
// 	        assertEquals("java", juego.getPalabraAdivinada().toString());
//     }
// 	@Test
//     public void perderJuegoAhorcado() {
//         String[] palabras = {"java"};
//         ServicioAhorcado juego = new ServicioAhorcadoImpel(palabras);

//         assertFalse(juego.intentarLetra('x'));
//         assertFalse(juego.intentarLetra('y'));
//         assertTrue(juego.intentarLetra('j'));
//         assertFalse(juego.intentarLetra('w'));
//         assertFalse(juego.intentarLetra('q'));
//         assertFalse(juego.intentarLetra('h'));
//         assertFalse(juego.intentarLetra('p'));

//         assertTrue(juego.isPerdido());
//     }
// //	
// //	 @Test
// //	    public void queSeActualiceLaPalabrasDisponiblesDespuesDeAdivinar() {
// //	        String[] palabras = {"java"};
// //	        ServicioAhorcado juego = new ServicioAhorcadoImpel(palabras);
// //
// //	        assertTrue(juego.intentarLetra('a'));
// //	        assertTrue(juego.intentarLetra('j'));
// //	        assertTrue(juego.intentarLetra('v'));
// //	        
// //	        assertFalse(Arrays.asList(juego.getPalabras()).contains("java"));
// //	        assertTrue(Arrays.asList(juego.getPalabrasAdivinadas()).contains("java"));
// //
// //	    }
// }
