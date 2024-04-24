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

    public Integer getValor() {
        return this.valor;
    }

    public Palo getPalo() {
        return this.palo;
    }

}