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
		// almacenar los números entregados
		Set<Integer> numerosEntregados = new HashSet<Integer>();
		Integer numeroAleatorio = servicioBingo.entregarNumeroAleatorio(numerosEntregados);
		numerosEntregados.add(numeroAleatorio);
		// que el número entregado no sea nulo
		assertThat(numeroAleatorio, is(notNullValue()));
		// esté dentro del rango especificado
		assertThat(numeroAleatorio, allOf(greaterThanOrEqualTo(1), lessThan(100)));
		// esté contenido en el conjunto numerosEntregados.
		assertThat(numerosEntregados, containsInAnyOrder(numeroAleatorio));
	}

	@Test
	public void queSePuedaGenerarUnCartonDe25NumerosAleatorios() {
		CartonBingo carton = servicioBingo.generarCarton(5);
		Integer[][] numeros = carton.getNumeros();
		// para almacenar los números que se encuentran en el cartón.
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
				{ 1, 2, 3, 4, 5 },
				{ 6, 7, 8, 9, 10 },
				{ 11, 12, 13, 14, 15 },
				{ 16, 17, 18, 19, 20 },
				{ 21, 22, 23, 24, 25 }
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

	@Test
	public void queSePuedaHacerLineaVertical() {
		// genero un carton
		Integer[][] numeros = {
				{ 1, 2, 3, 4, 5 },
				{ 6, 7, 8, 9, 10 },
				{ 11, 12, 13, 14, 15 },
				{ 16, 17, 18, 19, 20 },
				{ 21, 22, 23, 24, 25 }
		};
		CartonBingo carton = new CartonBingo(numeros);
		((ServicioBingoImpl) this.servicioBingo).setDimension(5);
		((ServicioBingoImpl) this.servicioBingo).setCartonNuevo(carton);

		Integer numeroAleatorio1 = 1;
		Integer numeroAleatorio6 = 6;
		Integer numeroAleatorio11 = 11;
		Integer numeroAleatorio16 = 16;
		Integer numeroAleatorio21 = 21;

		// te entrego numeros aleatorio que en el carton hagan linea
		Set<Integer> numerosEntregados = new HashSet<Integer>();
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio1);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio6);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio11);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio16);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio21);

		// marcarEsosNumerosEnElCarton
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio1, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio6, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio11, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio16, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio21, carton);

		Boolean linea = ((ServicioBingo) this.servicioBingo)
				.linea(((ServicioBingoImpl) this.servicioBingo).getNumerosMarcadosEnElCarton(), carton);
		((ServicioBingoImpl) this.servicioBingo).setSeHizoLinea(linea);
		assertThat(linea, is(true));
		assertThat(((ServicioBingoImpl) this.servicioBingo).getSeHizoLinea(), is(true));

	}

	@Test
	public void queSePuedaHacerLineaHorizontal() {
		Integer[][] numeros = {
				{ 1, 2, 3, 4, 5 },
				{ 6, 7, 8, 9, 10 },
				{ 11, 12, 13, 14, 15 },
				{ 16, 17, 18, 19, 20 },
				{ 21, 22, 23, 24, 25 }
		};
		CartonBingo carton = new CartonBingo(numeros);
		((ServicioBingoImpl) this.servicioBingo).setDimension(5);
		((ServicioBingoImpl) this.servicioBingo).setCartonNuevo(carton);
		Integer numeroAleatorio1 = 1;
		Integer numeroAleatorio2 = 2;
		Integer numeroAleatorio3 = 3;
		Integer numeroAleatorio4 = 4;
		Integer numeroAleatorio5 = 5;

		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio1);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio2);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio3);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio4);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio5);
		// marcarEsosNumerosEnElCarton
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio1, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio2, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio3, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio4, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio5, carton);

		boolean linea = ((ServicioBingo) this.servicioBingo)
				.linea(((ServicioBingoImpl) this.servicioBingo).getNumerosMarcadosEnElCarton(), carton);
		((ServicioBingoImpl) this.servicioBingo).setSeHizoLinea(linea);
		assertThat(linea, is(true));
		assertThat(((ServicioBingoImpl) this.servicioBingo).getSeHizoLinea(), is(true));
	}

	@Test
	public void queSePuedaHacerLineaDiagonalPrimaria() {
		Integer[][] numeros = {
				{ 1, 2, 3, 4, 5 },
				{ 6, 7, 8, 9, 10 },
				{ 11, 12, 13, 14, 15 },
				{ 16, 17, 18, 19, 20 },
				{ 21, 22, 23, 24, 25 }
		};
		CartonBingo carton = new CartonBingo(numeros);
		((ServicioBingoImpl) this.servicioBingo).setDimension(5);
		((ServicioBingoImpl) this.servicioBingo).setCartonNuevo(carton);

		Integer numeroAleatorio1 = 1;
		Integer numeroAleatorio7 = 7;
		Integer numeroAleatorio13 = 13;
		Integer numeroAleatorio19 = 19;
		Integer numeroAleatorio25 = 25;

		// te entrego numeros aleatorio que en el carton hagan linea
		Set<Integer> numerosEntregados = new HashSet<Integer>();
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio1);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio7);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio13);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio19);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio25);

		// marcarEsosNumerosEnElCarton
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio1, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio7, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio13, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio19, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio25, carton);

		// // como los numeros que te entregue forman una linea en tu carton podes hacer
		// // linea
		boolean linea = ((ServicioBingo) this.servicioBingo)
				.linea(((ServicioBingoImpl) this.servicioBingo).getNumerosMarcadosEnElCarton(), carton);
		((ServicioBingoImpl) this.servicioBingo).setSeHizoLinea(linea);
		assertThat(linea, is(true));
		assertThat(((ServicioBingoImpl) this.servicioBingo).getSeHizoLinea(), is(true));
	}

	@Test
	public void queSePuedaHacerLineaDiagonalSecundaria() {
		Integer[][] numeros = {
				{ 1, 2, 3, 4, 5 },
				{ 6, 7, 8, 9, 10 },
				{ 11, 12, 13, 14, 15 },
				{ 16, 17, 18, 19, 20 },
				{ 21, 22, 23, 24, 25 }
		};
		CartonBingo carton = new CartonBingo(numeros);
		((ServicioBingoImpl) this.servicioBingo).setDimension(5);
		((ServicioBingoImpl) this.servicioBingo).setCartonNuevo(carton);

		Integer numeroAleatorio5 = 5;
		Integer numeroAleatorio9 = 9;
		Integer numeroAleatorio13 = 13;
		Integer numeroAleatorio17 = 17;
		Integer numeroAleatorio21 = 21;

		// // te entrego numeros aleatorio que en el carton hagan linea
		Set<Integer> numerosEntregados = new HashSet<Integer>();
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio5);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio9);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio13);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio17);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio21);

		// marcarEsosNumerosEnElCarton
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio5, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio9, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio13, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio17, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio21, carton);

		// como los numeros que te entregue forman una linea en tu carton podes hacer
		// linea
		Boolean linea = ((ServicioBingo) this.servicioBingo)
				.linea(((ServicioBingoImpl) this.servicioBingo).getNumerosMarcadosEnElCarton(), carton);
		((ServicioBingoImpl) this.servicioBingo).setSeHizoLinea(linea);
		assertThat(linea, is(true));
		assertThat(((ServicioBingoImpl) this.servicioBingo).getSeHizoLinea(), is(true));
	}

	@Test
	public void queSePuedaObtenerLaCantidadDeNumerosRestantesParaCompletarLaTirada() {
		Integer tirada = 50;
		Integer numeroAleatorio = 10;
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio);
		Integer cantidadDeNumerosEntregados = ((ServicioBingoImpl) this.servicioBingo)
				.obtenerCantidadDeNumerosEntregados();
		Integer numerosRestantes = this.servicioBingo.obtenerCantidadDeNumerosRestantesParaCompletarLaTirada(
				this.servicioBingo.obtenerTirada(tirada), cantidadDeNumerosEntregados);
		Integer numerosRestantesEsperados = 49;
		assertThat(numerosRestantes, equalTo(numerosRestantesEsperados));
	}

	@Test
	public void queNoSePuedaHacerLineaSiNoSeMarcoUnaLinea() {
		// genero un carton
		Integer[][] numeros = {
				{ 1, 2, 3, 4, 5 },
				{ 6, 7, 8, 9, 10 },
				{ 11, 12, 13, 14, 15 },
				{ 16, 17, 18, 19, 20 },
				{ 21, 22, 23, 24, 25 }
		};
		CartonBingo carton = new CartonBingo(numeros);

		((ServicioBingoImpl) this.servicioBingo).setDimension(5);
		((ServicioBingoImpl) this.servicioBingo).setCartonNuevo(carton);

		Integer numeroAleatorio1 = 1;
		Integer numeroAleatorio6 = 6;
		Integer numeroAleatorio11 = 11;
		Integer numeroAleatorio16 = 16;
		Integer numeroAleatorio21 = 21;

		// te entrego numeros aleatorio que en el carton hagan linea
		Set<Integer> numerosEntregados = new HashSet<Integer>();
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio1);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio6);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio11);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio16);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio21);

		Set<Integer> numerosMarcadosEnElCarton = new HashSet<Integer>();
		numerosMarcadosEnElCarton.add(numeroAleatorio16);
		numerosMarcadosEnElCarton.add(numeroAleatorio11);

		// Verifico que no se haga línea
		Boolean seHizoLinea = servicioBingo.linea(numerosMarcadosEnElCarton, carton);
		assertThat(seHizoLinea, is(false));
	}

	@Test
	public void queSeVacienLosNumerosEntregadosYMarcadosDespuesDeHacerLinea() {
		// genero un carton
		Integer[][] numeros = {
				{ 1, 2, 3, 4, 5 },
				{ 6, 7, 8, 9, 10 },
				{ 11, 12, 13, 14, 15 },
				{ 16, 17, 18, 19, 20 },
				{ 21, 22, 23, 24, 25 }
		};
		CartonBingo carton = new CartonBingo(numeros);
		((ServicioBingoImpl) this.servicioBingo).setDimension(5);
		((ServicioBingoImpl) this.servicioBingo).setCartonNuevo(carton);

		Integer numeroAleatorio1 = 1;
		Integer numeroAleatorio6 = 6;
		Integer numeroAleatorio11 = 11;
		Integer numeroAleatorio16 = 16;
		Integer numeroAleatorio21 = 21;

		// te entrego numeros aleatorio que en el carton hagan linea
		Set<Integer> numerosEntregados = new HashSet<Integer>();
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio1);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio6);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio11);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio16);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(numeroAleatorio21);

		// marcarEsosNumerosEnElCarton
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio1, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio6, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio11, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio16, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(numeroAleatorio21, carton);

		Boolean linea = ((ServicioBingo) this.servicioBingo)
				.linea(((ServicioBingoImpl) this.servicioBingo).getNumerosMarcadosEnElCarton(), carton);
		((ServicioBingoImpl) this.servicioBingo).setSeHizoLinea(linea);

		assertTrue(((ServicioBingoImpl) servicioBingo).getSeHizoLinea());
		assertTrue(servicioBingo.getNumerosMarcadosEnElCarton().isEmpty());
		assertTrue(servicioBingo.getNumerosEntregados().isEmpty());
	}

	@Test
	public void queSeVacienLosNumerosEntregadosYMarcadosDespuesDeHacerBingo() {
		// genero un carton
		Integer[][] numeros = {
				{ 1, 2, 3},
				{ 6, 7, 8},
				{ 11, 12, 13}
		};
		CartonBingo carton = new CartonBingo(numeros);
		((ServicioBingoImpl) this.servicioBingo).setDimension(3);
		((ServicioBingoImpl) this.servicioBingo).setCartonNuevo(carton);

		// te entrego numeros aleatorio que en el carton hagan linea
		Set<Integer> numerosEntregados = new HashSet<Integer>();
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(1);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(2);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(3);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(6);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(7);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(8);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(11);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(12);
		((ServicioBingoImpl) this.servicioBingo).getNumerosEntregados().add(13);

		// marcarEsosNumerosEnElCarton
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(1, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(2, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(3, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(6, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(7, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(8, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(11, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(12, carton);
		((ServicioBingoImpl) this.servicioBingo).marcarCasillero(13, carton);

		Boolean bingo = ((ServicioBingo) this.servicioBingo)
				.bingo(((ServicioBingoImpl) this.servicioBingo).getNumerosMarcadosEnElCarton(), 3);
		((ServicioBingoImpl) this.servicioBingo).setSeHizobingo(bingo);

		assertTrue(((ServicioBingoImpl) servicioBingo).getSeHizobingo());
		assertTrue(servicioBingo.getNumerosMarcadosEnElCarton().isEmpty());
		assertTrue(servicioBingo.getNumerosEntregados().isEmpty());
	}
	
}