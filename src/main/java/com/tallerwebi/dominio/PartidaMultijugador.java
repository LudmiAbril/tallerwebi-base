// package com.tallerwebi.dominio;

// public class PartidaMultijugador extends Partida {
//     private Jugador jugador;
//     private Jugador jugador2;
//     private Jugador ganador;

<<<<<<< Updated upstream
//     public PartidaMultijugador(Juego juego, Jugador jugador, Jugador jugador2) {
//         super(juego);
//         this.jugador=jugador;
//         this.jugador2=jugador2;
//     }
=======
 public abstract class PartidaMultijugador extends Partida {
     private String gameId;
     private String nombreJugador;
     private String nombreJugador2;
     private String ganador;
     private String turno;
     private EstadoJuego gameState;
>>>>>>> Stashed changes

//     public Jugador getJugador() {
//         return jugador;
//     }

<<<<<<< Updated upstream
//     public void setJugador(Jugador jugador) {
//         this.jugador = jugador;
//     }
// }
=======
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

     public abstract void iniciarPartida();
     public abstract void realizarMovimiento(String jugador, Object movimiento);
     public abstract boolean verificarGanador();
 }
>>>>>>> Stashed changes
