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
		//almacenar los números entregados
		Set<Integer> numerosEntregados = new HashSet<Integer>();
		Integer numeroAleatorio = servicioBingo.entregarNumeroAleatorio(numerosEntregados);
		numerosEntregados.add(numeroAleatorio);
		//que el número entregado no sea nulo
		assertThat(numeroAleatorio, is(notNullValue()));
		// esté dentro del rango especificado
		assertThat(numeroAleatorio, allOf(greaterThanOrEqualTo(1), lessThan(100)));
		// esté contenido en el conjunto numerosEntregados.
		assertThat(numerosEntregados, containsInAnyOrder(numeroAleatorio));
	}

	@Test
	public void queSePuedaGenerarUnCartonDe25NumerosAleatorios() {
		CartonBingo carton = servicioBingo.generarCarton();
		Integer[][] numeros = carton.getNumeros();
		//para almacenar los números que se encuentran en el cartón.
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
	@Test
	public void queLosNumerosEntregadosSeGuardenCorrectamente(){

	}
	// gue los num marcados se guarda
	@Test
	public void queLosNumerosMarcadosSeGuarden(){}
	// que no puedas ganar si no marcaste todos los numeros entregados
	@Test
	public void queNoPuedasGanarSiNoMarcasteTodosLosNumerosEntregados(){}
	@Test
	public void queSoloUnoPuedaGanar(){}
	@Test
	public void queSoloMarque99Numeros(){}
	@Test
	public void queMuestreLosUltimos5NumerosEntregados(){}
	@Test
	public void queSePuedaHacerLinea(){}
}