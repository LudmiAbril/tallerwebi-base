package com.tallerwebi.dominio;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

import com.tallerwebi.infraestructura.ServicioBingoImpl;

public class ServicioBingoTest {

	private ServicioBingo servicioBingo;

	@BeforeEach
	public void init() {
		this.servicioBingo = new ServicioBingoImpl();
	}

	@Test
	public void queSePuedaEntregarUnNumeroAleatorioDel1Al99() {
		Integer numeroAleatorio = servicioBingo.entregarNumeroAleatorio();
		assertThat(numeroAleatorio, is(notNullValue()));
		assertThat(numeroAleatorio, allOf(greaterThanOrEqualTo(1), lessThan(100)));
	}

	// USAR HAS SIZE
	@Test
	public void queSePuedaGenerarUnCartonConNumerosAleatoriosQueNoSeRepitan() {
		Jugador jugador = new Jugador();
		Jugador jugador2 = new Jugador();
		Partida multijugador = new PartidaMultijugador(Juego.BINGO, jugador, jugador2);
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
