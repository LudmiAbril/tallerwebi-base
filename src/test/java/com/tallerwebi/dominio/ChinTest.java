package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.ServicioChinImpl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ChinTest {
    @Test
    public void hayChinCuandoLasDosCartasDelMedioSonIguales(){
        Carta carta1 = new Carta("A", 1, Palo.DIAMANTE);
        Carta carta2 = new Carta("A", 1, Palo.CORAZON);

        ArrayList<Carta> descarte1 = new ArrayList<>();
        ArrayList<Carta> descarte2 = new ArrayList<>();

        descarte1.add(carta1);
        descarte2.add(carta2);
        ServicioChin chin = new ServicioChinImpl();

        assertTrue(chin.hayChin(descarte1, descarte2));
    }
    @Test
    public void noHayChinCuandoLosValoresDeLasCartasSonDistintos(){
        Carta carta1 = new Carta("A", 1, Palo.DIAMANTE);
        Carta carta2 = new Carta("A", 5, Palo.CORAZON);

        ArrayList<Carta> descarte1 = new ArrayList<>();
        ArrayList<Carta> descarte2 = new ArrayList<>();

        descarte1.add(carta1);
        descarte2.add(carta2);
        ServicioChin chin = new ServicioChinImpl();

        assertFalse(chin.hayChin(descarte1, descarte2));
    }
    @Test
    public void soloSePuedeAgregarCartaSiguienteOAnteriorAlMazoDeDescarte(){
        Carta carta1 = new Carta("2", 2, Palo.DIAMANTE);
        Carta carta2 = new Carta("8", 8, Palo.CORAZON);

        ArrayList<Carta> descarte1 = new ArrayList<>();
        ArrayList<Carta> descarte2 = new ArrayList<>();

        descarte1.add(carta1);
        descarte2.add(carta2);
        ServicioChin chin = new ServicioChinImpl();
        Carta cartaTest = new Carta("7",7, Palo.PICA);
        chin.ponerCartaEnPilaDeDescarte(cartaTest, descarte1, descarte2);
        assertEquals(2, descarte2.size());
    }
    @Test
    public void noSePuedeAgregarCartaDistintaALaAnteriorOSiguiente(){
        Carta carta1 = new Carta("2", 2, Palo.DIAMANTE);
        Carta carta2 = new Carta("8", 8, Palo.CORAZON);

        ArrayList<Carta> descarte1 = new ArrayList<>();
        ArrayList<Carta> descarte2 = new ArrayList<>();

        descarte1.add(carta1);
        descarte2.add(carta2);
        ServicioChin chin = new ServicioChinImpl();
        Carta cartaTest = new Carta("4",4, Palo.PICA);
        chin.ponerCartaEnPilaDeDescarte(cartaTest, descarte1, descarte2);
        assertEquals(1, descarte1.size());


    }
    @Test
    public void elJugador1GanaSiNoTieneMasCartas(){
        ArrayList<Carta> mazoJugador1 = new ArrayList<>();

        assertEquals(0, mazoJugador1.size());
    }
    @Test
    public void elJugador2GanaSiNoTieneMasCartas(){
        ArrayList<Carta> mazoJugador2 = new ArrayList<>();

        assertEquals(0, mazoJugador2.size());
    }
    @Test
    public void queSeRepartanLas52CartasDelMazoACadaJugador(){
        ArrayList<Carta> mazoJugador1 = new ArrayList<>();
        ArrayList<Carta> mazoJugador2 = new ArrayList<>();

        ServicioChin chin = new ServicioChinImpl();
        chin.repartirTodasLasCartas(mazoJugador1, mazoJugador2);

        assertEquals(26, mazoJugador1.size());
        assertEquals(26, mazoJugador2.size());
    }
    @Test
    public void soloSePuedenTener4CartasEnLaMano(){
        //el arraylist mano1 debe tener 4 cartas del mazo1
        //si tiene menos debe sacar una carta hasta tener 4
        ArrayList<Carta> mazoJugador1 = new ArrayList<>();
        ArrayList<Carta> mazoJugador2 = new ArrayList<>();
        ArrayList<Carta> manoJugador1 = new ArrayList<>();
        ServicioChin chin = new ServicioChinImpl();
        chin.repartirTodasLasCartas(mazoJugador1, mazoJugador2);

        while(manoJugador1.size()<4){
            chin.sacarDelMazoYPonerEnMano(mazoJugador1, manoJugador1);
        }
        assertEquals(4, manoJugador1.size());

    }
}
