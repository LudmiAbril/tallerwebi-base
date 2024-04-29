package com.tallerwebi.dominio;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ChinTest {
    @Test
    public void hayChin(){
        Carta carta1 = new Carta("A", 1, Palo.DIAMANTE);
        Carta carta2 = new Carta("A", 1, Palo.CORAZON);

        ArrayList<Carta> descarte1 = new ArrayList<>();
        ArrayList<Carta> descarte2 = new ArrayList<>();

        descarte1.add(carta1);
        descarte2.add(carta2);

        //assertEquals();
    }
}
