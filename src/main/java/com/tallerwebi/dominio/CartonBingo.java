package com.tallerwebi.dominio;

public class CartonBingo {
    
    private Integer[][] numeros;

    public CartonBingo(Integer[][] numeros) {
        this.numeros = numeros;
    }

    public Integer[][] getNumeros() {
        return numeros;
    }

    public void setNumeros(Integer[][] numeros) {
        this.numeros = numeros;
    }

    // public void imprimirCarton() {
    // for (int f = 0; f < numeros.length; f++) {
    // for (int c = 0; c < numeros.length; c++) {
    // System.out.println(numeros[f][c]+"\t");
    // }
    // System.out.println();
    // }
    // }

}
