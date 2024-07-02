package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioMayorMenor;
import com.tallerwebi.dominio.ServicioPlataforma;
import com.tallerwebi.dominio.Usuario;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

public class ControladorMayorMenor {
    private ServicioMayorMenor servicioMayorMenor;
    private ServicioPlataforma servicioPlataforma;

    public ControladorMayorMenor(ServicioMayorMenor servicioMayorMenorMock, ServicioPlataforma servicioPlataformaMock) {
        this.servicioMayorMenor = servicioMayorMenorMock;
        this.servicioPlataforma = servicioPlataformaMock;
    }

    @RequestMapping(path = "/inicio-MayorMenor")
    public ModelAndView inicioMayorMenor(HttpSession session) {
        ModelMap modelo = new ModelMap();
        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
        return new ModelAndView("irAMayorMenor", modelo);
    }

    @RequestMapping(path = "/Comenzar-MayorMenor")
    public ModelAndView comenzarMayorMenor(HttpSession session, int i) {
        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");

        return new ModelAndView("MayorMenor");
    }
}
