package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorSenku {

   @RequestMapping("/senku")
    public ModelAndView irPantallaJuego() {
        ModelMap modelo=new ModelMap();
        modelo.put("mensaje","SENKU");

        return new ModelAndView("senkuJuego",modelo);
    }
}
