package com.tallerwebi.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Juego;
import com.tallerwebi.dominio.Partida;
import com.tallerwebi.dominio.ServicioBlackjack;
import com.tallerwebi.dominio.ServicioPlataforma;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.PartidasDelJuegoNoEncontradasException;

import java.util.List;

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

    @RequestMapping(path = "/acceso-juegos")
    public ModelAndView accesoJuegos(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
        model.addAttribute("jugador", jugador.getNombre());
        return new ModelAndView("acceso-juegos", model);
    }

    @RequestMapping(path = "/verRanking")
    public ModelAndView verRanking(@RequestParam("tipoJuego") Juego tipoJuego) {

        ModelMap mav = new ModelMap();

        Juego juego = tipoJuego;

        List<Partida> partidas;
        try {
            partidas = servicioPlataforma.generarRanking(juego);
            mav.addAttribute("partidas", partidas);
        } catch (PartidasDelJuegoNoEncontradasException e) {
            mav.addAttribute("mensajeError",
                    "todavía no hay instancias de partidas de este juego,¿por qué no las empezas?");

        }

        mav.addAttribute("nombreJuego", tipoJuego.toString());

        return new ModelAndView("ranking", mav);
    }

    @RequestMapping(path = "/volverAlMenu")
    public ModelAndView volverAlMenuDeJuegos(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
        model.addAttribute("jugador", jugador.getNombre());
        return new ModelAndView("acceso-juegos", model);
    }

    @RequestMapping(path = "/guardarCambios", method = RequestMethod.POST)
    public ModelAndView guardarCambios(@RequestParam("duracionBlackjack") Integer duracionBlackjack,
            @RequestParam("valorAs") Integer valorAs, @RequestParam("cantidadPelotas") Integer cantidadPelotas,
            @RequestParam("dimensionCarton") Integer dimensionCarton, HttpSession session) {
        // setearle al user del ususario lo que viene por campos y modificar en el repo
        Usuario userActual = (Usuario) session.getAttribute("jugadorActual");
        userActual.getConfig().setDuracionBlackjack(duracionBlackjack);
        userActual.getConfig().setValorDelAs(valorAs);
        userActual.getConfig().setCantidadDePelotas(cantidadPelotas);
        userActual.getConfig().setDimensionCarton(dimensionCarton);
        servicioUsuario.actualizarConfiguracionesDePartida(userActual);
        return new ModelAndView("acceso-juegos");
    }

}
