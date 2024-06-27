package com.tallerwebi.presentacion;

import java.util.*;

import javax.servlet.http.HttpSession;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.PartidaConPuntajeNegativoException;
import com.tallerwebi.infraestructura.ServicioBingoImpl;
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

@Controller
public class ControladorBingoBot {

    private ServicioBingo servicioBingo;
    private ServicioPlataforma servicioPlataforma;

    @Autowired
    public ControladorBingoBot(ServicioBingo servicioBingo, ServicioPlataforma servicioPlataforma) {
        this.servicioBingo = servicioBingo;
        this.servicioPlataforma = servicioPlataforma;
    }

    @RequestMapping(path = "/comenzarJuegoBingoBot", method = RequestMethod.GET)
    public ModelAndView comenzarJuegoBingoBot(HttpSession session) {

        ModelMap model = new ModelMap();
        Usuario usuario;
        if (session.getAttribute("jugadorActual") == null
                || !(session.getAttribute("jugadorActual") instanceof Usuario)) {
            usuario = new Usuario();
            usuario.setNombre("user");
            session.setAttribute("jugadorActual", usuario);
        } else {
            usuario = (Usuario) session.getAttribute("jugadorActual");
        }

        Integer maxTirada = 99;
        session.setAttribute("tiradaLimiteDeLaSesion", maxTirada);
        session.setAttribute("dimensionDelCartonDeLaSesion", usuario.getConfig().getDimensionCarton());

        Integer dimensionDelCartonDeLaSesion = (Integer) session.getAttribute("dimensionDelCartonDeLaSesion");
        CartonBingo carton = servicioBingo.generarCarton(dimensionDelCartonDeLaSesion);
        session.setAttribute("carton", carton);

        Set<Integer> numerosEntregados = new LinkedHashSet<Integer>();
        Integer numeroNuevo = this.servicioBingo.entregarNumeroAleatorio(numerosEntregados);
        Integer numeroCantadoAleatorio = numeroNuevo;
        numerosEntregados.add(numeroNuevo);
        session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);
        session.setAttribute("numeroAleatorioCantado", numeroCantadoAleatorio);

        String nombreJugador = usuario.getNombre();
        model.put("nombreJugador", nombreJugador);

        session.setAttribute("nombreJugador", nombreJugador);

        TipoPartidaBingo tipoPartidaBingo = TipoPartidaBingo.BINGO;
        session.setAttribute("tipoPartidaBingo", tipoPartidaBingo);

        TipoPartidaBingo tipoPartidaBingoDeLaSesion = (TipoPartidaBingo) session.getAttribute("tipoPartidaBingo");
        model.put("tipoPartidaBingoDeLaSesion", tipoPartidaBingoDeLaSesion);

