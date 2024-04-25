package com.tallerwebi.presentacion;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

public class ControladorBingo {
    @RequestMapping(path="/bingo", method= RequestMethod.GET)
	public ModelAndView irAlBingo() {
		return new ModelAndView("bingo");
	}
}
