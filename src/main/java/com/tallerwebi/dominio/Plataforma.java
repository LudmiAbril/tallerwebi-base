package com.tallerwebi.dominio;

import java.util.ArrayList;
import com.tallerwebi.dominio.Partida;

public class Plataforma {
    private ArrayList<Partida> partidas ;

    public Plataforma() {
        this.partidas = new ArrayList<Partida>();
    }

    public ArrayList<Partida> getPartidas() {
        return partidas;
    }

    public void setPartidas(ArrayList<Partida> partidas) {
        this.partidas = partidas;
    }
}
