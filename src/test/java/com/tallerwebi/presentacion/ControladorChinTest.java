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
import static org.hamcrest.Matchers.*;
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

        this.servicioPlataforma = servicioPlataforma;
        this.servicioChinMock = mock(ServicioChin.class);
        this.controladorChin = new ControladorChin(servicioChinMock, servicioPlataforma);
        this.session = new MockHttpSession();

    }
    @Test
    public void queAlSolicitarLaVistaDeChinSeMuestreLaVistaDeChin(){
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
    public void queElNombreDelUsuarioNoPuedaSerNulo(){
        ModelAndView mav = this.controladorChin.irAlChin();
        Usuario MODELO_ACTUAL = ((Usuario) mav.getModel().get("nuevoJugador"));
        assertThat(MODELO_ACTUAL, is(notNullValue()));
    }
    @Test
    public void queElMazoyManodelJugador1NoSeanNulos(){
        Usuario jugadorMock = mock(Usuario.class);
        when(jugadorMock.getNombre()).thenReturn("Axel");
        controladorChin.comenzarJuegoChin(jugadorMock, session);

        assertThat(session.getAttribute("mazoJugador1"), is(notNullValue()));
        assertThat(session.getAttribute("manoJugador1"), is(notNullValue()));
    }
    @Test
    public void queElMazoyManodelJugador2NoSeanNulos(){
        Usuario jugadorMock = mock(Usuario.class);
        when(jugadorMock.getNombre()).thenReturn("Axel");
        controladorChin.comenzarJuegoChin(jugadorMock, session);

        assertThat(session.getAttribute("mazoJugador2"), is(notNullValue()));
        assertThat(session.getAttribute("manoJugador2"), is(notNullValue()));
    }
    @Test
    public void queElMazoDeDescarteInicieSiempreVacio(){
        Usuario jugadorMock = mock(Usuario.class);
        when(jugadorMock.getNombre()).thenReturn("Axel");
        controladorChin.comenzarJuegoChin(jugadorMock, session);

        assertThat(((ArrayList<Carta>) session.getAttribute("descarte1")).size(), is(0));
        assertThat(((ArrayList<Carta>) session.getAttribute("descarte2")).size(), is(0));
    }

}
