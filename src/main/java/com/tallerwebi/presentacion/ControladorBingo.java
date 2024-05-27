package com.tallerwebi.presentacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@RequestMapping(path = "/comenzarJuegoBingo", method = RequestMethod.POST)
	public ModelAndView comenzarJuegoBingo(@ModelAttribute("nuevoJugador") Jugador nuevoJugador, HttpSession session) {
		CartonBingo carton = servicioBingo.generarCarton();
		Set<Integer> numerosEntregados = new LinkedHashSet<Integer>();
		Integer numeroNuevo = this.servicioBingo.entregarNumeroAleatorio(numerosEntregados);
		Integer numeroCantadoAleatorio = numeroNuevo;
		numerosEntregados.add(numeroNuevo);
		session.setAttribute("carton", carton);
		session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);
		session.setAttribute("numeroAleatorioCantado", numeroCantadoAleatorio);
		ModelMap model = new ModelMap();
		String nombreJugador = nuevoJugador.getNombre();
		model.put("nombreJugador", nombreJugador);
		session.setAttribute("nombreJugador", nombreJugador);
		return new ModelAndView("bingo", model);
	}

	@RequestMapping(path = "/obtenerDatosIniciales", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> obtenerDatosIniciales(HttpSession session) {
		CartonBingo carton = (CartonBingo) session.getAttribute("carton");
		Integer numeroCantadoAleatorio = (Integer) session.getAttribute("numeroAleatorioCantado");
		Set<Integer> numerosEntregados = (Set<Integer>) session.getAttribute("numerosEntregadosDeLaSesion");
		if (numerosEntregados == null) {
			numerosEntregados = new LinkedHashSet<>();
			session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);
		}
		numerosEntregados.add(numeroCantadoAleatorio);
		session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);

		HashSet<Integer> numerosMarcadosDeLaSesion = new HashSet<Integer>();
		session.setAttribute("numerosMarcadosDeLaSesion", numerosMarcadosDeLaSesion);

		Map<String, Object> respuesta = new HashMap<>();
		respuesta.put("carton", carton);
		respuesta.put("numeroAleatorioCantado", numeroCantadoAleatorio);
		return respuesta;
	}

	@RequestMapping(path = "/marcarCasillero/{numeroCasillero}", method = RequestMethod.POST)
	@ResponseBody
	public void marcarCasillero(@PathVariable Integer numeroCasillero, HttpSession session) {
		CartonBingo carton = (CartonBingo) session.getAttribute("carton");
		Integer numeroCantado = (Integer) session.getAttribute("numeroAleatorioCantado");

		servicioBingo.marcarCasillero(numeroCasillero, carton);

		Set<Integer> numerosMarcadosDeLaSesion = (Set<Integer>) session.getAttribute("numerosMarcadosDeLaSesion");
		numerosMarcadosDeLaSesion.add(numeroCasillero);
		session.setAttribute("numerosMarcadosDeLaSesion", numerosMarcadosDeLaSesion);

	}

	@RequestMapping(path = "/obtenerNuevoNumero", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Integer> obtenerNuevoNumero(HttpSession session) {
		Set<Integer> numerosEntregados = (Set<Integer>) session.getAttribute("numerosEntregadosDeLaSesion");
		if (numerosEntregados == null) {
			numerosEntregados = new LinkedHashSet<>();
			session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);
		}
		Integer nuevoNumero = servicioBingo.entregarNumeroAleatorio(numerosEntregados);
		numerosEntregados.add(nuevoNumero);
		session.setAttribute("numeroAleatorioCantado", nuevoNumero);
		session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);
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

	@RequestMapping(path = "/bingo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> hacerBingo(HttpSession session) {
		Set<Integer> numerosMarcadosDeLaSesion = (Set<Integer>) session.getAttribute("numerosMarcadosDeLaSesion");
		Boolean seHizoBingo = this.servicioBingo.bingo(numerosMarcadosDeLaSesion);
		Map<String, Object> respuesta = new HashMap<String, Object>();
		respuesta.put("seHizoBingo", seHizoBingo);
		return respuesta;
	}

	@RequestMapping(path = "/obtenerLosNumerosEntregados", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> obtenerLosNumerosEntregados(HttpSession session) {
		Set<Integer> numerosEntregadosDeLaSesion = (Set<Integer>) session.getAttribute("numerosEntregadosDeLaSesion");
		Map<String, Object> respuesta = new HashMap<String, Object>();
		respuesta.put("numerosEntregadosDeLaSesion", numerosEntregadosDeLaSesion);
		return respuesta;
	}

	@RequestMapping(path = "/obtenerLosNumerosMarcados", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> obtenerLosNumerosMarcados(HttpSession session) {
		Set<Integer> numerosMarcadosDeLaSesion = (Set<Integer>) session.getAttribute("numerosMarcadosDeLaSesion");
		Map<String, Object> respuesta = new HashMap<String, Object>();
		respuesta.put("numerosMarcadosDeLaSesion", numerosMarcadosDeLaSesion);
		return respuesta;
	}

	@RequestMapping(path = "/obtenerUltimoNumeroEntregado", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> obtenerUltimoNumeroEntregado(HttpSession session) {
		Integer ultimoNumeroEntregado = (Integer) session.getAttribute("numeroAleatorioCantado");
		Map<String, Object> respuesta = new HashMap<String, Object>();
		respuesta.put("ultimoNumeroEntregado", ultimoNumeroEntregado);
		return respuesta;
	}

	@RequestMapping(path = "/obtenerCincoUltimosNumerosEntregados", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> obtenerCincoUltimosNumerosEntregados(HttpSession session) {
		Set<Integer> numerosEntregadosDeLaSesion = (Set<Integer>) session.getAttribute("numerosEntregadosDeLaSesion");

		if (numerosEntregadosDeLaSesion == null) {
			numerosEntregadosDeLaSesion = new LinkedHashSet<>();
			session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregadosDeLaSesion);
		}

		List<Integer> ultimosNumeros = new ArrayList<>(numerosEntregadosDeLaSesion);

		// Obtener los últimos 5 números, si hay menos de 5, devolver todos
		int startIndex = Math.max(0, ultimosNumeros.size() - 5);
		List<Integer> numerosParaMostrar = ultimosNumeros.subList(startIndex, ultimosNumeros.size());

		Map<String, Object> respuesta = new HashMap<>();
		respuesta.put("ultimosNumerosEntregados", numerosParaMostrar);
		return respuesta;
	}

// 	@RequestMapping(path = "/elegirTipoPartidaBingo", method = RequestMethod.GET)
// 	@ResponseBody
// 	public Map<String, Object> elegirTipoPartidaBingo(HttpSession session){
// // cuando haces click en jugar, te sale un pop up que te dice linea, bingo, ambos.
// // eso va a ser un form, y lo que recibo por request param, lo guardo en el map
// // en el js, recupero esa data, es decir, lo q llega por el form. Ahi, segun lo q llega, muestro el boton de linea, el boton de bingo o ambos botones.
// 	}

}
