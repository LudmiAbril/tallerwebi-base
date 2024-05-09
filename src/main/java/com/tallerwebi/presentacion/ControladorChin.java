package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Carta;
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.ServicioChin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorChin {
    private ServicioChin servicioChin;

    public ControladorChin(ServicioChin servicioChin){

        this.servicioChin = servicioChin;

    }

    @RequestMapping(path = "/inicio-chin")
    public ModelAndView inicioChin() {

        ModelMap modelo = new ModelMap();
        modelo.put("nuevoJugador", new Jugador());
        return new ModelAndView("inicio-chin", modelo);
    }

    public ModelAndView irAlChin() {
        String viewName = "chin";

        ModelMap model = new ModelMap();
        model.put("message", "Bienvenido al Chin");

        return new ModelAndView(viewName, model);
    }
    //public ModelAndView repartirCartas(){

    //}

    @RequestMapping(path = "/chin", method = RequestMethod.POST)
    public ModelAndView comenzarChin(@ModelAttribute("nuevoJugador") Jugador nuevoJugador,
                                          HttpSession session) {

        // establesco lo valores de los masos
        //List<Carta> cartasJugador = servicioChin.//entregarCartasPrincipales();
        //List<Carta> cartasCasa = servicioChin.//entregarCartasPrincipales();

        // guardo el nombre del jugador, los masos y los estados en la sesion
        //session.setAttribute("jugadorActual", nuevoJugador.getNombre());
        //session.setAttribute("cartasJugador", cartasJugador);
        //session.setAttribute("cartasCasa", cartasCasa);
        //session.setAttribute("estadoPartida", servicioBlackjack.estadoPartida(cartasJugador, cartasCasa, false));
        //session.setAttribute("ganador",
        //        servicioBlackjack.ganador(cartasJugador, cartasCasa, nuevoJugador.getNombre(), false));

        // si estado partida "finalizado" llamo al servicioplataforma y guardo en la
        // base
        return new ModelAndView("chin");

    }
}
