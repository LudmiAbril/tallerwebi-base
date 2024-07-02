package com.tallerwebi.dominio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ConfiguracionesJuego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer duracionBlackjack;
    private Integer valorDelAs;
    private Integer cantidadDePelotas;
    private Integer dimensionCarton;
    private Integer dimensionTablero;
    private Integer maxMovimientos;
    public ConfiguracionesJuego() {
        this.duracionBlackjack = 5;
        this.valorDelAs = 11;
        this.cantidadDePelotas = 90;
        this.dimensionCarton = 5;
        this.dimensionTablero = 5;
        this.maxMovimientos = 20;
    }

    public Integer getDuracionBlackjack() {
        return duracionBlackjack;
    }

    public void setDuracionBlackjack(Integer duracionBlackjack) {
        this.duracionBlackjack = duracionBlackjack;
    }

    public Integer getValorDelAs() {
        return valorDelAs;
    }

    public void setValorDelAs(Integer valorDelAs) {
        this.valorDelAs = valorDelAs;
    }

    public Integer getCantidadDePelotas() {
        return cantidadDePelotas;
    }

    public void setCantidadDePelotas(Integer cantidadDePelotas) {
        this.cantidadDePelotas = cantidadDePelotas;
    }

    public Integer getDimensionCarton() {
        return dimensionCarton;
    }

    public void setDimensionCarton(Integer dimensionCarton) {
        this.dimensionCarton = dimensionCarton;
    }

    public Integer  getDimensionTablero() {
       
        return this.dimensionTablero;
    }

    public void setDimensionTablero(Integer dimensionTablero) {
        this.dimensionTablero = dimensionTablero;
    }
    public Integer getMaxMovimientos() {
       
        return this.maxMovimientos;
    }
    public void setMaxMovimientos(Integer maxMovimientos) {
        this.maxMovimientos = maxMovimientos;
    }
}
