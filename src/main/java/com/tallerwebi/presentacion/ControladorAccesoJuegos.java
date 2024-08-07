package com.tallerwebi.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.tallerwebi.dominio.Juego;
import com.tallerwebi.dominio.Partida;
import com.tallerwebi.dominio.PartidaBingo;
import com.tallerwebi.dominio.ServicioPlataforma;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.NoHayPartidasDeBingoException;
import com.tallerwebi.dominio.excepcion.PartidaDeUsuarioNoEncontradaException;
import com.tallerwebi.dominio.excepcion.PartidasDelJuegoNoEncontradasException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

@Controller
public class ControladorAccesoJuegos {
    private ServicioPlataforma servicioPlataforma;
    private ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorAccesoJuegos(ServicioPlataforma servicioPlataforma, ServicioUsuario servicioUsuario) {

        this.servicioPlataforma = servicioPlataforma;
        this.servicioUsuario = servicioUsuario;
    }

    @RequestMapping(path = "/getHistorialPartidas", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> obtenerNuevoNumero(HttpSession session) {
        Map<String, Object> response = new HashMap();
        Usuario user = (Usuario) session.getAttribute("jugadorActual");
        Long idUsuario = user.getId();
        List<Partida> partidas;
        try {
            partidas = servicioPlataforma.obtenerTodasLasPartidasDelUsuario(idUsuario);
            response.put("hayPartidas", true);
            response.put("partidas", partidas);
        } catch (PartidaDeUsuarioNoEncontradaException e) {
            response.put("hayPartidas", false);
        }
        return response;
    }

    @RequestMapping(path = "/acceso-juegos")
    public ModelAndView accesoJuegos(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario jugador;
        if (session.getAttribute("jugadorActual") == null
                || !(session.getAttribute("jugadorActual") instanceof Usuario)) {

            jugador = new Usuario();
            jugador.setNombre("user");
            session.setAttribute("jugadorActual", jugador);
        } else {
            jugador = (Usuario) session.getAttribute("jugadorActual");
        }
        model.addAttribute("jugador", jugador.getNombre());
        model.addAttribute("usuarioConfig", jugador.getConfig());
        return new ModelAndView("acceso-juegos", model);
    }
    @RequestMapping(path = "/aboutUs")
    public ModelAndView aboutUs() {
       
        return new ModelAndView("aboutUs");
    }
    @RequestMapping(path = "/verRanking")
    public ModelAndView verRanking(@RequestParam("tipoJuego") Juego tipoJuego) {

        ModelMap mav = new ModelMap();

        List<Partida> partidas;
        try {
            partidas = servicioPlataforma.generarRanking(tipoJuego);
            mav.addAttribute("partidas", partidas);
        } catch (PartidasDelJuegoNoEncontradasException e) {
            mav.addAttribute("mensajeError",
                    "todavía no hay instancias de partidas de este juego, ¿por qué no las empezás?");
        }
        mav.addAttribute("nombreJuego", tipoJuego.toString());
        mav.addAttribute("tipoJuego", tipoJuego);

        return new ModelAndView("ranking", mav);
    }

    @RequestMapping(path = "/verRankingBingo")
    public ModelAndView verRankingBingo(@RequestParam("tipoJuego") Juego tipoJuego, HttpSession session) {

        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
        Long idUsuario = jugador.getId();
        ModelMap mav = new ModelMap();

        List<PartidaBingo> partidas;
        try {
            partidas = servicioPlataforma.generarRankingDePartidasDeBingo(idUsuario);
            mav.addAttribute("partidas", partidas);
        } catch (NoHayPartidasDeBingoException e) {
            mav.addAttribute("mensajeError",
                    "todavía no hay instancias de partidas de este juego, ¿por qué no las empezás?");
        }
        mav.addAttribute("nombreJuego", tipoJuego.toString());
        mav.addAttribute("tipoJuego", tipoJuego);

        return new ModelAndView("ranking", mav);
    }

    @RequestMapping(path = "/volverAlMenu")
    public ModelAndView volverAlMenuDeJuegos(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
        if (jugador == null) {
            return new ModelAndView("redirect:/login", model);
        }
        model.addAttribute("jugador", jugador.getNombre());
        model.addAttribute("usuarioConfig", jugador.getConfig());

        return new ModelAndView("acceso-juegos", model);
    }

    @RequestMapping(path = "/guardarCambios", method = RequestMethod.POST)
    public ModelAndView guardarCambios(@RequestParam("duracionBlackjack") Integer duracionBlackjack,
            @RequestParam("valorAs") Integer valorAs, @RequestParam("cantidadPelotas") Integer cantidadPelotas,
            @RequestParam("dimensionCarton") Integer dimensionCarton,
            @RequestParam("dimensionTablero") Integer dimensionTablero,
            @RequestParam("MaxMovimientos") Integer maxMovimientos, HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario userActual = (Usuario) session.getAttribute("jugadorActual");
        if (duracionBlackjack == null || valorAs == null || cantidadPelotas == null
                || dimensionCarton == null) {
            model.addAttribute("mensajeError", "Los datos no pueden estar vacíos");
        }
        userActual.getConfig().setDuracionBlackjack(duracionBlackjack);
        userActual.getConfig().setValorDelAs(valorAs);
        userActual.getConfig().setCantidadDePelotas(cantidadPelotas);
        userActual.getConfig().setDimensionCarton(dimensionCarton);
        userActual.getConfig().setDimensionTablero(dimensionTablero);
        userActual.getConfig().setMaxMovimientos(maxMovimientos);
        userActual.getConfig().setDuracionSenku(3);
        try {
            servicioUsuario.actualizarConfiguracionesDePartida(userActual);
            model.addAttribute("mensaje", "se actualizaron las preferencias");
        } catch (Exception e) {
            model.addAttribute("mensajeError", "no se pudieron actualizar los datos");
        }
        model.addAttribute("usuarioConfig", userActual.getConfig());
        return new ModelAndView("acceso-juegos", model);
    }

}
