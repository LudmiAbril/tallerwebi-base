package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ControladorChin {
    private ServicioChin servicioChin;
    private ServicioPlataforma servicioPlataforma;
    public ControladorChin(ServicioChin servicioChin, ServicioPlataforma servicioPlataforma){
        this.servicioPlataforma = servicioPlataforma;
        this.servicioChin = servicioChin;

    }

    @RequestMapping(path = "/inicio-chin")
    public ModelAndView inicioChin(HttpSession session) {

        ModelMap modelo = new ModelMap();
        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
        modelo.put("nuevoJugador", new Usuario());
        if(jugador.getConfig()==null){
            jugador.setConfig(new ConfiguracionesJuego());
        }
        return new ModelAndView("irAlChin", modelo);
    }

    public ModelAndView irAlChin() {
        String viewName = "chin";
        ModelMap model = new ModelMap();
        model.put("message", "Bienvenido al Chin");
        model.put("nuevoJugador", new Usuario());
        return new ModelAndView(viewName, model);
    }


    @RequestMapping(path = "/comenzarJuegoChin", method = RequestMethod.POST)
    public ModelAndView comenzarJuegoChin(@ModelAttribute("nuevoJugador") Usuario nuevoJugador, HttpSession session) {
        ArrayList<Carta> mazo1 = new ArrayList<>();
        ArrayList<Carta> mazo2 = new ArrayList<>();
        servicioChin.repartirTodasLasCartas(mazo1, mazo2);
        ArrayList<Carta> manoJugador1 = new ArrayList<>();
        ArrayList<Carta> manoJugador2 = new ArrayList<>();
        servicioChin.repartirCuatroCartasDeFrente(mazo1, manoJugador1);
        servicioChin.repartirCuatroCartasDeFrente(mazo2, manoJugador2);
        ArrayList<Carta> descarte1 = new ArrayList<>();
        ArrayList<Carta> descarte2 = new ArrayList<>();
        session.setAttribute("mazoJugador1", mazo1);
        session.setAttribute("mazoJugador2", mazo2);
        session.setAttribute("manoJugador1", manoJugador1);
        session.setAttribute("manoJugador2", manoJugador2);
        session.setAttribute("descarte1", descarte1);
        session.setAttribute("descarte2", descarte2);
        ModelMap model = new ModelMap();
        String nombreJugador = nuevoJugador.getNombre();
        model.put("nombreJugador", nombreJugador);
        session.setAttribute("nombreJugador", nombreJugador);
        return new ModelAndView("chin", model);
    }
}
