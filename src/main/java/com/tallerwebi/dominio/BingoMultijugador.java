package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.ServicioBingoImpl;

import java.util.LinkedHashSet;
import java.util.Set;

public class PartidaBingoMultijugador extends PartidaMultijugador{
    Integer dimension;
    private CartonBingo cartonJugador1;
    private CartonBingo cartonJugador2;
    ServicioBingo servicioBingo = new ServicioBingoImpl();
    private Set<Integer> numerosEntregados;

    public PartidaBingoMultijugador(String nombreJugador, String nombreJugador2) {
        super(nombreJugador, nombreJugador2);
        this.numerosEntregados = new LinkedHashSet<>();
        this.cartonJugador1 = servicioBingo.generarCarton(dimension);
        this.cartonJugador2 = servicioBingo.generarCarton(dimension);
    }
    @Override
    public void iniciarPartida() {
        // Lógica para iniciar una partida de bingo
    }
    @Override
    public void realizarMovimiento(String jugador, Object movimiento) {
        // Lógica para realizar un movimiento en una partida de bingo
    }

    @Override
    public boolean verificarGanador() {
        // Lógica para verificar si hay un ganador en una partida de bingo
        return false;
    }

    public Integer getDimension() {
        return dimension;
    }

    public void setDimension(Integer dimension) {
        this.dimension = dimension;
    }

    public Set<Integer> getNumerosEntregados() {
        return numerosEntregados;
    }

    public void setNumerosEntregados(Set<Integer> numerosEntregados) {
        this.numerosEntregados = numerosEntregados;
    }

    public CartonBingo getCartonJugador1() {
        return cartonJugador1;
    }

    public void setCartonJugador1(CartonBingo cartonJugador1) {
        this.cartonJugador1 = cartonJugador1;
    }

    public CartonBingo getCartonJugador2() {
        return cartonJugador2;
    }

    public void setCartonJugador2(CartonBingo cartonJugador2) {
        this.cartonJugador2 = cartonJugador2;
    }
}
