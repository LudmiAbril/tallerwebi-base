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
        //modelo.put("tiempoDefault", jugador.getConfig().getDuracionChin());
        return new ModelAndView("irAlChin", modelo);
    }

    public ModelAndView irAlChin() {
        String viewName = "chin";
        ModelMap model = new ModelMap();
        model.put("message", "Bienvenido al Chin");
        model.put("nuevoJugador", new Usuario());
        return new ModelAndView(viewName, model);
    }
    //public ModelAndView repartirCartas(){

    //}

//    @RequestMapping(path = "/chin", method = RequestMethod.POST)
//    public ModelAndView comenzarChin(@ModelAttribute("nuevoJugador") Usuario nuevoJugador,
//                                          HttpSession session) {
//
//        ModelMap model = new ModelMap();
//        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
//        String nombreJugador = jugador.getNombre();
//        ArrayList<Carta> mazo1 = new ArrayList<Carta> ();
//        ArrayList<Carta> mazo2 = new ArrayList<Carta> ();
//        servicioChin.repartirTodasLasCartas(mazo1, mazo2);
//        List<Carta> mano1;
//        servicioChin.entregarCartasPrincipales();
//        //List<Carta> mano2; = servicioChin.//entregarCartasPrincipales();
//
//        session.setAttribute("jugadorActual", nuevoJugador.getNombre());
//        session.setAttribute("cartasJugador", cartasJugador);
//        session.setAttribute("cartasCasa", cartasCasa);
//        session.setAttribute("estadoPartida", servicioBlackjack.estadoPartida(cartasJugador, cartasCasa, false));
//        session.setAttribute("ganador",
//                servicioBlackjack.ganador(cartasJugador, cartasCasa, nuevoJugador.getNombre(), false));
//
//        // si estado partida "finalizado" llamo al servicioplataforma y guardo en la
//        // base
//        return new ModelAndView("chin");
//
//    }
    @RequestMapping(path = "/comenzarJuegoChin", method = RequestMethod.POST)
    public ModelAndView comenzarJuegoChin(@ModelAttribute("nuevoJugador") Jugador nuevoJugador, HttpSession session) {
        //CartonBingo carton = servicioChin.generarCarton();
        Set<Integer> numerosEntregados = new LinkedHashSet<Integer>();
        //Integer numeroNuevo = this.servicioChin.entregarNumeroAleatorio(numerosEntregados);
        //Integer numeroCantadoAleatorio = numeroNuevo;
        //numerosEntregados.add(numeroNuevo);
        //session.setAttribute("carton", carton);
        session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);
        //session.setAttribute("numeroAleatorioCantado", numeroCantadoAleatorio);
        ModelMap model = new ModelMap();
        String nombreJugador = nuevoJugador.getNombre();
        model.put("nombreJugador", nombreJugador);
        session.setAttribute("nombreJugador", nombreJugador);
        return new ModelAndView("chin", model);
    }
}
