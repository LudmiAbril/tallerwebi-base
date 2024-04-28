package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.tallerwebi.infraestructura.ServicioBingoImpl;

public class BingoTest {

	@Test
	public void queSePuedaHacerBingo() {
		// necesito un carton
		// necesito que me entreguen un numero
		// busco que ese numero q me entregaron este en mi carton
		// si esta, lo marco.
		// marcar una linea completa horizontal o vertical del carton significa que hay
		// bingo
	}

	@Test
	public void queSePuedaEntregarUnNumeroAleatorioDel1Al100() {
		Jugador jugador = new Jugador();
		Jugador jugador2 = new Jugador();
		jugador.setNombre("mica");
		jugador2.setNombre("cele");
		Partida multijugador = new PartidaMultijugador(Juego.BINGO, jugador, jugador2);
		ServicioBingoImpl servicioBingo = new ServicioBingoImpl();
		Integer numeroAleatorio = servicioBingo.entregarNumeroAleatorio();
		assertNotNull(numeroAleatorio);
		assertTrue(numeroAleatorio >= 1 && numeroAleatorio < 100);
	}

	@Test
	public void queSePuedaGenerarUnCartonConNumerosAleatoriosQueNoSeRepitan() {
		Jugador jugador = new Jugador();
		Jugador jugador2 = new Jugador();
		jugador.setNombre("mica");
		jugador2.setNombre("cele");
		Partida multijugador = new PartidaMultijugador(Juego.BINGO, jugador, jugador2);
		ServicioBingoImpl servicioBingo = new ServicioBingoImpl();
		CartonBingo carton = servicioBingo.generarCarton();
		Integer[][] numeros = carton.getNumeros();
		Set<Integer> numerosUsados = new HashSet<Integer>();
		int cantidadDeNumerosActual = 0;
		final int CANTIDAD_DE_NUMEROS_ESPERADA = 25;

		for (int f = 0; f < numeros.length; f++) {
			for (int c = 0; c < numeros[f].length; c++) {
				int numero = numeros[f][c];
				assertTrue(numero >= 1 && numero <= 99);
				assertFalse(numerosUsados.contains(numero));
				cantidadDeNumerosActual++;
				numerosUsados.add(numero);
			}
		}
		assertEquals(cantidadDeNumerosActual, CANTIDAD_DE_NUMEROS_ESPERADA);
		assertNotNull(carton);

	}

}
