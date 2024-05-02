package com.tallerwebi.infraestructura;

import java.util.HashSet;
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

	public ServicioBingoImpl() {
		this.rand = new Random();
		this.seHizobingo = false;
	}

	@Override
	public Integer entregarNumeroAleatorio() {
		Integer numeroAleatorio = rand.nextInt(100) + 1;
		return numeroAleatorio;
	}

	@Override
	public void marcarCasillero(Integer numeroCantado) {
// completo una celda de mi casillero cuando el numero que me entregan esta tambien en mi celda
		// este metodo depende si o si de la url q llegue del cliente asi q todavia no puedo hacerlo
		CartonBingo cartonActual = this.getCartonNuevo();
		for (Integer[] fila : cartonActual.getNumeros()) {
			for (int i = 0; i < fila.length; i++) {
				if (fila[i].equals(numeroCantado)) {
				// entonces puedo marcar el casillero
				// deberia ser null u otra cosa?
				fila[i]=null;
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
		
		 return this.cartonNuevo = new CartonBingo(carton);
		
	}

	public CartonBingo getCartonNuevo() {
		return cartonNuevo;
	}
	
}

