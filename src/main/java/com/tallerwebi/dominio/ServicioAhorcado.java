package com.tallerwebi.dominio;

import java.util.function.BooleanSupplier;

public interface ServicioAhorcado {

	   
	String elegirPalabraAleatoria() ;
		
	Boolean intentarLetra(char letra);

	Boolean buscarLetraEnPalabra(char letra);

	Integer getPartesAhorcado();

	Object getPalabraAdivinada();

	BooleanSupplier isPerdido();

	Object getPalabras();

	Object getPalabrasAdivinadas();
	
	void agregarPalabraAdivinada(String palabra);
	
	 Object obtenerPalabrasAdivinadasSinNulos();
		
	
		
}