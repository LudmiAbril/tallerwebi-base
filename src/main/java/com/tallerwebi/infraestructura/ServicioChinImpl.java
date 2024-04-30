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

    @Autowired
    public ServicioChinImpl(){
        this.baraja= new Baraja();
    }

    @Override
    public List<Carta> repartirTodasLasCartas() {
        return List.of();
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
    public void ponerCartaEnPilaDeDescarte(Carta carta, ArrayList<Carta> descarte1, ArrayList<Carta> descarte2) {
        //ArrayList<Carta> descarte1= new ArrayList<>();
        //ArrayList<Carta> descarte2= new ArrayList<>();
        Carta carta1 = new Carta("3", 3, Palo.CORAZON);
        Carta carta2 = new Carta("6", 16, Palo.DIAMANTE);
        descarte1.add(carta1);
        descarte2.add(carta2);
        Integer valor1 = carta1.getValor();
        Integer valor2 = carta2.getValor();
        if(sePuedePonerCartaSiguienteOAnterior(carta.getValor(), valor1)){
            descarte1.add(carta);
        }
        if(sePuedePonerCartaSiguienteOAnterior(carta.getValor(), valor2)){
            descarte2.add(carta);
        }
    }
    public boolean sePuedePonerCartaSiguienteOAnterior(Integer miCarta, Integer otroValor){
        boolean sePuede=false;
        if(miCarta.equals(otroValor+1) || miCarta.equals(otroValor-1))
            sePuede=true;
        return sePuede;
    }
    @Override
    public void guardarResultadoDePartida(Juego CHIN, Integer puntaje) {

    }
}
