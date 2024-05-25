package com.tallerwebi.dominio;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PartidaBlackJack extends Partida {
    private Integer puntaje;
    private Boolean blackJack;
    private Boolean gano;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "mm:ss")
    private LocalTime duracion;

    public PartidaBlackJack(Integer puntaje, Boolean blackJack, Boolean gano, LocalTime duracion) {
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