package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("servicioChin")
public class ServicioChinImpl implements ServicioChin {
    private Baraja baraja;
    private ArrayList<Carta> descarte1;
    private ArrayList<Carta> descarte2;
    private ArrayList<Carta> mazoJugador1;
    private ArrayList<Carta> mazoJugador2;
    private ArrayList<Carta> manoJugador1;
    private ArrayList<Carta> manoJugador2;
    private boolean hayChin;

    @Autowired
    public ServicioChinImpl(){
        this.baraja= new Baraja();
        descarte1 = new ArrayList<>();
        descarte2 = new ArrayList<>();
        mazoJugador1 = new ArrayList<>();
        mazoJugador2 = new ArrayList<>();
        manoJugador1 = new ArrayList<>();
        manoJugador2 = new ArrayList<>();
        hayChin = false;
    }

    @Override
    public void repartirTodasLasCartas(ArrayList<Carta> mazoJugador1, ArrayList<Carta> mazoJugador2) {
        for(int i =0; i< (26); i++){
            mazoJugador1.add(baraja.sacarCarta());
        }
        for(int i =0; i< 26; i++){
            mazoJugador2.add(baraja.sacarCarta());
        }
//        for(int i =0; i< this.baraja.getSize(); i++){
//            if(i % 2 ==0){
//                mazoJugador1.add(baraja.sacarCarta());
//            }else{
//                mazoJugador2.add(baraja.sacarCarta());
//            }
//        }
//        }
    }

    @Override
    public List<Carta> repartirCuatroCartasDeFrente(ArrayList<Carta> mazoJugador1, ArrayList<Carta> manoJugador1) {
        for(Integer i =0; i < 4; i++){
            manoJugador1.add(mazoJugador1.remove(mazoJugador1.size()-1));
        }
        return manoJugador1;
    }

    @Override
    public boolean hayChin(ArrayList<Carta> descarte1, ArrayList<Carta> descarte2) {
        Integer cantidadUno= descarte1.size();
        Integer cantidadDos = descarte2.size();
        Integer ultimoValorUno = descarte1.get(cantidadUno-1).getValor();
        Integer ultimoValorDos = descarte2.get(cantidadDos-1).getValor();
        if(ultimoValorUno.equals(ultimoValorDos))
            this.hayChin=true;
        return hayChin;
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
    public void guardarResultadoDePartida(Juego CHIN, Integer puntaje) {}

    @Override
    public void sacarDelMazoYPonerEnMano(ArrayList<Carta> mazoJugador1, ArrayList<Carta> manoJugador1) {
        manoJugador1.add(mazoJugador1.remove(mazoJugador1.size()-1));
    }

    @Override
    public boolean sePuedenAgregarCartasAlDescarte(ArrayList<Carta> descarte1, ArrayList<Carta> descarte2, ArrayList<Carta> manoJugador1, ArrayList<Carta> manoJugador2) {
        Boolean sePuede = false;
        if (descarte1.isEmpty() && descarte2.isEmpty()) {
            return sePuede;
        }
        //Mientras haya una carta que sea posterior o anterior es true
        Integer valor1 = descarte1.get(descarte1.size()-1).getValor();
        Integer valor2 = descarte1.get(descarte1.size()-1).getValor();
        for(Carta cartaAComprobar : manoJugador1){
            if(cartaAComprobar.getValor().equals(valor1 +1 ) || cartaAComprobar.getValor().equals(valor1 -1 )){
                sePuede=true;
            }
        }
        for(Carta cartaAComprobar : manoJugador2){
            if(cartaAComprobar.getValor().equals(valor2 +1 ) || cartaAComprobar.getValor().equals(valor2 -1 )){
                sePuede=true;
            }
        }
        return sePuede;
    }
}
