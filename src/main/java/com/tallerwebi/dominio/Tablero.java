package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.CasilleroInexistenteException;
import com.tallerwebi.dominio.excepcion.MovimientoInvalidoException;

public class Tablero {
    private Casillero[][] casilleros;
    private Integer cantidadFilasYColumnas;
    private Integer contadorMovimientos;
    private Boolean ganado;



    public Tablero(Integer cantidadFilasYColumnas) {
        this.ganado=false;

        this.cantidadFilasYColumnas = cantidadFilasYColumnas;
        this.contadorMovimientos = 0;
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


    public Boolean validarMovimiento(Casillero seleccionado, Casillero destino) throws MovimientoInvalidoException {
        validarDistancia(seleccionado, destino);
        validarDestinoLiberado(destino);
        validarCasilleroIntermedio(seleccionado, destino);

        return true;
    }

    private void validarDestinoLiberado(Casillero destino) throws MovimientoInvalidoException {
        if (destino.getOcupado()) {
            throw new MovimientoInvalidoException("El movimiento no es válido. Debe moverse a un espacio libre.");
        }
    }

    private void validarDistancia(Casillero seleccionado, Casillero destino) throws MovimientoInvalidoException {
        int distanciaX = Math.abs(seleccionado.getCoordenadaX() - destino.getCoordenadaX());
        int distanciaY = Math.abs(seleccionado.getCoordenadaY() - destino.getCoordenadaY());
        if (!((distanciaX == 2 && distanciaY == 0) || (distanciaX == 0 && distanciaY == 2))) {
            throw new MovimientoInvalidoException("El movimiento no es válido. Debe moverse exactamente 2 posiciones en horizontal o vertical.");
        }
    }

    private Boolean validarCasilleroIntermedio(Casillero seleccionado, Casillero destino) throws MovimientoInvalidoException {
        int xIntermedio = (seleccionado.getCoordenadaX() + destino.getCoordenadaX()) / 2;
        int yIntermedio = (seleccionado.getCoordenadaY() + destino.getCoordenadaY()) / 2;

        if (!casilleros[xIntermedio][yIntermedio].getOcupado()) {
            throw new MovimientoInvalidoException("El movimiento no es válido. Debe capturar una ficha en posición intermedia.");
        }

        return true;
    }


    public void realizarMovimiento(Casillero seleccionado, Casillero destino) {
        //AL LUGAR A DONDE SE MUEVE LA FICHA,LO OCUPO
        destino.setOcupado(true);

        //CALCULAR COORDENADAS DEL CASILLERO A SALTAR -INTERMEDIO
        int xIntermedio = (seleccionado.getCoordenadaX() + destino.getCoordenadaX()) / 2;
        int yIntermedio = (seleccionado.getCoordenadaY() + destino.getCoordenadaY()) / 2;

        //AL INTERMEDIO LO DESOCUPO PORQUE ME COMÍ LA FICHA
        casilleros[xIntermedio][yIntermedio].setOcupado(false);

        //EL SELECCIONADO(ORIGEN)SE DESOCUPA
        seleccionado.setOcupado(false);
        this.contadorMovimientos++;
    }

    public Integer getContadorMovimientos() {
        return contadorMovimientos;
    }

    public void setContadorMovimientos(Integer contadorMovimientos) {
        this.contadorMovimientos = contadorMovimientos;
    }

    public boolean hayMovimientoDisponibleEnTablero() throws MovimientoInvalidoException {
        for (int i = 0; i < cantidadFilasYColumnas; i++) {
            for (int j = 0; j < cantidadFilasYColumnas; j++) {
                Casillero casilleroActual = casilleros[i][j];
                if (!casilleroActual.getOcupado()) {
                    //mover una ficha desde dos casilleros arriba.
                    if (esMovimientoValido(i, j, i - 2, j)) {
                        return true;
                    }
                    //mover una ficha desde dos casilleros abajo.
                    if (esMovimientoValido(i, j, i + 2, j)) {
                        return true;
                    }
                    //mover una ficha desde dos casilleros a la izq
                    if (esMovimientoValido(i, j, i, j - 2)) {
                        return true;
                    }
                    //mover una ficha desde dos casilleros a la der
                    if (esMovimientoValido(i, j, i, j + 2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    



    public boolean esMovimientoValido(int xInicial, int yInicial, int xDestino, int yDestino) throws MovimientoInvalidoException {
        //REVISO QUE SEA UNA JUGADA DENTRO DE LOS LIMITES DEL TABLERO
        if (xDestino >= 0 && xDestino < cantidadFilasYColumnas &&
                yDestino >= 0 && yDestino < cantidadFilasYColumnas) {
            //UBICO LOS CASILLEROS QUE SON PARTE DE LA JUGADA/MOV
            Casillero seleccionado = casilleros[xInicial][yInicial];
            Casillero destino = casilleros[xDestino][yDestino];

            //REVISO QUE EL MOV ES VALIDO
            return validarMovimiento(seleccionado, destino);
        }
        return false;

}

    public Boolean ganar() {
        for (int i = 0; i < cantidadFilasYColumnas; i++) {
            for (int j = 0; j < cantidadFilasYColumnas; j++) {
                if (casilleros[i][j].getOcupado()) {
                    return this.ganado=false;
                }else{
                    this.ganado = true;
            } }
        }

        return this.ganado;
    }

    public Boolean getGanado() {
        return ganado;
    }

    public void setGanado(Boolean ganado) {
        this.ganado = ganado;
    }
}