        CartonBingo cartonBot = this.servicioBingo.generarCarton(dimensionDelCartonDeLaSesion);
        session.setAttribute("cartonBot", cartonBot);
        return new ModelAndView("bingoBot", model);
    }

    @RequestMapping(path = "/obtenerDatosInicialesBot", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> obtenerDatosIniciales(HttpSession session) {

        CartonBingo carton = (CartonBingo) session.getAttribute("carton");
        CartonBingo cartonBot = (CartonBingo) session.getAttribute("cartonBot");
        Integer tirada = (Integer) session.getAttribute("tiradaLimiteDeLaSesion");
        Integer numeroCantadoAleatorio = (Integer) session.getAttribute("numeroAleatorioCantado");
        Set<Integer> numerosEntregados = (Set<Integer>) session.getAttribute("numerosEntregadosDeLaSesion");
        if (numerosEntregados == null) {
            numerosEntregados = new LinkedHashSet<>();
            session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);
        }
        numerosEntregados.add(numeroCantadoAleatorio);
        session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);

        HashSet<Integer> numerosMarcadosDeLaSesion = new HashSet<Integer>();
        session.setAttribute("numerosMarcadosDeLaSesion", numerosMarcadosDeLaSesion);
        session.setAttribute("numerosMarcadosBot", numerosMarcadosDeLaSesion);
        TipoPartidaBingo tipoPartidaBingo = (TipoPartidaBingo) session.getAttribute("tipoPartidaBingo");

        Integer numerosRestantesParaCompletarLaTirada = this.servicioBingo
                .obtenerCantidadDeNumerosRestantesParaCompletarLaTirada(tirada,
                        numerosEntregados.size());

        session.setAttribute("numerosRestantesParaCompletarLaTiradaDeLaSesion",
                numerosRestantesParaCompletarLaTirada);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("carton", carton);
        respuesta.put("numeroAleatorioCantado", numeroCantadoAleatorio);
        respuesta.put("tipoPartidaBingo", tipoPartidaBingo);
        respuesta.put("numerosRestantesParaCompletarLaTirada",
                numerosRestantesParaCompletarLaTirada);
        respuesta.put("cartonBot", cartonBot);
        return respuesta;
    }



    @RequestMapping(path = "/obtenerNuevoNumeroBot", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> obtenerNuevoNumero(HttpSession session) throws PartidaConPuntajeNegativoException {
        Integer nuevoNumero = 0;
        CartonBingo cartonBot = (CartonBingo) session.getAttribute("cartonBot");
        Set<Integer> numerosEntregados = (Set<Integer>) session.getAttribute("numerosEntregadosDeLaSesion");
        Integer tiradaLimiteDeLaSesion = (Integer) session.getAttribute("tiradaLimiteDeLaSesion");
        Boolean limiteAlcanzado = false;
        Map<String, Object> respuesta = new HashMap<>();

        if (numerosEntregados == null) {
            numerosEntregados = new LinkedHashSet<>();
            session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);
        }
        if (numerosEntregados.size() == tiradaLimiteDeLaSesion) {
            session.setAttribute("seHizoLinea", false);
            session.setAttribute("seHizoBingo", false);
            limiteAlcanzado = true;
            respuesta.put("limiteAlcanzado", limiteAlcanzado);
        } else {
            nuevoNumero = servicioBingo.entregarNumeroAleatorio(numerosEntregados);
            // Integer tirada = (Integer) session.getAttribute("tiradaLimiteDeLaSesion");
            Integer numerosRestantesParaCompletarLaTirada = this.servicioBingo
                    .obtenerCantidadDeNumerosRestantesParaCompletarLaTirada(tiradaLimiteDeLaSesion,
                            numerosEntregados.size());
            numerosEntregados.add(nuevoNumero);
            session.setAttribute("numeroAleatorioCantado", nuevoNumero);
            session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);

            respuesta.put("nuevoNumero", nuevoNumero);
            respuesta.put("limiteAlcanzado", false);
            respuesta.put("numerosRestantesParaCompletarLaTirada",
                    numerosRestantesParaCompletarLaTirada);
        }

        Boolean seMarcoBot = false;
        Boolean seHizoBingoBot = false;
        Set<Integer> numerosMarcadosBot;

        if (this.servicioBingo.marcarCasilleroBot(nuevoNumero, cartonBot)) {
            numerosMarcadosBot = (Set<Integer>) session.getAttribute("numerosMarcadosBot");
            session.setAttribute("numerosMarcadosBot", this.servicioBingo.getNumerosMarcadosBot());
            seMarcoBot = true;
            Integer dimension = (Integer) session.getAttribute("dimensionDelCartonDeLaSesion");
            session.setAttribute("numerosMarcadosBot", this.servicioBingo.getNumerosMarcadosBot());
            seHizoBingoBot = this.servicioBingo.bingo(numerosMarcadosBot, dimension);
        }

        numerosMarcadosBot = (Set<Integer>) session.getAttribute("numerosMarcadosBot");
        session.setAttribute("seHizoBingoBot", seHizoBingoBot);

        respuesta.put("numerosMarcadosBot", numerosMarcadosBot);
        respuesta.put("seMarcoBot", seMarcoBot);
        respuesta.put("seHizoBingoBot", seHizoBingoBot);

        return respuesta;
    }


    @RequestMapping(path = "/obtenerCincoUltimosNumerosEntregadosBot", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> obtenerCincoUltimosNumerosEntregados(HttpSession session) {
        Set<Integer> numerosEntregadosDeLaSesion = (Set<Integer>) session.getAttribute("numerosEntregadosDeLaSesion");

        if (numerosEntregadosDeLaSesion == null) {
            numerosEntregadosDeLaSesion = new LinkedHashSet<>();
            session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregadosDeLaSesion);
        }

        List<Integer> ultimosNumeros = new ArrayList<>(numerosEntregadosDeLaSesion);

        int startIndex = Math.max(0, ultimosNumeros.size() - 5);
        List<Integer> numerosParaMostrar = ultimosNumeros.subList(startIndex, ultimosNumeros.size());

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("ultimosNumerosEntregados", numerosParaMostrar);
        return respuesta;
    }

    @RequestMapping(path = "/finalizarPartidaBot", method = RequestMethod.POST)
    public ModelAndView finalizar(HttpSession session) throws PartidaConPuntajeNegativoException,
            IllegalArgumentException {
        return new ModelAndView("irAlBingo");
    }

}