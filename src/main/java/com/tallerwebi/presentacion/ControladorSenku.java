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
import com.tallerwebi.dominio.excepcion.MovimientoInvalidoException;
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
    //PANTALLA DE IR AL SENKU
    @RequestMapping(path = "/irAlSenku", method = RequestMethod.GET)
    public ModelAndView inicioSenku() {
        ModelMap modelo = new ModelMap();
        modelo.put("nuevoJugador", new Jugador());
        return new ModelAndView("irAlSenku", modelo);
    }
    //BOTON DE JUGAR

    @RequestMapping(path = "/comenzarJuegoSenku", method = RequestMethod.POST)
    public ModelAndView comenzarJuegoSenku(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuario;
        if (session.getAttribute("jugadorActual") == null || !(session.getAttribute("jugadorActual") instanceof Usuario)) {
            // Si no está o no es un Usuario, crea un nuevo Usuario y ponlo en la sesión
            usuario = new Usuario();
            usuario.setNombre("user");
            session.setAttribute("jugadorActual", usuario);
        } else {
            usuario = (Usuario) session.getAttribute("jugadorActual");
        }
        Tablero tablero = new Tablero(5); 
    session.setAttribute("tablero", tablero);
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
            session.setAttribute("casilleroSeleccionado", casilleroSeleccionado);
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

    @RequestMapping(path = "/moverOSeleccionar/{x}/{y}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> moverOSeleccionar(@PathVariable Integer x, @PathVariable Integer y, HttpSession session) {
        Map<String, Object> respuesta = new HashMap<>();
        Tablero tablero = (Tablero) session.getAttribute("tablero");
        Casillero casilleroSeleccionado = (Casillero) session.getAttribute("casilleroSeleccionado");

        try {
            if (casilleroSeleccionado != null) {
                // Intentar mover
                Casillero casilleroDestino = servicioSenku.getCasillero(tablero, x, y);
                if (!casilleroDestino.getOcupado()) {
                    servicioSenku.realizarMovimiento(tablero, casilleroSeleccionado, casilleroDestino);
                    session.removeAttribute("casilleroSeleccionado");
                    respuesta.put("success", true);
                    respuesta.put("message", "Movimiento realizado con éxito.");
                } else {
                    respuesta.put("success", false);
                    respuesta.put("message", "El casillero de destino debe estar vacío.");
                }
            } else {
                // Seleccionar casillero
                Casillero casilleroSeleccionadoNuevo = servicioSenku.seleccionarCasillero(tablero, x, y);
                session.setAttribute("casilleroSeleccionado", casilleroSeleccionadoNuevo);
                respuesta.put("success", true);
                respuesta.put("message", "Casillero seleccionado con éxito.");
            }
        } catch (CasilleroVacio e) {
            respuesta.put("success", false);
            respuesta.put("message", "El casillero seleccionado está vacío.");
        } catch (CasilleroInexistenteException e) {
            respuesta.put("success", false);
            respuesta.put("message", "El casillero seleccionado no existe en el tablero.");
        } catch (MovimientoInvalidoException e) {
            respuesta.put("success", false);
            respuesta.put("message", e.getMessage());
        }

        return respuesta;
    }
}


    


