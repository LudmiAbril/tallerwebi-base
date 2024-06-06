package com.tallerwebi.dominio.dto;

import com.tallerwebi.dominio.EstadoJuego;

public class MensajeBingo implements Mensaje{
    private String type;
    private String gameId;
    private String player1;
    private String player2;
    private String winner;
    private String turn;
    private String content;
    private String[][] board;
    private int move;
    private EstadoJuego gameState;
    private String sender;

    public MensajeBingo() {
    }

    /*public MensajeBingo(TicTacToe game) {
        this.gameId = game.getGameId();
        this.player1 = game.getPlayer1();
        this.player2 = game.getPlayer2();
        this.winner = game.getWinner();
        this.turn = game.getTurn();
        this.board = game.getBoard();
        this.gameState = game.getGameState();
    }*/

    /**
     * Getters and Setters
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
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

    public String[][] getBoard() {
        return board;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EstadoJuego getGameState() {
        return gameState;
    }

    public void setGameState(EstadoJuego gameState) {
        this.gameState = gameState;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

}
