package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.ServicioBingoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

//extends PartidaMultijugador
@Service("servicioBingoMultijugador")
public class BingoMultijugador {
    @Autowired
    public final ServicioBingo servicioBingo;
    
    private String gameId;
    private CartonBingo cartonJugador1;
    private CartonBingo cartonJugador2;
    private String nombreJugador;
    private String nombreJugador2;
    private String winner;
    private String turn;
    private EstadoJuego gameState;
    private Integer dimension;
    private Set<Integer> numerosEntregados;

    public BingoMultijugador(String player1, String player2) {
        servicioBingo = new ServicioBingoImpl();
        this.gameId = UUID.randomUUID().toString();
        this.nombreJugador = player1;
        this.nombreJugador2 = player2;
        this.turn = player1;
        this.cartonJugador1 = servicioBingo.generarCarton(5);
        this.cartonJugador2 = servicioBingo.generarCarton(5);
        gameState = EstadoJuego.WAITING_FOR_PLAYER;
    }
    public BingoMultijugador(){
        servicioBingo = new ServicioBingoImpl();
    }
    private void checkWinner() {

        if(servicioBingo.linea(servicioBingo.getNumerosMarcadosEnElCarton(), cartonJugador1)){
            setWinner(nombreJugador);
            return;
        }
        else if(servicioBingo.linea(servicioBingo.getNumerosMarcadosEnElCarton(), cartonJugador2)){
            setWinner(nombreJugador2);
            return;
        }
    }
    public boolean isGameOver() {
        return winner != null || servicioBingo.getSeHizobingo();
    }


    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String player1) {
        this.nombreJugador = player1;
    }

    public String getNombreJugador2() {
        return nombreJugador2;
    }

    public void setNombreJugador2(String player2) {
        this.nombreJugador2 = player2;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public EstadoJuego getGameState() {
        return gameState;
    }

    public void setGameState(EstadoJuego gameState) {
        this.gameState = gameState;
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
