package com.tallerwebi.dominio;

public class Tablero {
    private Casillero[][] casilleros;
    private Integer cantidadFilasYColumnas;

    public Tablero(Integer cantidadFilasYColumnas) {
        this.cantidadFilasYColumnas = cantidadFilasYColumnas;
        this.casilleros = new Casillero[cantidadFilasYColumnas][cantidadFilasYColumnas];
        // ALINCIO DEL JUEGO,LLENO EL TABLERO DE FICHAS (OCUPADO (true))
        for (int i = 0; i < cantidadFilasYColumnas; i++) {
            for (int j = 0; j < cantidadFilasYColumnas; j++) {
                casilleros[i][j] = new Casillero();
            }
        }
        // VERIFICO QUE EL NUMERO DE COLUMNAS Y CASILLERO SEA IMPAR Y ENTONCES DESOCUPO LA DEL MEDIO
        if (cantidadFilasYColumnas % 2 == 1) {
            int filaCentral = cantidadFilasYColumnas / 2;
            int columnaCentral = cantidadFilasYColumnas / 2;
            casilleros[filaCentral][columnaCentral].setOcupado(false);
        }
    }

    public Casillero[][] getCasilleros() {
        return casilleros;
    }

    public void setCasilleros(Casillero[][] casilleros) {
        this.casilleros = casilleros;
    }

    public Integer getCantidadFilasYColumnas() {
        return cantidadFilasYColumnas;
    }

    public void setCantidadFilasYColumnas(Integer cantidadFilasYColumnas) {
        this.cantidadFilasYColumnas = cantidadFilasYColumnas;
    }
}

