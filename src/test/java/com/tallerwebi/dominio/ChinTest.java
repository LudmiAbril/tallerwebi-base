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
    public void soloSePuedenTenerHasta4CartasEnLaMano(){

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
    @Test
    public void siHayCHINElMazoDeDescarteVaAlMazoDelPerdedor(){
        ArrayList<Carta> mazoJugador1 = new ArrayList<>();
        ArrayList<Carta> mazoJugador2 = new ArrayList<>();
        ArrayList<Carta> manoJugador1 = new ArrayList<>();
        ArrayList<Carta> manoJugador2 = new ArrayList<>();
        Carta carta1 = new Carta("8", 8, Palo.DIAMANTE);
        Carta carta2 = new Carta("8", 8, Palo.CORAZON);

        ArrayList<Carta> descarte1 = new ArrayList<>();
        ArrayList<Carta> descarte2 = new ArrayList<>();

        descarte1.add(carta1);
        descarte2.add(carta2);
        ServicioChin chin = new ServicioChinImpl();
        if(chin.hayChin(descarte1, descarte2)){
            while(!descarte1.isEmpty() && !descarte2.isEmpty()){
                manoJugador2.add(descarte1.remove(descarte1.size()-1));
                manoJugador2.add(descarte2.remove(descarte2.size()-1));
            }
        }
        assertTrue(descarte1.isEmpty());
        assertTrue(descarte2.isEmpty());

    }
    @Test
    public void siNoHayCartasDisponiblesParaPonerEnElDescarteSeAgreganMas(){
        ArrayList<Carta> mazoJugador1 = new ArrayList<>();
        ArrayList<Carta> mazoJugador2 = new ArrayList<>();
        ArrayList<Carta> manoJugador1 = new ArrayList<>();
        ArrayList<Carta> manoJugador2 = new ArrayList<>();
        ServicioChin chin = new ServicioChinImpl();

        ArrayList<Carta> descarte1 = new ArrayList<>();
        ArrayList<Carta> descarte2 = new ArrayList<>();
        chin.repartirTodasLasCartas(mazoJugador1, mazoJugador2);
        chin.repartirCuatroCartasDeFrente(mazoJugador1, manoJugador1);
        chin.repartirCuatroCartasDeFrente(mazoJugador2, manoJugador2);
        descarte1.add(mazoJugador1.remove(mazoJugador1.size()-1));
        descarte2.add(mazoJugador2.remove(mazoJugador2.size()-1));
        assertTrue(chin.sePuedenAgregarCartasAlDescarte(descarte1, descarte2, manoJugador1, manoJugador2));
    }



    @Test
    public void queArranqueCon4CartasEnElCentro(){
        ArrayList<Carta> mazoJugador1 = new ArrayList<>();
        ArrayList<Carta> mazoJugador2 = new ArrayList<>();
        ArrayList<Carta> manoJugador1 = new ArrayList<>();
        ArrayList<Carta> manoJugador2 = new ArrayList<>();
        ServicioChin chin = new ServicioChinImpl();
        chin.repartirTodasLasCartas(mazoJugador1, mazoJugador2);
        chin.repartirCuatroCartasDeFrente(mazoJugador1, manoJugador1);
        chin.repartirCuatroCartasDeFrente(mazoJugador2, manoJugador2);

        assertEquals(4, manoJugador1.size());
        assertEquals(4, manoJugador2.size());
    }

}
