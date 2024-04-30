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

    }
    @Test
    public void elJugadorGanaSiNoTieneMasCartas(){

    }
}
