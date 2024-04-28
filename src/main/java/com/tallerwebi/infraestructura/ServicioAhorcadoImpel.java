// package com.tallerwebi.infraestructura;

// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Random;
// import java.util.function.BooleanSupplier;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.tallerwebi.dominio.ServicioAhorcado;

// @Service("ServicioAhorcado")
// public class ServicioAhorcadoImpel implements ServicioAhorcado{
	
// 	 private String[] palabras;
// 	    private String palabraSecreta;
// 	    private StringBuilder palabraAdivinada;
// 	    private String[] palabrasAdivinadas; 
// 	    private Integer partesAhorcado;
// 	    private Boolean perdido;
	    
// 	    @Autowired
// 	    public ServicioAhorcadoImpel(String[] palabras) {
// 	        this.palabras = palabras;
// 	        this.palabraSecreta = elegirPalabraAleatoria();
// 	        this.palabraAdivinada = new StringBuilder("_".repeat(palabraSecreta.length()));
// 	        this.palabrasAdivinadas = new String[palabras.length];
// 	        this.partesAhorcado = 0;
// 	        this.perdido = false;
// 	    }
    

// 	@Override
// 	public String elegirPalabraAleatoria() {
// 		 Random rand = new Random();
// 	        int index = rand.nextInt(palabras.length);
// 	        return palabras[index];
// 	}


// 	@Override
// 	public Boolean intentarLetra(char letra) {
// 		 boolean acierto = buscarLetraEnPalabra(letra);
// 		    if (!acierto) {
// 		        this.partesAhorcado++;
// 		        if (partesAhorcado== 6) {
// 		           this.perdido = true;
// 		        }
// 		    }
// 		    return acierto;
// 		    }
	

// 	 @Override
// 	    public Boolean buscarLetraEnPalabra(char letra) {
// 	        boolean acierto = false;
// 	        // Si se encuentra una coincidencia entre la letra propuesta y 
// 	        // una letra de la palabra secreta, se actualiza la cadena palabraAdivinada
// 	        for (int i = 0; i < palabraSecreta.length(); i++) {
// 	            if (palabraSecreta.charAt(i) == letra) {
// 	                palabraAdivinada.setCharAt(i, letra);
// 	                acierto = true;
// 	            }
// 	        }
	       
	    
// 	    return acierto;
// 	}


// 	public String[] getPalabras() {
// 		return palabras;
// 	}


// 	public void setPalabras(String[] palabras) {
// 		this.palabras = palabras;
// 	}


// 	public String getPalabraSecreta() {
// 		return palabraSecreta;
// 	}


// 	public void setPalabraSecreta(String palabraSecreta) {
// 		this.palabraSecreta = palabraSecreta;
// 	}


// 	public StringBuilder getPalabraAdivinada() {
// 		return palabraAdivinada;
// 	}


// 	public void setPalabraAdivinada(StringBuilder palabraAdivinada) {
// 		this.palabraAdivinada = palabraAdivinada;
// 	}


// 	public Integer getPartesAhorcado() {
// 		return partesAhorcado;
// 	}


// 	public void setPartesAhorcado(Integer partesAhorcado) {
// 		this.partesAhorcado = partesAhorcado;
// 	}


// 	public Boolean getPerdido() {
// 		return perdido;
// 	}


// 	public void setPerdido(Boolean perdido) {
// 		this.perdido = perdido;
// 	}


// 	@Override
// 	public BooleanSupplier isPerdido() {
// 	    return () -> this.perdido;
// 	}
	
// 	@Override
//     public String[] getPalabrasAdivinadas() {
//         return palabrasAdivinadas;
//     }


// 	@Override
// 	public void agregarPalabraAdivinada(String palabra) {
// 		// TODO Auto-generated method stub
		
// 	}


// 	@Override
// 	public Object obtenerPalabrasAdivinadasSinNulos() {
// 		// TODO Auto-generated method stub
// 		return null;
// 	}
    
	

// }
