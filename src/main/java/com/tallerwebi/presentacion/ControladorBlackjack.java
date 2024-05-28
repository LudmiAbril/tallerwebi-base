package com.tallerwebi.presentacion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Carta;
import com.tallerwebi.dominio.EstadoPartida;
import com.tallerwebi.dominio.Juego;
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.Partida;
import com.tallerwebi.dominio.ServicioBlackjack;
import com.tallerwebi.dominio.ServicioPlataforma;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.PartidaDeUsuarioNoEncontradaException;

@Controller
public class ControladorBlackjack {
    private ServicioPlataforma servicioPlataforma;
    private ServicioBlackjack servicioBlackjack;

    @Autowired
    public ControladorBlackjack(ServicioBlackjack servicioBlackjack, ServicioPlataforma servicioPlataforma) {
        this.servicioBlackjack = servicioBlackjack;
        this.servicioPlataforma = servicioPlataforma;
    }

    @RequestMapping(path = "/inicio-blackjack")
    public ModelAndView inicioBlackjack(HttpSession session) {
        ModelMap modelo = new ModelMap();
        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
        modelo.put("tiempoDefault", jugador.getConfig().getDuracionBlackjack());
        return new ModelAndView("irAlBlackjack", modelo);
    }

    @RequestMapping(path = "/blackjack", method = RequestMethod.POST)
    public ModelAndView comenzarBlackjack(HttpSession session,
            @RequestParam(value = "contrareloj", defaultValue = "false") Boolean contrareloj,
            @RequestParam(value = "tiempoLimite", required = false) Integer tiempoLimiteMinutos) {

        ModelMap model = new ModelMap();

        // establesco lo valores de los masos y el puntaje inicial
        List<Carta> cartasJugador = servicioBlackjack.entregarCartasPrincipales();
        List<Carta> cartasCasa = servicioBlackjack.entregarCartasPrincipales();
        Integer puntajeInicial = servicioBlackjack.calcularPuntuacion(cartasJugador);
        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
        String nombreJugador = jugador.getNombre();
        List<Partida> partidasAnteriores = new ArrayList<Partida>();

        if (contrareloj) {
            // calcula la hora exacta final, la formateo y la paso
            long tiempoLimiteMilisegundos = tiempoLimiteMinutos * 60 * 1000;
            long tiempoExpiracion = System.currentTimeMillis() + tiempoLimiteMilisegundos;
            Date fechaExpiracion = new Date(tiempoExpiracion);
            SimpleDateFormat formato = new SimpleDateFormat("HH:mm");
            String tiempoExpiracionFormateado = formato.format(fechaExpiracion);

            session.setAttribute("contrareloj", true);
            session.setAttribute("tiempoLimite", tiempoExpiracionFormateado);
            session.setAttribute("minutos", tiempoLimiteMinutos);
        } else {
            session.setAttribute("contrareloj", false);
        }

        try {
            partidasAnteriores = servicioPlataforma.obtenerUltimasPartidasDelUsuario(jugador.getId(),
                    Juego.BLACKJACK);
        } catch (PartidaDeUsuarioNoEncontradaException e) {
            model.addAttribute("mensajePartidas", "aun no hay partidas registradas.");
        }
        // guardo el nombre del jugador, los masos y los estados en la sesion
        session.setAttribute("nombre", nombreJugador);
        session.setAttribute("partidas", partidasAnteriores);
        session.setAttribute("cartasJugador", cartasJugador);
        session.setAttribute("cartasCasa", cartasCasa);
        session.setAttribute("puntaje", puntajeInicial);
        session.setAttribute("estadoPartida", servicioBlackjack.estadoPartida(cartasJugador, cartasCasa, false));
        session.setAttribute("ganador",
                servicioBlackjack.ganador(cartasJugador, cartasCasa, nombreJugador, false));

        // si estado partida "finalizado" llamo al servicioplataforma y guardo en la
        // base
        return new ModelAndView("blackjack", model);

    }

