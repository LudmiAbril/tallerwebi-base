package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.BingoBotEsNullException;
import com.tallerwebi.dominio.excepcion.PartidaConPuntajeNegativoException;
import com.tallerwebi.dominio.excepcion.PartidaDeUsuarioNoEncontradaException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

//El controlador te lleva a dos modos:
//Modo acumular aciertos: se intenta lograr el mayor numero de aciertos consecutivos
//Modo cronometro: mayor numero de aciertos en un tiempo
@Controller
public class ControladorMayorMenor {
    private ServicioMayorMenor servicioMayorMenor;
    private ServicioPlataforma servicioPlataforma;
    private HttpSession session;

    public ControladorMayorMenor(ServicioMayorMenor servicioMayorMenorMock, ServicioPlataforma servicioPlataformaMock) {
        this.servicioMayorMenor = servicioMayorMenorMock;
        this.servicioPlataforma = servicioPlataformaMock;
    }

    @RequestMapping(path = "/inicioMayorMenor")
    public ModelAndView inicioMayorMenor(HttpSession session) {
        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");

        return new ModelAndView("irAMayorMenor");
    }
    // return new ModelAndView("redirect:/inicio-blackjack");

    @RequestMapping(path = "/ComenzarMayorMenor", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> comenzarMayorMenor(HttpSession session,
            @RequestParam(value = "contrareloj", defaultValue = "false") boolean contrareloj,
            @RequestParam(value = "tiempoLimite", required = false) Integer tiempoLimiteMinutos) {
        ModelMap modelo = new ModelMap();

        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
        List<Partida> partidasAnteriores = (List<Partida>) session.getAttribute("partidasMN");
        String nombreJugador = jugador.getNombre();
        servicioMayorMenor.inicializarBaraja();
        servicioMayorMenor.barajar();
        Carta cartaInicial = servicioMayorMenor.sacarCarta();
        session.setAttribute("partidas", partidasAnteriores);
        session.setAttribute("cartaJugador", cartaInicial);
        session.setAttribute("nombre", nombreJugador);
        session.setAttribute("aciertos", 0);
        Map<String, Object> response = new HashMap<>();
        response.put("jugadorActual", session.getAttribute("nombre"));
        response.put("cartaInicial", cartaInicial);
        response.put("partidas", partidasAnteriores);
        response.put("mensajePartidas", "Aun no se han jugado partidas.");
        return response;
    }

    @RequestMapping(path = "/reiniciarMayorMenor")
    public ModelAndView reiniciar(HttpSession session)
            throws PartidaConPuntajeNegativoException, IllegalArgumentException, BingoBotEsNullException {
        ModelMap model = new ModelMap();
        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
        Integer puntajeFinal = (Integer) session.getAttribute("aciertos");
        servicioPlataforma.agregarPartida(
                new PartidaMayorMenor(jugador.getId(), Juego.MAYOR_MENOR, puntajeFinal));
        List<Partida> partidasAnteriores = new ArrayList<Partida>();
        try {
            partidasAnteriores = servicioPlataforma.obtenerUltimasPartidasDelUsuario(jugador.getId(),
                    Juego.MAYOR_MENOR);
            session.setAttribute("partidasMN", partidasAnteriores);

        } catch (PartidaDeUsuarioNoEncontradaException e) {
            model.put("mensajePartidas", "Aun no se han jugado partidas.");
        }
        return new ModelAndView("MayorMenor", model);
    }

    @RequestMapping(path = "/comparar-carta", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> compararCarta(HttpSession session,
            @RequestParam(value = "eleccion") Valor eleccion) {
        Carta cartaDelMedio = (Carta) session.getAttribute("cartaJugador");
        Carta cartaNueva = servicioMayorMenor.sacarCarta();
        String nombreJugador = (String) session.getAttribute("nombre");
        Valor valorElegido = "mayor".equalsIgnoreCase(String.valueOf(eleccion)) ? Valor.MAYOR : Valor.MENOR;
        servicioMayorMenor.setValorElegido(valorElegido);
        Valor valorRecibido = servicioMayorMenor.getValorElegido();
        boolean acierto = servicioMayorMenor.evaluarCarta(cartaDelMedio, cartaNueva, valorElegido);
        Integer aciertos = (Integer) session.getAttribute("aciertos");
        if (aciertos == null) {
            aciertos = 0;
        }
        if (acierto) {
            aciertos++;
            session.setAttribute("cartaJugador", cartaNueva);
        } else {
            session.setAttribute("partidaTerminada", true);
            session.setAttribute("aciertos", aciertos);
        }

        session.setAttribute("aciertos", aciertos);
        Map<String, Object> response = new HashMap<>();
        response.put("jugadorActual", nombreJugador);
        response.put("cartaNueva", cartaNueva);
        response.put("acierto", acierto);
        response.put("aciertos", aciertos);
        response.put("partidaTerminada", !acierto);
        return response;
    }

    @RequestMapping("/finalizarMayorMenor")
    public ModelAndView finalizarMayorMenor(HttpSession session)
            throws PartidaConPuntajeNegativoException, IllegalArgumentException, BingoBotEsNullException {

        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
        Integer puntajeFinal = (Integer) session.getAttribute("aciertos");
        String ganador = (String) session.getAttribute("ganador");
        Boolean gano = false;
        LocalTime duracion = LocalTime.of(3, 00);

        servicioPlataforma.agregarPartida(
                new PartidaMayorMenor(jugador.getId(), Juego.MAYOR_MENOR, puntajeFinal));
        return new ModelAndView("redirect:/inicioMayorMenor");
    }

    // comenzarConTiempo
    @RequestMapping("/comenzarConTiempo")
    public ModelAndView comenzarConTiempo(HttpSession session,
            @RequestParam(value = "contrareloj", defaultValue = "false") boolean contrareloj,
            @RequestParam(value = "tiempoLimite", required = false) Integer tiempoLimiteMinutos) {
        Usuario user = (Usuario) session.getAttribute("jugadorActual");
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
        ModelMap model = new ModelMap();
        List<Partida> partidasAnteriores = new ArrayList<Partida>();
        try {
            partidasAnteriores = servicioPlataforma.obtenerUltimasPartidasDelUsuario(user.getId(),
                    Juego.MAYOR_MENOR);
            session.setAttribute("partidasMN", partidasAnteriores);

        } catch (PartidaDeUsuarioNoEncontradaException e) {
            model.put("mensajePartidas", "Aun no se han jugado partidas.");
        }

        return new ModelAndView("MayorMenor", model);
    }

}
