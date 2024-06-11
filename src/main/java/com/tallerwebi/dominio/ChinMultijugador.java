package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.ServicioChinImpl;

import java.util.Set;
import java.util.UUID;
import java.util.LinkedHashSet;


public class ChinMultijugador {

        public final ServicioChin servicioChin;
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

        public ChinMultijugador(String player1, String player2) {
            servicioChin = new ServicioChinImpl();
            this.gameId = UUID.randomUUID().toString();
            this.nombreJugador = player1;
            this.nombreJugador2 = player2;
            this.turn = player1;
            //this.cartonJugador1 = servicioChin.generarCarton(5);
            //this.cartonJugador2 = servicioChin.generarCarton(5);
            gameState = EstadoJuego.WAITING_FOR_PLAYER;
        }
        private void checkWinner() {

        }
        public boolean isGameOver() {
            return winner != null;
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




}