    @RequestMapping(path = "/comenzar", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> comenzarJuego(HttpSession session) {
        // recupero los datos de la sesion
        List<Carta> cartasJugador = (List<Carta>) session.getAttribute("cartasJugador");
        List<Carta> cartasCasa = (List<Carta>) session.getAttribute("cartasCasa");
        String nombre = (String) session.getAttribute("nombre");
        EstadoPartida estado = (EstadoPartida) session.getAttribute("estadoPartida");
        String ganador = (String) session.getAttribute("ganador");
        Integer puntaje = (Integer) session.getAttribute("puntaje");
        List<Partida> partidasAnteriores = (List<Partida>) session.getAttribute("partidas");
        Boolean contrareloj = (Boolean) session.getAttribute("contrareloj");

        // Creo la respuesta con las cartas del jugador y del crupier para pasarle al js
        Map<String, Object> response = new HashMap<>();
        if (contrareloj) {
            response.put("contrareloj", true);
            response.put("tiempoLimite", session.getAttribute("tiempoLimite"));
        } else {
            response.put("contrareloj", false);
        }
        response.put("cartasJugador", cartasJugador);
        response.put("partidas", partidasAnteriores);
        response.put("cartasCasa", cartasCasa);
        response.put("jugadorActual", nombre);
        response.put("estadoPartida", estado);
        response.put("puntaje", puntaje);
        response.put("ganador", ganador);
        return response;
    }

    @RequestMapping(path = "/pedir-carta", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> pedirCarta(HttpSession session) {
        // recupero los masos de la sesion
        List<Carta> cartasJugadorActualizadas = new ArrayList<>((List<Carta>) session.getAttribute("cartasJugador"));
        List<Carta> cartasCasaActualizadas = new ArrayList<>((List<Carta>) session.getAttribute("cartasCasa"));
        // pida la carta nueva
        Carta cartaNueva = servicioBlackjack.pedirCarta();
        // Agregar la carta al mazo del jugador
        cartasJugadorActualizadas.add(cartaNueva);
        // obtener el nombre
        String nombreJugador = (String) session.getAttribute("nombre");

        // calculo el resto
        EstadoPartida nuevoEstado = servicioBlackjack.estadoPartida(cartasJugadorActualizadas, cartasCasaActualizadas,
                false);
        String ganadorActualizado = servicioBlackjack.ganador(cartasJugadorActualizadas, cartasCasaActualizadas,
                nombreJugador, false);
        Integer puntajeActualizado = servicioBlackjack.calcularPuntuacion(cartasJugadorActualizadas);

        // Actualizar la sesión con los nuevos datos
        session.setAttribute("cartasJugador", cartasJugadorActualizadas);
        session.setAttribute("estadoPartida", nuevoEstado);
        session.setAttribute("ganador", ganadorActualizado);
        session.setAttribute("puntaje", puntajeActualizado);

        // creo el map con los datos nuevos para recuperar en el js
        Map<String, Object> response = new HashMap<>();
        response.put("cartaNueva", cartaNueva);
        response.put("estadoPartida", nuevoEstado);
        response.put("ganador", ganadorActualizado);
        response.put("puntaje", puntajeActualizado);
        response.put("jugadorActual", nombreJugador);

        return response;

    }

    @GetMapping("/plantarse")
    @ResponseBody
    @Transactional
    public Map<String, Object> plantarse(HttpSession session) {
        // recupero los datos de la sesion
        List<Carta> cartasJugador = new ArrayList<>((List<Carta>) session.getAttribute("cartasJugador"));
        List<Carta> cartasCasaActualizadas = new ArrayList<>((List<Carta>) session.getAttribute("cartasCasa"));
        String jugador = (String) session.getAttribute("nombre");
        List<Carta> cartasNuevasCrupier = servicioBlackjack.plantarse(cartasCasaActualizadas);

        // actualizo al crupier
        cartasCasaActualizadas.addAll(cartasNuevasCrupier);
        // actualizo el estado y ganador
        String ganador = servicioBlackjack.ganador(cartasJugador, cartasCasaActualizadas, jugador, true);

        // guardo en el map
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("manoFinalCrupier", cartasNuevasCrupier);
        response.put("ganador", ganador);
        response.put("jugadorActual", jugador);
        response.put("estadoPartida", servicioBlackjack.estadoPartida(cartasJugador, cartasCasaActualizadas, true));

        return response;
    }

    @RequestMapping("/finalizar")
    public ModelAndView finalizar(HttpSession session) {
        // guardo la partida con todo lo que hay en la session y luego la limpio
        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
        Integer puntajeFinal = (Integer) session.getAttribute("puntaje");

        servicioPlataforma.agregarPartida(new Partida(jugador.getId(), Juego.BLACKJACK));

        return new ModelAndView("redirect:/inicio-blackjack");
    }

    @RequestMapping(path = "/reiniciar")
    public ModelAndView reiniciar(HttpSession session) {
        // reinicio los datos de la session
        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
        Integer puntajeFinal = (Integer) session.getAttribute("puntaje");

        servicioPlataforma.agregarPartida(new Partida(jugador.getId(),
                Juego.BLACKJACK));

        List<Carta> cartasJugador = servicioBlackjack.entregarCartasPrincipales();
        List<Carta> cartasCasa = servicioBlackjack.entregarCartasPrincipales();
        Integer puntajeInicial = servicioBlackjack.calcularPuntuacion(cartasJugador);
        List<Partida> partidas = new ArrayList<Partida>();
        Boolean contrareloj = (Boolean) session.getAttribute("contrareloj");
        ModelMap model = new ModelMap();

        if (contrareloj) {
            Integer tiempoLimiteMinutos = (Integer) session.getAttribute("minutos");
            long tiempoLimiteMilisegundos = tiempoLimiteMinutos * 60 * 1000;
            long tiempoExpiracion = System.currentTimeMillis() + tiempoLimiteMilisegundos;
            Date fechaExpiracion = new Date(tiempoExpiracion);
            SimpleDateFormat formato = new SimpleDateFormat("HH:mm");
            String tiempoExpiracionFormateado = formato.format(fechaExpiracion);
            session.setAttribute("tiempoLimite", tiempoExpiracionFormateado);

        }

        try {
            partidas = servicioPlataforma.obtenerPartidasUsuario(jugador.getId(), Juego.BLACKJACK);
            session.setAttribute("partidas", partidas);

        } catch (PartidaDeUsuarioNoEncontradaException e) {
            model.addAttribute("mensajePartidas", "aun no hay partidas registradas");
        }

        session.setAttribute("cartasJugador", cartasJugador);
        session.setAttribute("cartasCasa", cartasCasa);
        session.setAttribute("puntaje", puntajeInicial);
        session.setAttribute("estadoPartida", servicioBlackjack.estadoPartida(cartasJugador, cartasCasa, false));
        session.setAttribute("ganador",
                servicioBlackjack.ganador(cartasJugador, cartasCasa, jugador.getNombre(), false));
        return new ModelAndView("blackjack", model);
    }

    @RequestMapping(path = "/reiniciar-blackjack", method = RequestMethod.GET)
    public ModelAndView mostrarBlackjack() {
        return new ModelAndView("blackjack");
    }

}
