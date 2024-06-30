package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder.In;

import net.bytebuddy.implementation.bind.annotation.This;

public class Baraja {
    private List<Carta> cartas;
    private Integer size;
    private Integer valorAs;

    public Integer getSize() {
        return size;
    }

    public Baraja(Integer valorAs) {
        this.cartas = new ArrayList<>();
        this.valorAs = valorAs;
        inicializarBaraja(valorAs);
        this.size = cartas.size();
    }

    public Baraja() {
        this.cartas = new ArrayList<>();
        this.valorAs = 1;
        inicializarBaraja(valorAs);
    }

    private void inicializarBaraja(Integer valorAs) {
        for (Palo palo : Palo.values()) {
            for (int valor = 1; valor <= 13; valor++) {
                String nombreCarta;
                int valorCarta;
                switch (valor) {
                    case 1:
                        nombreCarta = "A";
                        valorCarta = valorAs;
                        break;
                    case 11:
                        nombreCarta = "J";
                        valorCarta = 10;
                        break;
                    case 12:
                        nombreCarta = "Q";
                        valorCarta = 10;
                        break;
                    case 13:
                        nombreCarta = "K";
                        valorCarta = 10;
                        break;
                    default:
                        nombreCarta = Integer.toString(valor);
                        valorCarta = valor;
                        break;
                }
                cartas.add(new Carta(nombreCarta, valorCarta, palo));
            }
        }
    }

    public void barajar() {
        Collections.shuffle(cartas);
    }

    public Carta sacarCarta() {
        if(cartas.size()==0){
            inicializarBaraja(1);
        }
        Carta carta = cartas.remove(0);

        size = cartas.size();
        return carta;
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getValorAs() {
        return valorAs;
    }

    public void setValorAs(Integer valorAs) {
        this.valorAs = valorAs;
    }
    public String toString(){
            StringBuilder sb = new StringBuilder();
            for (Carta carta : cartas) {
                sb.append(carta.toString()).append("\n");
            }
            return sb.toString();
        }

}
