package com.tallerwebi.presentacion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Casillero;
import com.tallerwebi.dominio.Juego;
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.Partida;
import com.tallerwebi.dominio.Senku;
import com.tallerwebi.dominio.ServicioPlataforma;
import com.tallerwebi.dominio.ServicioSenku;
import com.tallerwebi.dominio.Tablero;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.CasilleroInexistenteException;
import com.tallerwebi.dominio.excepcion.CasilleroVacio;
import com.tallerwebi.dominio.excepcion.PartidaDeUsuarioNoEncontradaException;

@Controller
public class ControladorSenku {

    private ServicioSenku servicioSenku;
    private ServicioPlataforma servicioPlataforma;
    @Autowired
    public ControladorSenku(ServicioSenku servicioSenku, ServicioPlataforma servicioPlataforma) {
        this.servicioSenku = servicioSenku;
        this.servicioPlataforma = servicioPlataforma;
    }
    
    @RequestMapping(path = "/irAlSenku", method = RequestMethod.GET)
    public ModelAndView inicioSenku() {
        ModelMap modelo = new ModelMap();
        modelo.put("nuevoJugador", new Jugador());
        return new ModelAndView("irAlSenku", modelo);
    }
    @RequestMapping(path = "/comenzarJuegoSenku", method = RequestMethod.GET)
	public ModelAndView comenzarJuegoBingo(@RequestParam("tipo") String tipo, HttpSession session) {

		ModelMap model = new ModelMap();
		Usuario usuario;
		if (session.getAttribute("jugadorActual") == null || !(session.getAttribute("jugadorActual") instanceof Usuario)) {
			// Si no está o no es un Usuario, crea un nuevo Usuario y ponlo en la sesión
			usuario = new Usuario();
			usuario.setNombre("user");
			session.setAttribute("jugadorActual", usuario);
		}
		else {
			// Si está, recupéralo de la sesión
			usuario = (Usuario) session.getAttribute("jugadorActual");
		}
        return new ModelAndView("senku", model);
         }
        
        @RequestMapping(path = "/obtenerTablero", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> obtenerTablero(HttpSession session) {
        Tablero tablero = (Tablero) session.getAttribute("tablero");
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("tablero", tablero);
        return respuesta;
    }
    
    @RequestMapping(path = "/marcarCasillero/{x}/{y}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> marcarCasillero(@PathVariable Integer x, @PathVariable Integer y, HttpSession session) {
        Map<String, Object> respuesta = new HashMap<>();
        Tablero tablero = (Tablero) session.getAttribute("tablero");
        try {
            Casillero casilleroSeleccionado = servicioSenku.seleccionarCasillero(tablero, x, y);
            // Agrega lógica adicional si es necesario
            respuesta.put("success", true);
        } catch (CasilleroVacio e) {
            respuesta.put("success", false);
            respuesta.put("message", "El casillero seleccionado está vacío.");
        } catch (CasilleroInexistenteException e) {
            respuesta.put("success", false);
            respuesta.put("message", "El casillero seleccionado no existe en el tablero.");
        }
        return respuesta;
    }
    /*

    @RequestMapping("/finalizar")
    public ModelAndView finalizar(HttpSession session) {
        String jugador = (String) session.getAttribute("jugadorActual");
        servicioPlataforma.agregarPartida(new Partida(jugador, 0, Juego.SENKU));
        return new ModelAndView("redirect:/irAlSenku");
    }

    @RequestMapping(path = "/reiniciar", method = RequestMethod.POST)
    public ModelAndView reiniciar(HttpSession session) {
        String jugador = (String) session.getAttribute("jugadorActual");
        Integer puntajeFinal = (Integer) session.getAttribute("puntaje");

        if (jugador != null && puntajeFinal != null) {
            servicioPlataforma.agregarPartida(new Partida(jugador, puntajeFinal, Juego.SENKU));
        }

        Senku senku = new Senku(5);
        Tablero tablero = senku.getTablero();

        // Actualizar los atributos de sesión
        session.setAttribute("senku", senku);
        session.setAttribute("tablero", tablero);

        List<Partida> partidas = new ArrayList<>();
        ModelMap model = new ModelMap();

        try {
            partidas = servicioPlataforma.obtenerPartidasUsuario(jugador, Juego.SENKU);
            session.setAttribute("partidas", partidas);
            model.addAttribute("partidas", partidas);
        } catch (PartidaDeUsuarioNoEncontradaException e) {
            model.addAttribute("mensajePartidas", "aun no hay partidas registradas");
        }

        return new ModelAndView("senku", model);
    }

    @RequestMapping(path = "/reiniciar-senku", method = RequestMethod.GET)
    public ModelAndView mostrarSenku() {
        return new ModelAndView("senku");
    }

    @RequestMapping(path = "/comenzar", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> comenzarJuego(HttpSession session) {
        Tablero tablero = (Tablero) session.getAttribute("tablero");
        String nombre = (String) session.getAttribute("jugadorActual");
        Boolean contrareloj = (Boolean) session.getAttribute("contrareloj");
        List<Partida> partidasAnteriores = (List<Partida>) session.getAttribute("partidas");

        Map<String, Object> response = new HashMap<>();
        if (contrareloj != null && contrareloj) {
            response.put("contrareloj", true);
            response.put("tiempoLimite", session.getAttribute("tiempoLimite"));
        } else {
            response.put("contrareloj", false);
        }
        response.put("tablero", tablero);
        response.put("jugadorActual", nombre);
        response.put("partidas", partidasAnteriores);
        return response;
    }*/
}

    


