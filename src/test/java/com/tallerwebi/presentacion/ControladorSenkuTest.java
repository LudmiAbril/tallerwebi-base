package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ControladorSenkuTest {

    @Test
    public void queAlSolicitarLaPantallaDelSenkuSeMuestreLaPantallaDelSenku(){
        // preparacion
     ControladorSenku controladorSenku= new ControladorSenku();

        // ejecucion
        ModelAndView mav = controladorSenku.irPantallaJuego();
        String mensaje= mav.getModel().get("mensaje").toString();

        // validacion
       assertThat(mav.getViewName(),equalToIgnoringCase("senkuJuego"));
       assertThat(mensaje,equalToIgnoringCase("SENKU"));
    }
}
