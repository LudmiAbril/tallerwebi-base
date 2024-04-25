package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.CasilleroInexistenteException;

public class Tablero {
    private Casillero[][] casilleros;
    private Integer cantidadFilasYColumnas;



    public Tablero(Integer cantidadFilasYColumnas) {
        this.cantidadFilasYColumnas = cantidadFilasYColumnas;
        this.casilleros = new Casillero[cantidadFilasYColumnas][cantidadFilasYColumnas];
        inicializarCasilleros();
        if (cantidadFilasYColumnas % 2 == 1) {
            int filaCentral = cantidadFilasYColumnas / 2;
            int columnaCentral = cantidadFilasYColumnas / 2;
            casilleros[filaCentral][columnaCentral].setOcupado(false);
        }
        // VERIFICO QUE EL NUMERO DE COLUMNAS Y CASILLERO SEA IMPAR Y ENTONCES DESOCUPO LA DEL MEDIO
        VaciarCasilleroCentral(cantidadFilasYColumnas);
    }
    private void inicializarCasilleros() {
        for (int i = 0; i < cantidadFilasYColumnas; i++) {
            for (int j = 0; j < cantidadFilasYColumnas; j++) {
                casilleros[i][j] = new Casillero(i, j); // Inicializar cada casillero con sus coordenadas x e y
            }
        }
    }
    private void VaciarCasilleroCentral(Integer cantidadFilasYColumnas) {
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


    public Casillero buscarCasilleroPorCoordenadas(Integer x, Integer y) throws CasilleroInexistenteException {
        for (int i = 0; i < cantidadFilasYColumnas; i++) {
            for (int j = 0; j < cantidadFilasYColumnas; j++) {
                if (casilleros[i][j].getCoordenadaX() == x && casilleros[i][j].getCoordenadaY() == y) {
                    return casilleros[i][j];
                }
            }
        }
        throw new CasilleroInexistenteException("Casillero inexistente en las coordenadas: (" + x + ", " + y + ")");
    }
}


