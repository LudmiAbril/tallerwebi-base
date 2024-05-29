package com.tallerwebi.infraestructura;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.CartonBingo;
import com.tallerwebi.dominio.ServicioBingo;

@Service("servicioBingo")
public class ServicioBingoImpl implements ServicioBingo {

	private Random rand;
	private Boolean seHizobingo;
	private CartonBingo cartonNuevo;
	private Set<Integer> numerosMarcadosEnElCarton;
	private Set<Integer> numerosEntregados;
	private Boolean seHizoLinea;
	private Integer dimension;

	public ServicioBingoImpl() {
		this.rand = new Random();
		this.seHizobingo = false;
		this.numerosMarcadosEnElCarton = new HashSet<Integer>();
		this.numerosEntregados = new LinkedHashSet<Integer>();
	}

	@Override
	public Integer entregarNumeroAleatorio(Set<Integer> numerosEntregados) {
		Integer numeroAleatorio = 0;
		if (numerosEntregados == null) {
			numeroAleatorio = rand.nextInt(99) + 1;
			numerosEntregados.add(numeroAleatorio);
			return numeroAleatorio;

		}

		do {
			numeroAleatorio = rand.nextInt(99) + 1;
		} while (numerosEntregados.contains(numeroAleatorio));
		numerosEntregados.add(numeroAleatorio);
		return numeroAleatorio;

	}

	@Override
	public void marcarCasillero(Integer numeroCasillero, CartonBingo carton) {
		Integer[][] numeros = carton.getNumeros();
		for (int i = 0; i < numeros.length; i++) {
			for (int j = 0; j < numeros[i].length; j++) {
				if ((numeros[i][j].equals(numeroCasillero)) &&
						(!numerosMarcadosEnElCarton.contains(numeroCasillero)) &&
						(!this.getSeHizobingo() || numerosEntregados.contains(numeroCasillero))) {
					numerosMarcadosEnElCarton.add(numeroCasillero);
					return;
				}
			}
		}
	}

	@Override
	public Boolean bingo(Set<Integer> numerosMarcadosEnElCarton, Integer dimension) {
		if (numerosMarcadosEnElCarton.size() == dimension*dimension) {
			this.setSeHizobingo(true);
			this.numerosMarcadosEnElCarton.clear();
			this.numerosEntregados.clear();
			return this.getSeHizobingo();
		} else {
			this.setSeHizobingo(false);
			return this.getSeHizobingo();
		}

	}

	@Override
	public CartonBingo generarCarton(Integer dimension) {
		this.setDimension(dimension);
		Integer[][] carton = new Integer[dimension][dimension];
		Set<Integer> numerosUsados = new HashSet<Integer>();

		for (int f = 0; f < dimension; f++) {
			for (int c = 0; c < dimension; c++) {
				int numero;
				do {
					numero = rand.nextInt(MAX_NUMERO) + 1;
				} while (numerosUsados.contains(numero));

				carton[f][c] = numero;
				numerosUsados.add(numero);
			}
		}

		List<Integer> numerosOrdenados = new ArrayList<Integer>();
		for (int f = 0; f < dimension; f++) {
			for (int c = 0; c < dimension; c++) {
				numerosOrdenados.add(carton[c][f]);

			}
		}

		Collections.sort(numerosOrdenados);

		int index = 0;

		for (int f = 0; f < dimension; f++) {
			for (int c = 0; c < dimension; c++) {
				carton[c][f] = numerosOrdenados.get(index++);
			}
		}

		CartonBingo cartonNuevo = new CartonBingo(carton);
		return cartonNuevo;

	}

	public Integer setDimension(Integer dimension) {
		return this.dimension = dimension;
	}

	public Integer getDimension() {
		return dimension;
	}

	public CartonBingo getCartonNuevo() {
		return cartonNuevo;
	}

	public void setCartonNuevo(CartonBingo cartonNuevo) {
		this.cartonNuevo = cartonNuevo;
	}

	public Set<Integer> getNumerosEntregados() {
		return numerosEntregados;
	}

	public void setNumerosEntregados(Set<Integer> numerosEntregados) {
		this.numerosEntregados = numerosEntregados;
	}

	public Boolean getSeHizobingo() {
		return seHizobingo;
	}

	public void setSeHizobingo(Boolean seHizobingo) {
		this.seHizobingo = seHizobingo;
	}

	public Boolean getSeHizoLinea() {
		return this.seHizoLinea;
	}

	public void setSeHizoLinea(Boolean seHizoLinea) {
		this.seHizoLinea = seHizoLinea;
	}

	public Set<Integer> getNumerosMarcadosEnElCarton() {
		return numerosMarcadosEnElCarton;
	}

	public void setNumerosMarcadosEnElCarton(Set<Integer> numerosMarcadosEnElCarton) {
		this.numerosMarcadosEnElCarton = numerosMarcadosEnElCarton;
	}

	@Override
	public Boolean linea(Set<Integer> numerosMarcadosEnElCarton, CartonBingo carton) {
		Integer[][] numeros = carton.getNumeros();
		List<Integer> numerosEnFila = new ArrayList<Integer>();
		boolean seHizoLinea = false;

		// HORIZONTAL
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				numerosEnFila.add(numeros[i][j]);
				if (numerosEnFila.size() == dimension) {
					if (numerosMarcadosEnElCarton.containsAll(numerosEnFila)) {
						seHizoLinea = true;
						this.numerosMarcadosEnElCarton.clear();
						this.numerosEntregados.clear();
						return seHizoLinea;
					} else {
						numerosEnFila.clear();
					}
				}

			}

		}

		// VERTICAL
		for (int j = 0; j < dimension; j++) {
			for (int i = 0; i < dimension; i++) {
				numerosEnFila.add(numeros[i][j]);
				if (numerosEnFila.size() == dimension) {
					if (numerosMarcadosEnElCarton.containsAll(numerosEnFila)) {
						seHizoLinea = true;
						this.numerosMarcadosEnElCarton.clear();
						this.numerosEntregados.clear();
						return seHizoLinea;
					} else {
						numerosEnFila.clear();
					}
				}

			}

		}

		// DIAGONAL PRINCIPAL
		for (int i = 0; i < dimension; i++) {
			numerosEnFila.add(numeros[i][i]);
		}
		if (numerosMarcadosEnElCarton.containsAll(numerosEnFila)) {
			seHizoLinea = true;
			this.numerosMarcadosEnElCarton.clear();
			this.numerosEntregados.clear();
			return seHizoLinea;
		} else {
			numerosEnFila.clear();
		}
	
		// DIAGONAL SECUNDARIA
		for (int i = 0; i < dimension; i++) {
			numerosEnFila.add(numeros[i][dimension - 1 - i]);
		}
		if (numerosMarcadosEnElCarton.containsAll(numerosEnFila)) {
			seHizoLinea = true;
			this.numerosMarcadosEnElCarton.clear();
			this.numerosEntregados.clear();
			return seHizoLinea;
		}
	
		return seHizoLinea;
	}

	public Integer obtenerCantidadDeNumerosEntregados() {
		// esta seria la tirada
		return this.numerosEntregados.size();
	}

}
