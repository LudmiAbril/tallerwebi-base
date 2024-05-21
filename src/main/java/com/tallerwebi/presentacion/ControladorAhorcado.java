package com.tallerwebi.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.ServicioAhorcado;

@Controller
public class ControladorAhorcado {

    private ServicioAhorcado servicioAhorcado;

    @Autowired
    public ControladorAhorcado(ServicioAhorcado servicio) {
        this.servicioAhorcado = servicio;
    }

  @RequestMapping(path = "/irAlAhorcado", method = RequestMethod.GET)
    public ModelAndView irAlAhorcado(@SessionAttribute("jugador") Jugador jugador) {
        ModelMap model = new ModelMap();
        model.put("jugador", jugador);
        return new ModelAndView("ahorcado", model);
    }
}
