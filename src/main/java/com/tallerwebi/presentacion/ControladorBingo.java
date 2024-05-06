package com.tallerwebi.presentacion;

import java.util.HashMap;
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

import com.tallerwebi.dominio.ServicioBingo;
import com.tallerwebi.dominio.CartonBingo;
import com.tallerwebi.dominio.Jugador;

@Controller
public class ControladorBingo {

	private ServicioBingo servicioBingo;

	@Autowired
	public ControladorBingo(ServicioBingo servicioBingo) {
		this.servicioBingo = servicioBingo;
	}

	@RequestMapping(path = "/irAlBingo", method = RequestMethod.GET)
	public ModelAndView irAlBingo() {
		ModelMap model = new ModelMap();
		model.put("nuevoJugador", new Jugador());
		return new ModelAndView("irAlBingo", model);
	}

	// cuando se solicite la vista ir al bingo con el metodo get se va a guardar en
	// el modelo un nuevo jugador y la key nuevoJugador. La key es recuperada por
	// thymeleaf para renderizar los datos. Se devuelve un model and view con la
	// vista irAlBingo y el modelo que contiene el nuevo jugador y la key.

	@RequestMapping(path = "/comenzarJuegoBingo", method = RequestMethod.POST)
	public ModelAndView comenzarJuegoBingo(@ModelAttribute("nuevoJugador") Jugador nuevoJugador, HttpSession session) {
		CartonBingo carton = servicioBingo.generarCarton();
		Integer numeroCantadoAleatorio = servicioBingo.entregarNumeroAleatorio();
		// GUARDANDO DATOS DE SESION!!!!!!!!!
		session.setAttribute("carton", carton);
		session.setAttribute("numeroAleatorioCantado", numeroCantadoAleatorio);
		return new ModelAndView("bingo");
	}

	// una vez que se envia el form de ingresar tu nombre en la vista ir al bingo,
	// se va a ejecutar la url /comenzarJuegoBingo. El metodo recibe por param el
	// ModelAttribute ya que le llega por parametro un objeto jugador, cuyos datos
	// se ingresaron en el form de la vista irALBingo. Tambien recibe una
	// HttpSession. Esta clase crea una sesion que se usara para guardar datos
	// importantes del usuario o del juego, que se hace mediante el metodo
	// setAttribute(nombre, objeto). Al objeto se lo guarda con un nombre. Luego se
	// utiliza para recuperar ese objeto guardado.

	@RequestMapping(path = "/obtenerDatosIniciales", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> obtenerDatosIniciales(HttpSession session) {
		// recupero los datos de la sesion
		CartonBingo carton = (CartonBingo) session.getAttribute("carton");
		Integer numeroCantadoAleatorio = (Integer) session.getAttribute("numeroAleatorioCantado");
		// creo un map para la respuesta
		Map<String, Object> respuesta = new HashMap<>();
		respuesta.put("carton", carton);
		respuesta.put("numeroAleatorioCantado", numeroCantadoAleatorio);
		return respuesta;
	}

	// cuando la url sea obtenerDatosIniciales y el metodo sea get, este metodo
	// unicamente se encargara de recuperar los datos de la sesion mediante su
	// nombre, que habian sido guardados anteriormente. Estos son guardados en un
	// mapa que se entrega como respuesta JSON mediante la anotación @ResponseBody.
	// Esto permite que el cliente reciba los datos en formato JSON y los utilice
	// para actualizar dinámicamente la interfaz de usuario sin necesidad de
	// recargar toda la página.

	@RequestMapping(path = "/marcarCasillero/{numeroCasillero}", method = RequestMethod.POST)
	public ModelAndView marcarCasillero(@PathVariable Integer numeroCasillero, HttpSession session) {
		CartonBingo carton = (CartonBingo) session.getAttribute("carton");
		Integer numeroCantado = (Integer) session.getAttribute("numeroAleatorioCantado");

		if (carton != null && numeroCantado != null) {
			// Marcar el casillero en el cartón si el número coincide con el número cantado
			if (numeroCasillero.equals(numeroCantado)) {
				servicioBingo.marcarCasillero(numeroCasillero, carton);
			}
		}
		return new ModelAndView("redirect:/bingo");
	}

	@RequestMapping(path = "/obtenerNuevoNumero", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Integer> obtenerNuevoNumero(HttpSession session) {
		Integer nuevoNumero = this.servicioBingo.entregarNumeroAleatorio();
		session.setAttribute("numeroAleatorioCantado", nuevoNumero);
		Map<String, Integer> respuesta = new HashMap<>();
		respuesta.put("nuevoNumero", nuevoNumero);
		return respuesta;
	}

	@RequestMapping(path = "/obtenerNumeroActual", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Integer> obtenerNumeroActual(HttpSession session) {
		Integer numeroActual = (Integer) session.getAttribute("numeroAleatorioCantado");
		Map<String, Integer> respuesta = new HashMap<>();
		respuesta.put("numeroActual", numeroActual);
		return respuesta;
	}
	

}
