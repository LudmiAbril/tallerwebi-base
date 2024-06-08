package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.ServicioAhorcado;

import javax.servlet.http.HttpSession;

@Controller
public class ControladorAhorcado {

    private ServicioAhorcado servicioAhorcado;

    @Autowired
    public ControladorAhorcado(ServicioAhorcado servicio) {
        this.servicioAhorcado = servicio;
    }

    @RequestMapping(path = "/irAlAhorcado", method = RequestMethod.GET)
    public ModelAndView irAlAhorcado() {
        ModelMap model = new ModelMap();
        model.put("nuevoJugador", new Usuario());
        return new ModelAndView("irAlAhorcado", model);
    }

    @RequestMapping(path = "/ahorcadoJuego", method = RequestMethod.GET)
    public ModelAndView ahorcadoJuego() {
        ModelMap model = new ModelMap();
        try {
            model.put("jugador", new Jugador());
            String palabra = servicioAhorcado.entregarPalabra();
            model.put("palabra", palabra);
            int partesAhorcado = 6; // Número de partes del muñeco del ahorcado
            model.put("partesAhorcado", partesAhorcado);
            return new ModelAndView("ahorcado", model);
        } catch (Exception e) {
            model.put("mensajeError", "No se pudo inicializar el jugador.");
            return new ModelAndView("error", model);
        }
    }
}
