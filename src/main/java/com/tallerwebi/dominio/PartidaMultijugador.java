 package com.tallerwebi.dominio;

 import java.util.UUID;

 public class PartidaMultijugador extends Partida {
     private String gameId;
     private String nombreJugador;
     private String nombreJugador2;
     private String ganador;
     private String turno;
     private EstadoJuego gameState;

     public PartidaMultijugador(Juego juego, String nombreJugador, String nombreJugador2) {
         //super(juego);
         //Crea un id unico
         this.gameId = UUID.randomUUID().toString();
         this.juego = juego;
         this.nombreJugador= nombreJugador;
         this.nombreJugador2=nombreJugador2;
         gameState = EstadoJuego.WAITING_FOR_PLAYER;
     }

     public String getJugador() {
         return nombreJugador;
     }

     public void setJugador(String jugador) {
         this.nombreJugador = jugador;
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

     public void setNombreJugador(String nombreJugador) {
         this.nombreJugador = nombreJugador;
     }

     public String getNombreJugador2() {
         return nombreJugador2;
     }

     public void setNombreJugador2(String nombreJugador2) {
         this.nombreJugador2 = nombreJugador2;
     }

     public String getGanador() {
         return ganador;
     }

     public void setGanador(String ganador) {
         this.ganador = ganador;
     }

     public String getTurno() {
         return turno;
     }

     public void setTurn(String turno) {
         this.turno = turno;
     }

     public EstadoJuego getGameState() {
         return gameState;
     }

     public void setGameState(EstadoJuego gameState) {
         this.gameState = gameState;
     }
 }
