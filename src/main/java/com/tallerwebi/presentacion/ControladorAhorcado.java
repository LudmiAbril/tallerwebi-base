package com.tallerwebi.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.ServicioAhorcado;
import com.tallerwebi.dominio.Usuario;

import javax.servlet.http.HttpSession;

@Controller
public class ControladorAhorcado {

    private ServicioAhorcado servicioAhorcado;

    @Autowired
    public ControladorAhorcado(ServicioAhorcado servicioAhorcado) {
        this.servicioAhorcado = servicioAhorcado;
    }
    @RequestMapping(path = "/irAlAhorcado", method = RequestMethod.GET)
    public ModelAndView irAlAhorcado(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
        model.put("nuevoJugador", jugador);
        return new ModelAndView("irAlAhorcado", model);
    }
    @RequestMapping(path = "/ahorcadoJuego", method = RequestMethod.GET)
    public ModelAndView ahorcadoJuego(HttpSession session) {
        ModelMap model = new ModelMap();
        try {
            Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
            if (jugador == null) {
                return new ModelAndView("redirect:/home");
            }
            model.put("jugador", jugador);
            String palabra = servicioAhorcado.entregarPalabra();
            model.put("palabra", palabra);
            int partesAhorcado = 6; 
            model.put("partesAhorcado", partesAhorcado);
            return new ModelAndView("ahorcado", model);
        } catch (Exception e) {
            model.put("mensajeError", "No se pudo inicializar el jugador.");
            model.put("error", e.getMessage());
            return new ModelAndView("forward:/irAlAhorcado", model);
        }
    }
    
    


    @RequestMapping(path = "/intentarLetra", method = RequestMethod.POST)
    public ModelAndView intentarLetra(@RequestParam("letra") Character letra, HttpSession session) {
        ModelMap model = new ModelMap();
        try {
            String palabra = (String) session.getAttribute("palabra");
            Integer partesAhorcado = (Integer) session.getAttribute("partesAhorcado");
            StringBuilder letrasIntentadas = (StringBuilder) session.getAttribute("letrasIntentadas");
    
            partesAhorcado = servicioAhorcado.intentarLetra(letra, palabra, partesAhorcado);
            letrasIntentadas.append(letra);
    
            session.setAttribute("partesAhorcado", partesAhorcado);
            session.setAttribute("letrasIntentadas", letrasIntentadas);
    
            if (servicioAhorcado.Perdio(partesAhorcado)) {
                model.put("mensajeError", "Perdiste. La palabra era: " + palabra);
                return new ModelAndView("perdiste", model);
            }
    
            String palabraOculta = mostrarPalabraOculta(palabra, letrasIntentadas.toString());
    
            model.put("jugador", session.getAttribute("jugador"));
            model.put("palabra", palabraOculta);
            model.put("partesAhorcado", partesAhorcado);
            model.put("letrasIntentadas", letrasIntentadas.toString());
    
            if (palabraOculta.equals(palabra)) {
                model.put("mensajeExito", "¡Ganaste!");
                return new ModelAndView("ganaste", model);
            }
    
            return new ModelAndView("ahorcado", model);
        } catch (Exception e) {
            model.put("mensajeError", "Ocurrió un error.");
            return new ModelAndView("error", model);
        }
    }
    
    

    public String mostrarPalabraOculta(String palabra, String letrasIntentadas) {
        StringBuilder palabraOculta = new StringBuilder(palabra.length());
    
        for (int i = 0; i < palabra.length(); i++) {
            char letra = palabra.charAt(i);
            
            if (letra == ' ') {
                palabraOculta.append(' '); // Mantener el espacio en blanco sin cambios
            } else if (letrasIntentadas.indexOf(letra) >= 0) {
                palabraOculta.append(letra); // Mostrar la letra si está en letrasIntentadas
            } else {
                palabraOculta.append('_'); // Ocultar la letra si no está en letrasIntentadas
            }
        }
    
        return palabraOculta.toString();
    }

    @RequestMapping(path = "/intentarPalabra", method = RequestMethod.POST)
    public ModelAndView intentarPalabra(@RequestParam("intento") String intento, HttpSession session) {
        ModelMap model = new ModelMap();
        try {
            String palabra = (String) session.getAttribute("palabra");
            Integer partesAhorcado = (Integer) session.getAttribute("partesAhorcado");
            //StringBuilder letrasIntentadas = (StringBuilder) session.getAttribute("letrasIntentadas");

            partesAhorcado = servicioAhorcado.intentarPalabra(intento, palabra);

            session.setAttribute("partesAhorcado", partesAhorcado);
            if (servicioAhorcado.Perdio(partesAhorcado)) {
                model.put("mensajeError", "Perdiste. La palabra era: " + palabra);
                return new ModelAndView("perdiste", model);
            }

            model.put("jugador", session.getAttribute("jugador"));

            model.put("partesAhorcado", partesAhorcado);

            if (intento.equals(palabra)) {
                model.put("mensajeExito", "¡Ganaste!");
                model.put("palabra", intento);
                return new ModelAndView("ganaste", model);
            }

            return new ModelAndView("ahorcado", model);
        } catch (Exception e) {
            model.put("mensajeError", "Ocurrió un error.");
            return new ModelAndView("error", model);
        }
    }

}