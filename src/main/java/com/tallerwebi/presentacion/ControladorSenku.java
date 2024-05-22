package com.tallerwebi.presentacion;

import java.util.HashMap;
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
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.Senku;
import com.tallerwebi.dominio.ServicioSenku;
import com.tallerwebi.dominio.Tablero;
import com.tallerwebi.dominio.excepcion.CasilleroInexistenteException;
import com.tallerwebi.dominio.excepcion.CasilleroVacio;


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
    public Map<String, Object> obtenerTablero(HttpSession session) {
        Tablero tablero=(Tablero) session.getAttribute("tablero");
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



    }
    
    


