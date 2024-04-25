package com.tallerwebi.dominio;

public class Senku {

    //CLASE CONTENEDORA DEL SENKU
    Tablero tablero;
    Integer contadorMovimientos;



    public Senku(Integer cantidadFilasYColumnas){
        if (cantidadFilasYColumnas % 2 != 1) {
            //excepción en Java que indica que se ha producido un error al pasar un argumento a un método
            throw new IllegalArgumentException("La cantidad de filas y columnas debe ser un número impar.");
        }
        this.tablero = new Tablero(cantidadFilasYColumnas);
        this.contadorMovimientos = 0;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public Integer getContadorMovimientos() {
        return contadorMovimientos;
    }

    public void setContadorMovimientos(Integer contadorMovimientos) {
        this.contadorMovimientos = contadorMovimientos;
    }
}
