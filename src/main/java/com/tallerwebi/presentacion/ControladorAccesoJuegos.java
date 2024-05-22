package com.tallerwebi.presentacion;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Juego;
import com.tallerwebi.dominio.Partida;
import com.tallerwebi.dominio.ServicioBlackjack;
import com.tallerwebi.dominio.ServicioPlataforma;
import com.tallerwebi.dominio.excepcion.PartidasDelJuegoNoEncontradasException;

import java.util.List;


@Controller
public class ControladorAccesoJuegos {

    @RequestMapping(path = "/acceso-juegos")
    public ModelAndView accesoJuegos() {

        return new ModelAndView("acceso-juegos");
    }
 //MISMO CONTROLADOR PARA EL TEMA DEL RANKING
    private ServicioPlataforma servicioPlataforma;
    @Autowired
     public ControladorAccesoJuegos(ServicioPlataforma servicioPlataforma) {
      
        this.servicioPlataforma = servicioPlataforma;
    }
    @RequestMapping(path = "/verRanking" )
    public ModelAndView verRanking(@RequestParam("tipoJuego") Juego tipoJuego ) {
     
        
       ModelMap mav = new ModelMap();

        Juego juego = tipoJuego;
    
    
        List<Partida> partidas;
        try {
            partidas = servicioPlataforma.generarRanking(juego);
            mav.addAttribute("partidas", partidas);
        } catch (PartidasDelJuegoNoEncontradasException e) {
            mav.addAttribute("mensajeError","todavía no hay instancias de partidas de este juego,¿por qué no las empezas?");
           
        }
        
        mav.addAttribute("nombreJuego",tipoJuego.toString());
    
        return new ModelAndView("ranking",mav);
    }
    
}
