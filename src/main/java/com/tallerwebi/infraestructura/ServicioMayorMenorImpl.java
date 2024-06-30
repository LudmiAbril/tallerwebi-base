package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServicioMayorMenorImpl implements ServicioMayorMenor {
    Baraja baraja = new Baraja();
    List<Carta> cartas = new ArrayList();
    Integer puntaje = 0;


    @Override
    public Carta entregarCartaDelMedio() {
        return sacarCarta();
    }

    @Override
    public void inicializarBaraja() {
        /*for (Palo palo : Palo.values()) {
            for (int valor = 1; valor <= 13; valor++) {
                String nombreCarta;
                int valorCarta;
                switch (valor) {
                    case 1:
                        nombreCarta = "A";
                        valorCarta = 11;
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
        }*/
        baraja = new Baraja(11);
        cartas = baraja.getCartas();
    }

    @Override
    public void barajar() {
        baraja.barajar();
    }

    @Override
    public Carta sacarCarta() {
        return baraja.sacarCarta();
    }

    @Override
    public void comenzarPartida() {

    }

    @Override
    public boolean evaluarCarta(Carta actual, Carta nueva, Valor valor) {
        if(valor.equals(Valor.MAYOR)){
            if(nueva.getValor() > actual.getValor()){
                return true;
            }
            else{
                return false;
            }
        }
        else if(valor.equals(Valor.MENOR)){
            if(nueva.getValor() < actual.getValor()){
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }


    @Override
    public void actualizarEstadoPartida(HttpSession session, Carta nueva, Boolean evaluada) {
        if(evaluada != null){
            session.setAttribute("estadoPartida", "jugando");
        }
    }

    @Override
    public void reiniciarJuego(HttpSession session) {

    }
    @Override
    public Integer cantidadCartasRestantesEnMazo(){
        return cartas.size();
    }

    public Baraja getBaraja() {
        return baraja;
    }

    public void setBaraja(Baraja baraja) {
        this.baraja = baraja;
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
    }

    public Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }
}
