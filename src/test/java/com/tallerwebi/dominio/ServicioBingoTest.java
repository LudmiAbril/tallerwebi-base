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

	// @Test
	// public void queSePuedaEntregarUnNumeroAleatorioDel1Al99YQueNoSeRepitan() {
	// 	Integer numeroAleatorio = servicioBingo.entregarNumeroAleatorio();
	// 	Set<Integer> numerosEntregados = ((ServicioBingoImpl) servicioBingo).getNumerosEntregados();
	// 	assertThat(numeroAleatorio, is(notNullValue()));
	// 	assertThat(numeroAleatorio, allOf(greaterThanOrEqualTo(1), lessThan(100)));
	// 	assertThat(numerosEntregados, containsInAnyOrder(numeroAleatorio));
	// }

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

	// @Test
	// public void queElCasilleroMarcadoSeaIgualAlNumeroEntregado() {
	// CartonBingo carton = this.servicioBingo.generarCarton();
	// Integer[][] numeros = carton.getNumeros();

	// Integer numeroAleatorio;
	// do {
	// numeroAleatorio = this.servicioBingo.entregarNumeroAleatorio();
	// } while (esNumeroEnElCarton(numeroAleatorio, numeros));

	// this.servicioBingo.marcarCasillero(numeroAleatorio, carton);

	// Set<Integer> numerosEntregados = ((ServicioBingoImpl)
	// this.servicioBingo).getNumerosEntregados();
	// Set<Integer> numerosMarcadosEnElCarton = ((ServicioBingoImpl)
	// this.servicioBingo)
	// .getNumerosMarcadosEnElCarton();

	// assertThat(numerosEntregados, contains(numeroAleatorio));
	// assertThat(numerosMarcadosEnElCarton, contains(numeroAleatorio));
	// }

	// private boolean esNumeroEnElCarton(Integer numero, Integer[][] carton) {
	// for (int i = 0; i < carton.length; i++) {
	// for (int j = 0; j < carton[i].length; j++) {
	// if (carton[i][j].equals(numero)) {
	// return true;
	// }
	// }
	// }
	// return false;
	// }
}
