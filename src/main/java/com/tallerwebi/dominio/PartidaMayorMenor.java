package com.tallerwebi.dominio;


import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class PartidaMayorMenor extends Partida{
    @Column(name = "aciertos")
    private Integer puntaje;


    public PartidaMayorMenor(Long idJugador, Juego juego, Integer puntaje){
        super(idJugador, juego);
        this.puntaje=puntaje;
    }
    public PartidaMayorMenor(){}
    public Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }
}
