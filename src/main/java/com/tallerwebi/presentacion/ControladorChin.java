package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorChin {

    public ModelAndView irAlChin() {
        String viewName = "chin";

        ModelMap model = new ModelMap();
        model.put("message", "Bienvenido al Chin");

        return new ModelAndView(viewName, model);
    }
}
