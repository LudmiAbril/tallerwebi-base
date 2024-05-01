package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("servicioChin")
public class ServicioChinImpl implements ServicioChin {
    private Baraja baraja;
    private ArrayList<Carta> descarte1 = new ArrayList<>();
    private ArrayList<Carta> descarte2 = new ArrayList<>();
    private ArrayList<Carta> mazoJugador1 = new ArrayList<>();
    private ArrayList<Carta> mazoJugador2 = new ArrayList<>();
    private ArrayList<Carta> manoJugador1 = new ArrayList<>();
    private ArrayList<Carta> manoJugador2 = new ArrayList<>();

    @Autowired
    public ServicioChinImpl(){
        this.baraja= new Baraja();
    }

    @Override
    public void repartirTodasLasCartas(ArrayList<Carta> mazoJugador1, ArrayList<Carta> mazoJugador2) {

        for(int i =0; i< (baraja.getSize()/2); i++){
            mazoJugador1.add(baraja.sacarCarta());
        }
        for(int i =baraja.getSize()/2; i< baraja.getSize(); i++){
            mazoJugador2.add(baraja.sacarCarta());
        }
    }

    @Override
    public List<Carta> repartirCuatroCartasDeFrente(Jugador jugador) {
        //ArrayList<Carta>
        return List.of();
    }

    @Override
    public boolean hayChin(ArrayList<Carta> descarte1, ArrayList<Carta> descarte2) {

        boolean iguales = false;
        Integer cantidadUno= descarte1.size();
        Integer cantidadDos = descarte2.size();
        Integer ultimoValorUno = descarte1.get(cantidadUno-1).getValor();
        Integer ultimoValorDos = descarte2.get(cantidadDos-1).getValor();
        if(ultimoValorUno.equals(ultimoValorDos))
            iguales=true;
        return iguales;
    }

    @Override
    public Carta pedirUnaCartaExtra() {
        this.baraja.barajar();
        return this.baraja.sacarCarta();
    }

    @Override
    public void ponerCartaEnPilaDeDescarte(Carta cartaJugada, ArrayList<Carta> descarte1, ArrayList<Carta> descarte2) {
        if (descarte1.isEmpty() && descarte2.isEmpty()) {
            return;
        }

        Integer valorJugada = cartaJugada.getValor();

        Integer ultimoValorDescarte1 = !descarte1.isEmpty() ? descarte1.get(descarte1.size() - 1).getValor() : null;
        Integer ultimoValorDescarte2 = !descarte2.isEmpty() ? descarte2.get(descarte2.size() - 1).getValor() : null;

        if (ultimoValorDescarte1 != null && (valorJugada.equals(ultimoValorDescarte1 - 1) || valorJugada.equals(ultimoValorDescarte1 + 1))) {
            descarte1.add(cartaJugada);
        }

        else if (ultimoValorDescarte2 != null && (valorJugada.equals(ultimoValorDescarte2 - 1) || valorJugada.equals(ultimoValorDescarte2 + 1))) {
            descarte2.add(cartaJugada);
        }
    }

    @Override
    public void guardarResultadoDePartida(Juego CHIN, Integer puntaje) {


    }

    @Override
    public void sacarDelMazoYPonerEnMano(ArrayList<Carta> mazoJugador1, ArrayList<Carta> manoJugador1) {
        //ArrayList<Carta> manoJugador1 = new ArrayList<>();

        manoJugador1.add(mazoJugador1.remove(mazoJugador1.size()-1));
    }
}