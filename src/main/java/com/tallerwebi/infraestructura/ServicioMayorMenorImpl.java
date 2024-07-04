package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("servicioMayorMenor")
public class ServicioMayorMenorImpl implements ServicioMayorMenor {
    Baraja baraja = new Baraja();
    List<Carta> cartas = new ArrayList();
    Integer puntaje = 0;
    Valor valor;


    @Override
    public Carta entregarCartaDelMedio() {
        return sacarCarta();
    }

    @Override
    public void inicializarBaraja() {

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
    public void setValorElegido(Valor valorElegido){ this.valor = valorElegido;}
    public Valor getValorElegido(){ return valor;}
}
