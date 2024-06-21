package com.tallerwebi.presentacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ControladorWebSocket {
    @Autowired
    private ServicioBingo servicioBingo;


    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public String getNumero() throws Exception {
        HashSet<Integer> numerosEntregados = new HashSet<>();
        Integer numero = servicioBingo.entregarNumeroAleatorio(numerosEntregados);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(numero);
        System.out.println("Número enviado: " + json);
        return json;
    }

    @RequestMapping(path = "/comenzarJuegoBingoMultijugador", method = RequestMethod.GET)
    public ModelAndView comenzarJuegoBingoMultijugador( HttpSession session) {
        ModelMap model = new ModelMap();
        if (session.getAttribute("jugadorActual") == null)
                 {
            return new ModelAndView("redirect:/home");
        }
        Usuario usuario = (Usuario) session.getAttribute("jugadorActual");
        session.setAttribute("tiradaLimiteDeLaSesion", usuario.getConfig().getCantidadDePelotas());
        session.setAttribute("dimensionDelCartonDeLaSesion", usuario.getConfig().getDimensionCarton());

        Integer dimensionDelCartonDeLaSesion = (Integer) session.getAttribute("dimensionDelCartonDeLaSesion");
        CartonBingo carton = servicioBingo.generarCarton(dimensionDelCartonDeLaSesion);

        Set<Integer> numerosEntregados = new LinkedHashSet<Integer>();
        session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);
        /*Integer numeroNuevo = this.servicioBingo.entregarNumeroAleatorio(numerosEntregados);
        Integer numeroCantadoAleatorio = numeroNuevo;
        numerosEntregados.add(numeroNuevo);

        session.setAttribute("numeroAleatorioCantado", numeroCantadoAleatorio);*/
        session.setAttribute("carton", carton);

        String nombreJugador = usuario.getNombre();
        model.put("nombreJugador", nombreJugador);
        session.setAttribute("nombreJugador", nombreJugador);
        //TipoPartidaBingo tipoPartidaBingo = TipoPartidaBingo.valueOf(tipo.toUpperCase());
        //session.setAttribute("tipoPartidaBingo", tipoPartidaBingo);

        //TipoPartidaBingo tipoPartidaBingoDeLaSesion = (TipoPartidaBingo) session.getAttribute("tipoPartidaBingo");
        //model.put("tipoPartidaBingoDeLaSesion", tipoPartidaBingoDeLaSesion);
        return new ModelAndView("bingo-multijugador", model);
    }

    @RequestMapping(path = "/obtenerDatosInicialesMultijugador", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> obtenerDatosInicialesMultijugador(HttpSession session) {
        CartonBingo carton = (CartonBingo) session.getAttribute("carton");
        //Integer numeroCantadoAleatorio = (Integer) session.getAttribute("numeroAleatorioCantado");
        Set<Integer> numerosEntregados = (Set<Integer>) session.getAttribute("numerosEntregadosDeLaSesion");
        if (numerosEntregados == null) {
            numerosEntregados = new LinkedHashSet<>();
            session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);
        }
        //numerosEntregados.add(numeroCantadoAleatorio);
        //session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);

        HashSet<Integer> numerosMarcadosDeLaSesion = new HashSet<Integer>();
        session.setAttribute("numerosMarcadosDeLaSesion", numerosMarcadosDeLaSesion);
        //TipoPartidaBingo tipoPartidaBingo = (TipoPartidaBingo) session.getAttribute("tipoPartidaBingo");

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("carton", carton);
        //respuesta.put("numeroAleatorioCantado", numeroCantadoAleatorio);
        //respuesta.put("tipoPartidaBingo", tipoPartidaBingo);
        return respuesta;
    }
}

