package com.tallerwebi.dominio;

public class Carta {
    private String simbolo;
    private Integer valor;
    private Palo palo;

    public Carta(String simbolo, Integer valor, Palo palo) {
        this.simbolo = simbolo;
        this.valor = valor;
        this.palo = palo;
    }

    public String getSimbolo() {
        return this.simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public Integer getValor() {
        return this.valor;
    }

    public void setPalo(Palo palo) {
        this.palo = palo;
    }

    public Palo getPalo() {
        return this.palo;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

}
