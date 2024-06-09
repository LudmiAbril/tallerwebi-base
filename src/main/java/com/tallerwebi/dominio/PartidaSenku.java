package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class PartidaSenku extends Partida {
    
    private Boolean ganado;
    private Integer cantidadMovimientos;

    public PartidaSenku(Long idJugador, Juego juego, Boolean ganado, Integer cantidadMovimientos) {
        super(idJugador, juego);
        this.ganado = ganado;
        this.cantidadMovimientos = cantidadMovimientos;
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
}
