package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Carta;
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.ServicioBingo;
import com.tallerwebi.dominio.ServicioChin;
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
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorChinTest {
    private ControladorChin controladorChin;
    private ServicioChin servicioChinMock;
    private HttpSession session;

    @BeforeEach
    public void init(){
        //<>
        this.servicioChinMock = mock(ServicioChin.class);
        this.controladorChin = new ControladorChin(servicioChinMock);
        this.session = new MockHttpSession();
//        ArrayList<Carta> mazoJugador1 = new ArrayList<Carta>();
//        ArrayList<Carta> mazoJugador2 = new ArrayList<Carta>();
//        servicioChin.repartirTodasLasCartas(mazoJugador1, mazoJugador2);
//        servicioChin.repartirCuatroCartasDeFrente(mazoJugador1, new ArrayList<Carta>());
//        servicioChin.repartirCuatroCartasDeFrente(mazoJugador2, new ArrayList<Carta>());

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
        Jugador MODELO_ACTUAL = ((Jugador) mav.getModel().get("nuevoJugador"));
        assertThat(MODELO_ACTUAL, instanceOf(Jugador.class));
    }
    @Test
    public void queSeMuestrenLasCartasDelMazoRepartidas(){
        //when(this.servicioChin.repartirCuatroCartasDeFrente(new ArrayList<Carta>(), new ArrayList<Carta>()).thenReturn(4));
        //ModelAndView mav = this.controladorChin;

    }
    @Test
    public void queSeGuardeLaCantidadDeChinsEnLaPartida(){
        //        Set<Integer> numerosEntregados = new LinkedHashSet<>();
        //        Integer numeroAleatorio = 10;
        //
        //        when(servicioBingoMock.entregarNumeroAleatorio(numerosEntregados)).thenReturn(numeroAleatorio);
        //
        //        controladorBingo.comenzarJuegoBingo(jugadorMock, session);
        //
        //        assertThat(session.getAttribute("numeroAleatorioCantado"), equalTo(numeroAleatorio));
        Jugador jugadorMock = mock(Jugador.class);
        when(jugadorMock.getNombre()).thenReturn("Axel");
        List<Integer> puntajesDePartidas = new ArrayList<>();
        //when(servicioChinMock.entregarNumeroAleatorio(numerosEntregados)).thenReturn(numeroAleatorio);
        controladorChin.comenzarJuegoChin(jugadorMock, session);
        //assertThat(session.getAttribute("cantidadDeChinsEnPartida"), equalTo(numeroAleatorio));
    }
}
