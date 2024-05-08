package com.tallerwebi.presentacion;


import com.tallerwebi.infraestructura.ServicioSenkuImpl;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

public class ControladorSenkuTest {


    private ControladorSenku controladorSenku;
    private ServicioSenkuImpl servicioSenku;

    @BeforeEach
    public void init() {
        this.servicioSenku = new ServicioSenkuImpl();
        this.controladorSenku= new ControladorSenku(servicioSenku);
    }
@Test
    public void queSeDevuelvaLaVistaInicialSenku() {

        ModelAndView modelAndView = controladorSenku.inicioSenku();
        String viewname = modelAndView.getViewName();

        assertThat(viewname, equalToIgnoringCase("senku-inicio"));
    
    }
  }
