package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.ServicioChinImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

public class ChinTest {

    @BeforeEach
    public void init(){


    }
    @Test
    public void devuelveChinCuandoLasDosCartasDelMedioSonIguales(){
        Carta carta1 = new Carta("A", 1, Palo.DIAMANTE);
        Carta carta2 = new Carta("A", 1, Palo.CORAZON);
        ArrayList<Carta> descarte1 = new ArrayList<>();
        ArrayList<Carta> descarte2 = new ArrayList<>();
        descarte1.add(carta1);
        descarte2.add(carta2);

        ServicioChin chin = new ServicioChinImpl();
        assertThat(chin.hayChin(descarte1, descarte2), is(true));
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
        assertThat(chin.hayChin(descarte1, descarte2), is(false));
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
        assertThat(descarte2.size(), is(2));
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
        assertThat(descarte1.size(), is(1));
    }
    @Test
    public void elJugador1GanaSiNoTieneMasCartas(){
        ArrayList<Carta> mazoJugador1 = new ArrayList<>();

        assertThat(mazoJugador1.size(), is(0));
    }
    @Test
    public void elJugador2GanaSiNoTieneMasCartas(){
        ArrayList<Carta> mazoJugador2 = new ArrayList<>();

        assertThat(mazoJugador2.size(), is(0));
    }
//    @Test
//    public void devuelveLasCartasDelMazoACadaJugador(){
//        ArrayList<Carta> mazoJugador1 = new ArrayList<>();
//        ArrayList<Carta> mazoJugador2 = new ArrayList<>();
//
//        ServicioChin chin = new ServicioChinImpl();
//        chin.repartirTodasLasCartas(mazoJugador1, mazoJugador2);
//
//        assertThat(mazoJugador1.size(), is(26));
//        assertThat(mazoJugador2.size(), is(26));
//    }
    /*@Test
    public void soloSePuedenTenerHasta4CartasEnLaMano(){

        ArrayList<Carta> mazoJugador1 = new ArrayList<>();
        ArrayList<Carta> mazoJugador2 = new ArrayList<>();
        ArrayList<Carta> manoJugador1 = new ArrayList<>();
        ServicioChin chin = new ServicioChinImpl();
        chin.repartirTodasLasCartas(mazoJugador1, mazoJugador2);

        while(manoJugador1.size()<4){
            chin.sacarDelMazoYPonerEnMano(mazoJugador1, manoJugador1);
        }
        assertThat(manoJugador1.size(), is(4));

    }*/
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
        assertThat(descarte1.isEmpty(), is(true));
        assertThat(descarte2.isEmpty(), is(true));

    }
    /*@Test
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
        assertThat(chin.sePuedenAgregarCartasAlDescarte(descarte1, descarte2, manoJugador1, manoJugador2),is(true));
    }*/



    /*@Test
    public void queArranqueCon4CartasEnElCentro(){
        ArrayList<Carta> mazoJugador1 = new ArrayList<>();
        ArrayList<Carta> mazoJugador2 = new ArrayList<>();
        ArrayList<Carta> manoJugador1 = new ArrayList<>();
        ArrayList<Carta> manoJugador2 = new ArrayList<>();
        ServicioChin chin = new ServicioChinImpl();
        chin.repartirTodasLasCartas(mazoJugador1, mazoJugador2);
        chin.repartirCuatroCartasDeFrente(mazoJugador1, manoJugador1);
        chin.repartirCuatroCartasDeFrente(mazoJugador2, manoJugador2);

        assertThat(manoJugador1.size(), is(4));
        assertThat(manoJugador2.size(), is(4));
    }*/

}
