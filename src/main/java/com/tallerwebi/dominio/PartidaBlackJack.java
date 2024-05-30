package com.tallerwebi.dominio;

import java.time.LocalTime;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tallerwebi.dominio.excepcion.PartidaConPuntajeNegativoException;

import javax.persistence.Entity;

@Entity
public class PartidaBlackJack extends Partida {
    private Integer puntaje;
    private Boolean blackJack;
    private Boolean gano;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "mm:ss")
    private LocalTime duracion;

    public PartidaBlackJack() {

    }

    public PartidaBlackJack(Long idJugador, Integer puntaje, Juego juego, Boolean blackJack, Boolean gano,
            LocalTime duracion) {
        super(idJugador, juego); // Llama al constructor de la clase base
        this.puntaje = puntaje;
        this.blackJack = blackJack;
        this.gano = gano;
        this.duracion = duracion;
    }

    public Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }

    public Boolean getBlackJack() {
        return blackJack;
    }

    public void setBlackJack(Boolean blackJack) {
        this.blackJack = blackJack;
    }

    public Boolean getGano() {
        return gano;
    }

    public void setGano(Boolean esMano) {
        this.gano = esMano;
    }

    public LocalTime getDuracion() {
        return duracion;
    }

    public void setDuracion(LocalTime tiempo) {
        this.duracion = tiempo;
    }

}