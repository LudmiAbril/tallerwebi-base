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

    @Override
    public String toString() {
        return valor + " de " + palo;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Carta carta = (Carta) o;

        if (valor != carta.valor) return false;
        if (!simbolo.equals(carta.simbolo)) return false;
        return palo == carta.palo;
    }

    @Override
    public int hashCode() {
        int result = simbolo.hashCode();
        result = 31 * result + valor;
        result = 31 * result + (palo != null ? palo.hashCode() : 0);
        return result;
    }
}
