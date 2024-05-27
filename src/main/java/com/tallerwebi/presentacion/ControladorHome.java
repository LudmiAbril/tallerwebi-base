package com.tallerwebi.presentacion;



import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorHome {


    @RequestMapping(path = "/home")
    public ModelAndView inicioHome() {

        return new ModelAndView("home");
    }
      @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio(){
        return new ModelAndView("redirect:/home");
    }

    @RequestMapping("/registro")
    public ModelAndView irARegistrarme() {

        ModelMap modelo = new ModelMap();
        modelo.put("datosRegistro", new DatosLogin());
        return new ModelAndView("registro", modelo);
    }

}
