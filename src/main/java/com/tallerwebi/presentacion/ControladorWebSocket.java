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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

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

    @RequestMapping(path = "/comenzarJuegoBingo", method = RequestMethod.GET)
    public ModelAndView comenzarJuegoBingo(@RequestParam("tipo") String tipo, HttpSession session) {

        ModelMap model = new ModelMap();
        Usuario usuario;
        if (session.getAttribute("jugadorActual") == null
                || !(session.getAttribute("jugadorActual") instanceof Usuario)) {
            // Si no está o no es un Usuario, crea un nuevo Usuario y ponlo en la sesión
            usuario = new Usuario();
            usuario.setNombre("user");
            session.setAttribute("jugadorActual", usuario);
        } else {
            usuario = (Usuario) session.getAttribute("jugadorActual");
        }

        session.setAttribute("tiradaLimiteDeLaSesion", usuario.getConfig().getCantidadDePelotas());
        session.setAttribute("dimensionDelCartonDeLaSesion", usuario.getConfig().getDimensionCarton());

        Integer dimensionDelCartonDeLaSesion = (Integer) session.getAttribute("dimensionDelCartonDeLaSesion");
        CartonBingo carton = servicioBingo.generarCarton(dimensionDelCartonDeLaSesion);

        Set<Integer> numerosEntregados = new LinkedHashSet<Integer>();
        Integer numeroNuevo = this.servicioBingo.entregarNumeroAleatorio(numerosEntregados);
        Integer numeroCantadoAleatorio = numeroNuevo;
        numerosEntregados.add(numeroNuevo);
        session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);
        session.setAttribute("numeroAleatorioCantado", numeroCantadoAleatorio);
        session.setAttribute("carton", carton);

        String nombreJugador = usuario.getNombre();
        model.put("nombreJugador", nombreJugador);
        session.setAttribute("nombreJugador", nombreJugador);
        TipoPartidaBingo tipoPartidaBingo = TipoPartidaBingo.valueOf(tipo.toUpperCase());
        session.setAttribute("tipoPartidaBingo", tipoPartidaBingo);

        TipoPartidaBingo tipoPartidaBingoDeLaSesion = (TipoPartidaBingo) session.getAttribute("tipoPartidaBingo");
        model.put("tipoPartidaBingoDeLaSesion", tipoPartidaBingoDeLaSesion);
        return new ModelAndView("bingo-multijugador", model);
    }
}

