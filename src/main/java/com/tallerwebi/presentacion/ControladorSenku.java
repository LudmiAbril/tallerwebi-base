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
import com.tallerwebi.dominio.PartidaSenku;
import com.tallerwebi.dominio.Senku;
import com.tallerwebi.dominio.ServicioPlataforma;
import com.tallerwebi.dominio.ServicioSenku;
import com.tallerwebi.dominio.Tablero;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.CasilleroInexistenteException;
import com.tallerwebi.dominio.excepcion.CasilleroVacio;
import com.tallerwebi.dominio.excepcion.MovimientoInvalidoException;
import com.tallerwebi.dominio.excepcion.PartidaConPuntajeNegativoException;
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

    // PANTALLA DE IR AL SENKU
    @RequestMapping(path = "/irAlSenku", method = RequestMethod.GET)
    public ModelAndView inicioSenku() {
        ModelMap modelo = new ModelMap();
        modelo.put("nuevoJugador", new Jugador());
        return new ModelAndView("irAlSenku", modelo);
    }
    // BOTON DE JUGAR

    @RequestMapping(path = "/comenzarJuegoSenku", method = RequestMethod.POST)
    public ModelAndView comenzarJuegoSenku(HttpSession session) {
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
        Tablero tablero = new Tablero(5);
        session.setAttribute("tablero", tablero);
        // PONGO LOS DATOS EN EL MODELO ASI LOS PUEDO RENDERIZAR CON THIMELEAF
        model.put("mensaje", "¡Bienvenido " + usuario.getNombre() + "! Comienza tu juego.");
        model.put("nombreJugador", usuario.getNombre());
        model.put("contadorMovimientos", 0);
        session.setAttribute("contadorMovimientos", 0);
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
    public Map<String, Object> moverOSeleccionar(@PathVariable Integer x, @PathVariable Integer y,
            HttpSession session) {
        Map<String, Object> respuesta = new HashMap<>();
        Tablero tablero = (Tablero) session.getAttribute("tablero");
        Casillero casilleroSeleccionado = (Casillero) session.getAttribute("casilleroSeleccionado");

        try {
            if (casilleroSeleccionado != null) {
                Casillero casilleroDestino = servicioSenku.getCasillero(tablero, x, y);
                if (!casilleroDestino.getOcupado()) {
                    servicioSenku.realizarMovimiento(tablero, casilleroSeleccionado, casilleroDestino);
                    session.removeAttribute("casilleroSeleccionado");
                    Integer contadorMovimientos = (Integer) session.getAttribute("contadorMovimientos");

                    contadorMovimientos++;
                    session.setAttribute("contadorMovimientos", contadorMovimientos);
                    respuesta.put("success", true);
                    respuesta.put("mensaje", "Movimiento realizado con éxito.");
                    respuesta.put("contadorMovimientos", contadorMovimientos);
                } else {
                    respuesta.put("success", false);
                    respuesta.put("mensaje", "El casillero de destino debe estar vacío.");
                    session.removeAttribute("casilleroSeleccionado");
                }
            } else {
                Casillero casilleroSeleccionadoNuevo = servicioSenku.seleccionarCasillero(tablero, x, y);
                session.setAttribute("casilleroSeleccionado", casilleroSeleccionadoNuevo);
                respuesta.put("success", true);
                respuesta.put("mensaje", "Casillero seleccionado con éxito.");
            }
        } catch (CasilleroVacio e) {
            respuesta.put("success", false);
            respuesta.put("mensaje", "El casillero seleccionado está vacío.");
            session.removeAttribute("casilleroSeleccionado");
        } catch (CasilleroInexistenteException e) {
            respuesta.put("success", false);
            respuesta.put("mensaje", "El casillero seleccionado no existe en el tablero.");
            session.removeAttribute("casilleroSeleccionado");
        } catch (MovimientoInvalidoException e) {
            respuesta.put("success", false);
            respuesta.put("mensaje", e.getMessage());
            session.removeAttribute("casilleroSeleccionado");
        }

        return respuesta;
    }

    @RequestMapping(path = "/reiniciar", method = RequestMethod.POST)
    public ModelAndView reiniciarPartida(HttpSession session) {

        Tablero tablero = new Tablero(5);
        session.setAttribute("tablero", tablero);
        session.setAttribute("contadorMovimientos", 0);
        session.removeAttribute("casilleroSeleccionado");

        Usuario usuario = (Usuario) session.getAttribute("jugadorActual");
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setNombre("user");
            session.setAttribute("jugadorActual", usuario);
        }

        ModelMap model = new ModelMap();
        model.put("mensaje", "Partida reiniciada. ¡Buena suerte " + usuario.getNombre() + "!");
        model.put("nombreJugador", usuario.getNombre());
        model.put("contadorMovimientos", 0);

        return new ModelAndView("senku", model);
    }

    @RequestMapping(path = "/senkuGano", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> comprobarSiSeGano(HttpSession session) {
        Map<String, Object> respuesta = new HashMap<>();
    
        if (session.getAttribute("tablero") == null) {
            respuesta.put("error", "No se encontró el tablero en la sesión");
            return respuesta;
        }
    
        Tablero tablero = (Tablero) session.getAttribute("tablero");
        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
    
        Boolean seGano = servicioSenku.seGano(tablero);
        Boolean movimientosDisponibles = true;
    
        if (!seGano) {
            try {
                movimientosDisponibles = servicioSenku.validarQueHayaMovimientosValidosDisponibles(tablero);
            } catch (MovimientoInvalidoException e) {
                e.printStackTrace();
            }
        }
    
        session.setAttribute("seGano", seGano);
        session.setAttribute("movimientosDisponibles", movimientosDisponibles);
    
        respuesta.put("seGano", seGano);
        respuesta.put("movimientosDisponibles", movimientosDisponibles);
        String nombreJugador = (jugador != null) ? jugador.getNombre() : "Jugador Desconocido";
        respuesta.put("nombreJugador", nombreJugador);
    
        return respuesta;
    }
    
    
    @RequestMapping(path = "/senkuFinalizarPartida", method = RequestMethod.POST)
public ModelAndView finalizarPartida(HttpSession session) {
    Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
    Tablero tablero = (Tablero) session.getAttribute("tablero");

    Boolean seGano = servicioSenku.seGano(tablero);

    if (seGano != null && seGano) {
        Partida partidaSenku = new PartidaSenku();
        Usuario jugadorActual = (Usuario) session.getAttribute("jugadorActual");
        Long id = jugadorActual.getId();

        partidaSenku.setIdJugador(id);
        partidaSenku.setJuego(Juego.SENKU);
        ((PartidaSenku) partidaSenku).setGanado(seGano);
        ((PartidaSenku) partidaSenku).setCantidadMovimientos((Integer) session.getAttribute("contadorMovimientos"));

        try {
            servicioPlataforma.agregarPartida(partidaSenku);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (PartidaConPuntajeNegativoException e) {
            e.printStackTrace();
        }

        return new ModelAndView("redirect:/acceso-juegos");
    }
    return new ModelAndView("redirect:/acceso-juegos");
}


}
