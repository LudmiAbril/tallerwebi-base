package com.tallerwebi.dominio;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long idJugador;
    private Juego juego;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaYhora;

    public Partida() {
    }

    public Partida(Long idJugador, Juego juego) {
        this.idJugador = idJugador;
        this.juego = juego;
        this.fechaYhora = LocalDateTime.now();
    }

    public Juego getJuego() {
        return juego;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    public Long getJugador() {
        return idJugador;
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

}
