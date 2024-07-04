package com.tallerwebi.dominio;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "partida_senku")
public class PartidaSenku extends Partida {
    
    @Column(name = "ganado")
    private Boolean ganado;

    @Column(name = "cantidad_movimientos")
    private Integer cantidadMovimientos;

    public PartidaSenku() {
    }
    public PartidaSenku(Long idJugador, Juego juego, Boolean ganado, Integer cantidadMovimientos) {
        super(idJugador, juego);
        this.ganado = ganado;
        this.cantidadMovimientos = cantidadMovimientos;
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
