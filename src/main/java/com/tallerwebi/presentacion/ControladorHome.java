package com.tallerwebi.presentacion;



import org.springframework.stereotype.Controller;
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

}
