package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Baraja {
    private List<Carta> cartas;

    public Baraja() {
        cartas = new ArrayList<>();
        inicializarBaraja();
    }

    private void inicializarBaraja() {
        for (Palo palo : Palo.values()) {
            for (int valor = 1; valor <= 13; valor++) {
                String nombreCarta;
                int valorCarta;
                switch (valor) {
                    case 1:
                        nombreCarta = "A";
                        valorCarta = 11; // Valor inicial del as
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

        return cartas.remove(0);
    }

}
