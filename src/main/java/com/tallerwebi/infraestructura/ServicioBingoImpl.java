package com.tallerwebi.infraestructura;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
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
	private List<Integer> numerosMarcadosEnElCarton;

	public ServicioBingoImpl() {
		this.rand = new Random();
		this.seHizobingo = false;
		this.numerosMarcadosEnElCarton = new ArrayList<Integer>();
	}

	@Override
	public Integer entregarNumeroAleatorio() {
		Integer numeroAleatorio = rand.nextInt(100) + 1;
		return numeroAleatorio;
	}

	@Override
	public void marcarCasillero(Integer numeroCasillero, CartonBingo carton) {
		Integer[][] numeros = carton.getNumeros();
		for (int i = 0; i < numeros.length; i++) {
			for (int j = 0; j < numeros[i].length; j++) {
				if (numeros[i][j].equals(numeroCasillero)) {
					// Marcar el casillero asignándole un valor especial
					numerosMarcadosEnElCarton.add(numeros[i][j]);
					return;
				}
			}
		}
	}
	

	@Override
	public Boolean bingo() {
		return this.seHizobingo = true;
		// se va a hacer bingo cuando
	}

	@Override
	public CartonBingo generarCarton() {
		Integer[][] carton = new Integer[CANTIDAD_DE_FILAS][CANTIDAD_DE_COLUMNAS];
		Set<Integer> numerosUsados = new HashSet<Integer>();

		for (int f = 0; f < CANTIDAD_DE_FILAS; f++) {
			for (int c = 0; c < CANTIDAD_DE_COLUMNAS; c++) {
				int numero;
				do {
					numero = rand.nextInt(MAX_NUMERO) + 1;
				} while (numerosUsados.contains(numero));

				carton[f][c] = numero;
				numerosUsados.add(numero);
			}
		}

		List<Integer> numerosOrdenados = new ArrayList<Integer>();
		for (int f = 0; f < CANTIDAD_DE_FILAS; f++) {
			for (int c = 0; c < CANTIDAD_DE_COLUMNAS; c++) {
				numerosOrdenados.add(carton[c][f]);

			}
		}

		Collections.sort(numerosOrdenados);

		int index = 0;

		for (int f = 0; f < CANTIDAD_DE_FILAS; f++) {
			for (int c = 0; c < CANTIDAD_DE_COLUMNAS; c++) {
				carton[c][f] = numerosOrdenados.get(index++);
			}
		}

		return this.cartonNuevo = new CartonBingo(carton);

	}

	public CartonBingo getCartonNuevo() {
		return cartonNuevo;
	}

}
