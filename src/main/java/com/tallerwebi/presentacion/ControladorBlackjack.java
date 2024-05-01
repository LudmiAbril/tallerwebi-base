package com.tallerwebi.presentacion;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Carta;
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.Palo;
import com.tallerwebi.dominio.ServicioBlackjack;
import com.tallerwebi.dominio.ServicioPlataforma;

@Controller
public class ControladorBlackjack {
    private ServicioPlataforma servicioPlataforma;
    private ServicioBlackjack servicioBlackjack;

    @Autowired
    public ControladorBlackjack(ServicioBlackjack servicio, ServicioPlataforma servicioPlataforma) {
        this.servicioBlackjack = servicio;
        this.servicioPlataforma = servicioPlataforma;
    }

    @RequestMapping(path = "/blackjack")
    public ModelAndView inicioBlackjack() {

        ModelMap modelo = new ModelMap();
        // aca van los datos que solicita para empezar a jugar
        modelo.put("nuevoJugador", new Jugador());
        return new ModelAndView("inicio-blackjack", modelo);
    }

    @RequestMapping(path = "/comenzar-blackjack", method = RequestMethod.POST)
    public ModelAndView comenzarBlackjack(@ModelAttribute("nuevoJugador") Jugador nuevoJugador,
            HttpServletRequest request) {

        // repensar la partida que se va a guardar con la plataforma
        // logica de guardado de partida y usuario...

        ModelMap model = new ModelMap();
        // se guardaran en la mano del jugador en su partida
        model.put("nombreJugador", nuevoJugador.getNombre());
        model.put("cartas", servicioBlackjack.entregarCartasPrincipales());
        return new ModelAndView("blackjack", model);

    }

    @GetMapping("/pedir-carta")
    @ResponseBody
    public Carta pedirCarta() {
        //guardar en baraja de partida
        return this.servicioBlackjack.pedirCarta();
    }

    @GetMapping("/plantarse")
    @ResponseBody
    public Boolean plantarse() {
        // de la partida va a usar los datos de la baraja del jugador
        //por ahora solo datos hardcodeados
        ArrayList<Carta> cartasJugador = new ArrayList<Carta>();
        cartasJugador.add(new Carta("A", 11, Palo.PICA));
        cartasJugador.add(new Carta("K", 10, Palo.CORAZON));
        cartasJugador.add(new Carta("3", 3, Palo.CORAZON));
        // agregar luego logica con crupier
        return this.servicioBlackjack.Perdio(cartasJugador);
    }

}
