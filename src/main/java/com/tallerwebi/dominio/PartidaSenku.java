package com.tallerwebi.dominio;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
public class PartidaSenku extends Partida {
    @Column(nullable = false)
    private Boolean ganado;
    @Column(nullable = false)
    private Integer puntaje;
    @Column(nullable = false)
    private Integer cantidadMovimientos;

    public PartidaSenku(Long idJugador, Juego juego, Boolean ganado, Integer cantidadMovimientos, Integer puntaje) {
        super(idJugador, juego);
        this.ganado = ganado;
        this.cantidadMovimientos = cantidadMovimientos;
        this.puntaje = puntaje;
        this.setFechaYhora(LocalDateTime.now()); 
    }

    public PartidaSenku() {
    }

    public Boolean getGanado() {
        return ganado;
    }

    public void setGanado(Boolean ganado) {
        this.ganado = ganado;
    }

    public Integer getCantidadMovimientos() {
        return cantidadMovimientos;
    }

    public void setCantidadMovimientos(Integer cantidadMovimientos) {
        this.cantidadMovimientos = cantidadMovimientos;
    }

    public Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }
}
