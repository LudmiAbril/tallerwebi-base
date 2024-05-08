package com.tallerwebi.presentacion;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorAccesoJuegos {

    @RequestMapping(path = "/acceso-juegos")
    public ModelAndView accesoJuegos() {

        return new ModelAndView("acceso-juegos");
    }
}
