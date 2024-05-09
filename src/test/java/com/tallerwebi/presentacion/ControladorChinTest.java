package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Carta;
import com.tallerwebi.dominio.ServicioChin;
import com.tallerwebi.infraestructura.ServicioChinImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.when;

public class ControladorChinTest {
    private ControladorChin controladorChin;
    private ServicioChin servicioChin;

    @BeforeEach
    public void init(){
        //<>
        this.servicioChin = new ServicioChinImpl();
        ArrayList<Carta> mazoJugador1 = new ArrayList<Carta>();
        ArrayList<Carta> mazoJugador2 = new ArrayList<Carta>();
        servicioChin.repartirTodasLasCartas(mazoJugador1, mazoJugador2);
        servicioChin.repartirCuatroCartasDeFrente(mazoJugador1, new ArrayList<Carta>());
        servicioChin.repartirCuatroCartasDeFrente(mazoJugador2, new ArrayList<Carta>());
        this.controladorChin = new ControladorChin(servicioChin);
    }
    @Test
    public void queAlSolicitarLaVistaDeChinSeMuestreLaVistaDeChin(){
        //when(this.servicioChin.get()).thenReturn();
        ModelAndView mav = controladorChin.irAlChin();
        String message = mav.getModel().get("message").toString();
        assertThat(mav.getViewName(), equalToIgnoringCase("chin"));
        assertThat(message, equalToIgnoringCase("Bienvenido al chin"));
    }

    @Test
    public void queSeMuestrenLasCartasDelMazoRepartidas(){
        //when(this.servicioChin.repartirCuatroCartasDeFrente(new ArrayList<Carta>(), new ArrayList<Carta>()).thenReturn(4));
        //ModelAndView mav = this.controladorChin;

    }
}
