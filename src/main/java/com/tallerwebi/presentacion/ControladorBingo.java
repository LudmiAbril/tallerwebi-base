package com.tallerwebi.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.ServicioBingo;
import com.tallerwebi.dominio.CartonBingo;

@Controller
public class ControladorBingo {

	private ServicioBingo servicioBingo;

	@Autowired
	public ControladorBingo(ServicioBingo servicioBingo) {
		this.servicioBingo = servicioBingo;
	}

	@RequestMapping(path = "/bingo", method = RequestMethod.GET)
	public ModelAndView irAlBingo() {
		CartonBingo carton = servicioBingo.generarCarton();
		Integer numeroCantadoAleatorio = servicioBingo.entregarNumeroAleatorio();
		ModelMap modelo = new ModelMap();
		modelo.put("carton", carton);
		modelo.put("numeroCantado", numeroCantadoAleatorio);
		return new ModelAndView("bingo", modelo);
	}

	@RequestMapping(path = "/marcarCasillero", method = RequestMethod.POST)
	public ModelAndView marcarCasillero(@RequestParam Integer numeroCantado) {
		this.servicioBingo.marcarCasillero(numeroCantado);
		return new ModelAndView("redirect:/bingo");
	}
}
