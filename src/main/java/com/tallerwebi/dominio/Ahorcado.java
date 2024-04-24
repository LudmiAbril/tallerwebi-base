package com.tallerwebi.dominio;

import java.util.Random;

public class Ahorcado {

	    private String[] palabras;
	    private String palabraSecreta;
	    private StringBuilder palabraAdivinada;
	    private int partesAhorcado;
	    private boolean perdido;

	    public Ahorcado(String[] palabras) {
	        this.palabras = palabras;
	        this.palabraSecreta = elegirPalabraAleatoria();
	        this.palabraAdivinada = new StringBuilder("_".repeat(palabraSecreta.length()));
	        this.partesAhorcado = 0;
	        this.perdido = false;
	    }

		private String elegirPalabraAleatoria() {
			 Random rand = new Random();
		        int index = rand.nextInt(palabras.length);
		        return palabras[index];
		}
		
		
		public boolean intentarLetra(char letra) {
		    boolean acierto = buscarLetraEnPalabra(letra);

		    if (!acierto) {
		        this.partesAhorcado++;
		        if (partesAhorcado== 6) {
		           this.perdido = true;
		        }
		    }

		    return acierto;
		}

		private boolean buscarLetraEnPalabra(char letra) {
		    boolean acierto = false;

		    // Si se encuentra una coincidencia entre la letra propuesta y 
		    // una letra de la palabra secreta, se actualiza la cadena palabraAdivinada
		    for (int i = 0; i < palabraSecreta.length(); i++) {
		        if (palabraSecreta.charAt(i) == letra) {
		            palabraAdivinada.setCharAt(i, letra);
		            acierto = true;
		        }
		    }

		    return acierto;
		}
		
		
		
		
		
		
		public String[] getPalabras() {
			return palabras;
		}

		public void setPalabras(String[] palabras) {
			this.palabras = palabras;
		}

		public String getPalabraSecreta() {
			return palabraSecreta;
		}

		public void setPalabraSecreta(String palabraSecreta) {
			this.palabraSecreta = palabraSecreta;
		}

		public StringBuilder getPalabraAdivinada() {
			return palabraAdivinada;
		}

		public void setPalabraAdivinada(StringBuilder palabraAdivinada) {
			this.palabraAdivinada = palabraAdivinada;
		}

		public int getPartesAhorcado() {
			return partesAhorcado;
		}

		public void setPartesAhorcado(int partesAhorcado) {
			this.partesAhorcado = partesAhorcado;
		}

		public boolean isPerdido() {
			return perdido;
		}

		public void setPerdido(boolean perdido) {
			this.perdido = perdido;
		}

		 
		 
}
