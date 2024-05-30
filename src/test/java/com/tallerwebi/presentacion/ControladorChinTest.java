package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.infraestructura.ServicioChinImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorChinTest {
    private ControladorChin controladorChin;
    private ServicioChin servicioChinMock;
    private HttpSession session;
    private ServicioPlataforma servicioPlataforma;

    @BeforeEach
    public void init(){
        //<>
        this.servicioPlataforma = servicioPlataforma;
        this.servicioChinMock = mock(ServicioChin.class);
        this.controladorChin = new ControladorChin(servicioChinMock, servicioPlataforma);
        this.session = new MockHttpSession();

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
    public void queAlSolicitarIrAlCHINSeGuardeElModeloCorrespondiente() {
        ModelAndView mav = this.controladorChin.irAlChin();
        Usuario MODELO_ACTUAL = ((Usuario) mav.getModel().get("nuevoJugador"));
        assertThat(MODELO_ACTUAL, instanceOf(Usuario.class));
    }
    @Test
    public void queSeMuestrenLasCartasDelMazoRepartidas(){
        //when(this.servicioChin.repartirCuatroCartasDeFrente(new ArrayList<Carta>(), new ArrayList<Carta>()).thenReturn(4));
        //ModelAndView mav = this.controladorChin;

    }
    @Test
    public void queSeGuardeLaCantidadDeChinsEnLaPartida(){
        Jugador jugadorMock = mock(Jugador.class);
        when(jugadorMock.getNombre()).thenReturn("Axel");
        List<Integer> puntajesDePartidas = new ArrayList<>();
        Integer cantidadChins =0;
        //when(servicioChinMock.entregarNumeroAleatorio(numerosEntregados)).thenReturn(numeroAleatorio);
        controladorChin.comenzarJuegoChin(jugadorMock, session);
        //controlador.ch
        //assertThat(session.getAttribute("cantidadDeChinsEnPartida"), equalTo(cantidadChins));
    }
}
