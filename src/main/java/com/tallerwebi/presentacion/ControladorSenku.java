package com.tallerwebi.presentacion;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.CartonBingo;
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.Senku;
import com.tallerwebi.dominio.ServicioSenku;
import com.tallerwebi.dominio.Tablero;

@Controller
public class ControladorSenku {

    private ServicioSenku servicioSenku;

    @Autowired
    public ControladorSenku(ServicioSenku servicioSenku) {
        this.servicioSenku = servicioSenku;
    }

    @RequestMapping(path = "/senku-inicio", method = RequestMethod.GET)
    public ModelAndView inicioSenku() {
        ModelMap modelo = new ModelMap();
        modelo.put("nuevoJugador", new Jugador());
        return new ModelAndView("senku-inicio", modelo);
    }

    @RequestMapping(path = "/comenzarJuegoSenku", method = RequestMethod.POST)
    public ModelAndView comenzarJuegoSenku(@ModelAttribute("nuevoJugador") Jugador nuevoJugador, HttpSession session) {
        Senku senku = new Senku(5);
        Tablero tablero = senku.getTablero();
        // GUARDANDO DATOS DE SESION
        session.setAttribute("senku", senku);
        session.setAttribute("tablero", tablero);
        return new ModelAndView("senku");
    }

    @RequestMapping(path = "/obtenerTablero", method = RequestMethod.GET)
    @ResponseBody
    public Tablero obtenerTablero(HttpSession session) {
        Senku senku = (Senku) session.getAttribute("senku");
        return senku.getTablero();
    }

}
