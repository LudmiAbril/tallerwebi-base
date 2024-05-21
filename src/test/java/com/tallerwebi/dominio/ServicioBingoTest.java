package com.tallerwebi.dominio;

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
	public void queSePuedaEntregarUnNumeroAleatorioDel1Al99YQueNoSeRepitan() {
		Set<Integer> numerosEntregados = new HashSet<Integer>();
		Integer numeroAleatorio = servicioBingo.entregarNumeroAleatorio(numerosEntregados);
		numerosEntregados.add(numeroAleatorio);
		assertThat(numeroAleatorio, is(notNullValue()));
		assertThat(numeroAleatorio, allOf(greaterThanOrEqualTo(1), lessThan(100)));
		assertThat(numerosEntregados, containsInAnyOrder(numeroAleatorio));
	}

	@Test
	public void queSePuedaGenerarUnCartonDe25NumerosAleatorios() {
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

		assertThat(numerosUsados, hasSize(CANTIDAD_DE_NUMEROS_ESPERADA));

	}

	@Test
	public void queElCasilleroMarcadoSeaIgualAlNumeroEntregado() {
		// Generar un cartón nuevo
		Integer[][] numeros = {
			{1, 2, 3, 4, 5},
			{6, 7, 8, 9, 10},
			{11, 12, 13, 14, 15},
			{16, 17, 18, 19, 20},
			{21, 22, 23, 24, 25}
		};
	
		CartonBingo carton = new CartonBingo(numeros);
		Set<Integer> numerosEntregados = new HashSet<Integer>();
		Integer numeroAleatorio = 10;
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio);
		this.servicioBingo.marcarCasillero(numeroAleatorio, carton);
		numerosEntregados = ((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados();
		Set<Integer> numerosMarcadosEnElCarton = ((ServicioBingoImpl) this.servicioBingo)
				.getNumerosMarcadosEnElCarton();

		assertTrue(numerosEntregados.contains(numeroAleatorio));
		assertThat(numerosEntregados, contains(numeroAleatorio));
		assertThat(numerosMarcadosEnElCarton, contains(numeroAleatorio));
	}

	// que los numeros entregados se guarden correctamente
	// gue los num marcados se guarda
	// que no puedas ganar si no marcaste todos los numeros entregados
}