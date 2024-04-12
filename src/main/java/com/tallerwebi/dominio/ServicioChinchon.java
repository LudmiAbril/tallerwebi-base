package com.tallerwebi.dominio;

import java.util.Comparator;
import java.util.List;


public class ServicioChinchon {
    public boolean hayChinchon(List<Carta> mano) {

        // ordenarlas
        mano.sort(Comparator.comparing(Carta::getNumero));

        // verificar que sean consecutivas (ascendente)
        Integer valorAComparar = 0;

        for (Carta carta : mano) {
            if (!(carta.getNumero() == valorAComparar + 1)) {
                return false;
            }
            valorAComparar = carta.getNumero();
        }

        // verfificar que sean del mismo palo
        Carta carta1 = mano.get(0);

        for (Carta cartaActual : mano) {
            if (!carta1.getPalo().equals(cartaActual.getPalo())) {
                return false;
            }
        }
        return true;
    }
}
