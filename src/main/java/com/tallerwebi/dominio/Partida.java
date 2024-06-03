package com.tallerwebi.dominio;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partida_seq")
    @SequenceGenerator(name = "partida_seq", sequenceName = "partida_sequence", allocationSize = 1)
    private long id;
    private String jugador;

    private Long idJugador;
    protected Juego juego;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaYhora;

    public Partida() {
    }

    public Partida(Long idJugador, Juego juego) {
        this.idJugador = idJugador;
        this.juego = juego;
        this.fechaYhora = LocalDateTime.now();
    }
    public Partida(String jugador, Juego juego){
        this.jugador = jugador;
        this.juego = juego;
    }
    public Juego getJuego() {
        return juego;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    public String getJugador() {
        return jugador;
    }

    public void setJugador(Long idJugador) {
        this.idJugador = idJugador;
    }

    public void setFechaYhora(LocalDateTime fechaYhora) {
        this.fechaYhora = fechaYhora;
    }

    public LocalDateTime getFechaYhora() {
        return this.fechaYhora;
    }

    public Long getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(Long idJugador) {
        this.idJugador = idJugador;
    }

    public void setJugador(String jugador) {
        this.jugador = jugador;
    }
}
