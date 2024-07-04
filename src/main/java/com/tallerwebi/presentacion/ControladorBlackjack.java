package com.tallerwebi.presentacion;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.BingoBotEsNullException;
import com.tallerwebi.dominio.excepcion.PartidaConPuntajeNegativoException;

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
            @RequestParam(value = "contrareloj", defaultValue = "false") boolean contrareloj,
            @RequestParam(value = "tiempoLimite", required = false) Integer tiempoLimiteMinutos) {

        ModelMap model = new ModelMap();
        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
        String nombreJugador = jugador.getNombre();
        Integer valorAs = jugador.getConfig().getValorDelAs();
        servicioBlackjack.inicializarBaraja(valorAs);
        List<Carta> cartasJugador = servicioBlackjack.entregarCartasPrincipales();
        List<Carta> cartasCasa = servicioBlackjack.entregarCartasPrincipales();
        Integer puntajeInicial = servicioBlackjack.calcularPuntuacion(cartasJugador);
        List<Partida> partidasAnteriores = new ArrayList<Partida>();

        if (contrareloj) {
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

        session.setAttribute("nombre", nombreJugador);
        session.setAttribute("partidas", partidasAnteriores);
        session.setAttribute("cartasJugador", cartasJugador);
        session.setAttribute("cartasCasa", cartasCasa);
        session.setAttribute("puntaje", puntajeInicial);
        session.setAttribute("modoDificil", false);
        session.setAttribute("estadoPartida", servicioBlackjack.estadoPartida(cartasJugador, cartasCasa, false));
        session.setAttribute("ganador",
                servicioBlackjack.ganador(cartasJugador, cartasCasa, nombreJugador, false));


        return new ModelAndView("blackjack", model);

    }

    @RequestMapping(path = "/blackjackDificil", method = RequestMethod.GET)
    public ModelAndView comenzarBlackjackModoDificil(HttpSession session) {

        ModelMap model = new ModelMap();

        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
        String nombreJugador = jugador.getNombre();
        Integer valorAs = 1;
        servicioBlackjack.inicializarBaraja(valorAs);

        List<Carta> cartasJugador = servicioBlackjack.entregarCartasPrincipales();
        List<Carta> cartasCasa = servicioBlackjack.entregarManoDeVeinte();
        Integer puntajeInicial = servicioBlackjack.calcularPuntuacion(cartasJugador);
        List<Partida> partidasAnteriores = new ArrayList<Partida>();


        long tiempoLimiteMilisegundos = 1 * 60 * 1000;
        long tiempoExpiracion = System.currentTimeMillis() + tiempoLimiteMilisegundos;
        Date fechaExpiracion = new Date(tiempoExpiracion);
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm");
        String tiempoExpiracionFormateado = formato.format(fechaExpiracion);

        try {
            partidasAnteriores = servicioPlataforma.obtenerUltimasPartidasDelUsuario(jugador.getId(),
                    Juego.BLACKJACK);
        } catch (PartidaDeUsuarioNoEncontradaException e) {
            model.addAttribute("mensajePartidas", "aun no hay partidas registradas.");
        }

        session.setAttribute("contrareloj", true);
        session.setAttribute("tiempoLimite", tiempoExpiracionFormateado);
        session.setAttribute("minutos", 1);
        session.setAttribute("nombre", nombreJugador);
        session.setAttribute("partidas", partidasAnteriores);
        session.setAttribute("cartasJugador", cartasJugador);
        session.setAttribute("cartasCasa", cartasCasa);
        session.setAttribute("modoDificil", true);
        session.setAttribute("puntaje", puntajeInicial);
        session.setAttribute("valorDelAs", valorAs);
        session.setAttribute("estadoPartida", servicioBlackjack.estadoPartida(cartasJugador, cartasCasa, false));
        session.setAttribute("ganador",
                servicioBlackjack.ganador(cartasJugador, cartasCasa, nombreJugador, false));
        model.addAttribute("tituloMensaje", "BlackjackLengendary");
        return new ModelAndView("blackjack", model);

    }

    @RequestMapping(path = "/comenzar", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> comenzarJuego(HttpSession session) {

        List<Carta> cartasJugador = (List<Carta>) session.getAttribute("cartasJugador");
        List<Carta> cartasCasa = (List<Carta>) session.getAttribute("cartasCasa");
        String nombre = (String) session.getAttribute("nombre");
        EstadoPartida estado = (EstadoPartida) session.getAttribute("estadoPartida");
        String ganador = (String) session.getAttribute("ganador");
        Integer puntaje = (Integer) session.getAttribute("puntaje");
        List<Partida> partidasAnteriores = (List<Partida>) session.getAttribute("partidas");
        Boolean contrareloj = (Boolean) session.getAttribute("contrareloj");
        Boolean modoDificil = (Boolean) session.getAttribute("modoDificil");


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
        response.put("modoDificil", modoDificil);


        return response;
    }

    @RequestMapping(path = "/pedir-carta", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> pedirCarta(HttpSession session) {

        List<Carta> cartasJugadorActualizadas = new ArrayList<>((List<Carta>) session.getAttribute("cartasJugador"));
        List<Carta> cartasCasaActualizadas = new ArrayList<>((List<Carta>) session.getAttribute("cartasCasa"));

        Carta cartaNueva = servicioBlackjack.pedirCarta();

        cartasJugadorActualizadas.add(cartaNueva);

        String nombreJugador = (String) session.getAttribute("nombre");

        EstadoPartida nuevoEstado = servicioBlackjack.estadoPartida(cartasJugadorActualizadas, cartasCasaActualizadas,
                false);
        String ganadorActualizado = servicioBlackjack.ganador(cartasJugadorActualizadas, cartasCasaActualizadas,
                nombreJugador, false);
        Integer puntajeActualizado = servicioBlackjack.calcularPuntuacion(cartasJugadorActualizadas);

        session.setAttribute("cartasJugador", cartasJugadorActualizadas);
        session.setAttribute("estadoPartida", nuevoEstado);
        session.setAttribute("ganador", ganadorActualizado);
        session.setAttribute("puntaje", puntajeActualizado);


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

        List<Carta> cartasJugador = new ArrayList<>((List<Carta>) session.getAttribute("cartasJugador"));
        List<Carta> cartasCasaActualizadas = new ArrayList<>((List<Carta>) session.getAttribute("cartasCasa"));
        String jugador = (String) session.getAttribute("nombre");


        List<Carta> cartasNuevasCrupier = servicioBlackjack.plantarse(cartasCasaActualizadas);
        cartasCasaActualizadas.addAll(cartasNuevasCrupier);
        String ganador = servicioBlackjack.ganador(cartasJugador, cartasCasaActualizadas, jugador, true);


        Map<String, Object> response = new HashMap<String, Object>();
        response.put("manoFinalCrupier", cartasNuevasCrupier);
        response.put("ganador", ganador);
        response.put("jugadorActual", jugador);
        response.put("estadoPartida", servicioBlackjack.estadoPartida(cartasJugador, cartasCasaActualizadas, true));

        return response;
    }

    @RequestMapping("/finalizarBlackjack")
    public ModelAndView finalizar(HttpSession session)
            throws PartidaConPuntajeNegativoException, IllegalArgumentException, BingoBotEsNullException {

        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
        Integer puntajeFinal = (Integer) session.getAttribute("puntaje");
        Boolean hayBlackjack = servicioBlackjack.hayBlackjack((List<Carta>) session.getAttribute("cartasJugador"));
        String ganador = (String) session.getAttribute("ganador");
        Boolean gano = false;
        LocalTime duracion = LocalTime.of(3, 00);
        if (ganador.equals(jugador.getNombre()) || ganador.equals("empate")) {
            gano = true;
        }
        servicioPlataforma.agregarPartida(
                new PartidaBlackJack(jugador.getId(), puntajeFinal, Juego.BLACKJACK, hayBlackjack, gano, duracion));
        return new ModelAndView("redirect:/inicio-blackjack");
    }

    @RequestMapping(path = "/reiniciarBlackjack")
    public ModelAndView reiniciar(HttpSession session)
            throws PartidaConPuntajeNegativoException, IllegalArgumentException, BingoBotEsNullException {

        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
        Integer puntajeFinal = (Integer) session.getAttribute("puntaje");
        Boolean hayBlackjack = servicioBlackjack.hayBlackjack((List<Carta>) session.getAttribute("cartasJugador"));
        String ganador = (String) session.getAttribute("ganador");
        Boolean gano = false;
        LocalTime duracion = LocalTime.of(3, 00);
        if (ganador.equals(jugador.getNombre()) || ganador.equals("empate")) {
            gano = true;
        }
        servicioPlataforma.agregarPartida(
                new PartidaBlackJack(jugador.getId(), puntajeFinal, Juego.BLACKJACK, hayBlackjack, gano, duracion));

        Boolean modoDificil = (Boolean) session.getAttribute("modoDificil");
        if (modoDificil == null)
            modoDificil = false;
        if (modoDificil) {
            return new ModelAndView("redirect:/blackjackDificil");
        }

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
            partidas = servicioPlataforma.obtenerUltimasPartidasDelUsuario(jugador.getId(), Juego.BLACKJACK);
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
