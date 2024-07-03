package com.tallerwebi.presentacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.NoHayCompras;
import com.tallerwebi.dominio.excepcion.PartidaConPuntajeNegativoException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ControladorBingo {

	private ServicioBingo servicioBingo;
	private ServicioPlataforma servicioPlataforma;

	@Autowired
	public ControladorBingo(ServicioBingo servicioBingo, ServicioPlataforma servicioPlataforma) {
		this.servicioBingo = servicioBingo;
		this.servicioPlataforma = servicioPlataforma;
	}

	@RequestMapping(path = "/irAlBingo", method = RequestMethod.GET)
	public ModelAndView irAlBingo() {
		ModelMap model = new ModelMap();
		model.put("nuevoJugador", new Usuario());
		return new ModelAndView("irAlBingo", model);
	}

	@RequestMapping(path = "/comenzarJuegoBingo", method = RequestMethod.GET)
	public ModelAndView comenzarJuegoBingo(@RequestParam("tipo") String tipo, HttpSession session) {

		ModelMap model = new ModelMap();
		Usuario usuario;
		if (session.getAttribute("jugadorActual") == null
				|| !(session.getAttribute("jugadorActual") instanceof Usuario)) {
			// Si no está o no es un Usuario, crea un nuevo Usuario y ponlo en la sesión
			usuario = new Usuario();
			usuario.setNombre("user");
			session.setAttribute("jugadorActual", usuario);
		} else {
			// Si está, recupéralo de la sesión
			usuario = (Usuario) session.getAttribute("jugadorActual");
		}

		session.setAttribute("tiradaLimiteDeLaSesion", usuario.getConfig().getCantidadDePelotas());
		session.setAttribute("dimensionDelCartonDeLaSesion", usuario.getConfig().getDimensionCarton());

		Integer dimensionDelCartonDeLaSesion = (Integer) session.getAttribute("dimensionDelCartonDeLaSesion");
		CartonBingo carton = servicioBingo.generarCarton(dimensionDelCartonDeLaSesion);
		session.setAttribute("carton", carton);
		Set<Integer> numerosEntregados = new LinkedHashSet<Integer>();
		Integer numeroNuevo = this.servicioBingo.entregarNumeroAleatorio(numerosEntregados);
		Integer numeroCantadoAleatorio = numeroNuevo;
		numerosEntregados.add(numeroNuevo);
		session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);
		session.setAttribute("numeroAleatorioCantado", numeroCantadoAleatorio);
		String nombreJugador = usuario.getNombre();
		model.put("nombreJugador", nombreJugador);

		session.setAttribute("nombreJugador", nombreJugador);

		TipoPartidaBingo tipoPartidaBingo = TipoPartidaBingo.valueOf(tipo.toUpperCase());
		session.setAttribute("tipoPartidaBingo", tipoPartidaBingo);

		TipoPartidaBingo tipoPartidaBingoDeLaSesion = (TipoPartidaBingo) session.getAttribute("tipoPartidaBingo");
		model.put("tipoPartidaBingoDeLaSesion", tipoPartidaBingoDeLaSesion);

		List<Compra> compras;
		Long idUsuario = ((Usuario) session.getAttribute("jugadorActual")).getId();
		try {
			compras = this.servicioPlataforma.obtenerCompras(idUsuario, Juego.BINGO);
			model.addAttribute("compras", compras);
		} catch (NoHayCompras e) {
			model.addAttribute("mensajeErrorCompra", "¡Todavía no compraste nada!");
		}
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
		TipoPartidaBingo tipoPartidaBingo = (TipoPartidaBingo) session.getAttribute("tipoPartidaBingo");

		Integer tirada = (Integer) session.getAttribute("tiradaLimiteDeLaSesion");
		Integer numerosRestantesParaCompletarLaTirada = this.servicioBingo
				.obtenerCantidadDeNumerosRestantesParaCompletarLaTirada(tirada,
						numerosEntregados.size());

		session.setAttribute("numerosRestantesParaCompletarLaTiradaDeLaSesion",
				numerosRestantesParaCompletarLaTirada);

		Map<String, Object> respuesta = new HashMap<>();
		respuesta.put("carton", carton);
		respuesta.put("numeroAleatorioCantado", numeroCantadoAleatorio);
		respuesta.put("tipoPartidaBingo", tipoPartidaBingo);
		respuesta.put("numerosRestantesParaCompletarLaTirada",
				numerosRestantesParaCompletarLaTirada);
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
	public Map<String, Object> obtenerNuevoNumero(HttpSession session) throws PartidaConPuntajeNegativoException {
		Set<Integer> numerosEntregados = (Set<Integer>) session.getAttribute("numerosEntregadosDeLaSesion");
		Integer tiradaLimiteDeLaSesion = (Integer) session.getAttribute("tiradaLimiteDeLaSesion");
		Boolean limiteAlcanzado = false;
		Map<String, Object> respuesta = new HashMap<>();
		this.servicioBingo.obtenerTirada(tiradaLimiteDeLaSesion);
		if (numerosEntregados == null) {
			numerosEntregados = new LinkedHashSet<>();
			session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);
		}
		if (numerosEntregados.size() == tiradaLimiteDeLaSesion) {
			session.setAttribute("seHizoLinea", false);
			session.setAttribute("seHizoBingo", false);
			limiteAlcanzado = true;
			respuesta.put("limiteAlcanzado", limiteAlcanzado);
		} else {
			Integer nuevoNumero = servicioBingo.entregarNumeroAleatorio(numerosEntregados);
			// Integer tirada = (Integer) session.getAttribute("tiradaLimiteDeLaSesion");
			Integer numerosRestantesParaCompletarLaTirada = this.servicioBingo
					.obtenerCantidadDeNumerosRestantesParaCompletarLaTirada(tiradaLimiteDeLaSesion,
							numerosEntregados.size());
			numerosEntregados.add(nuevoNumero);
			session.setAttribute("numeroAleatorioCantado", nuevoNumero);
			session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);

			respuesta.put("nuevoNumero", nuevoNumero);
			respuesta.put("limiteAlcanzado", false);
			respuesta.put("numerosRestantesParaCompletarLaTirada",
					numerosRestantesParaCompletarLaTirada);
		}
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
		Integer dimension = (Integer) session.getAttribute("dimensionDelCartonDeLaSesion");
		Boolean seHizoBingo = this.servicioBingo.bingo(numerosMarcadosDeLaSesion, dimension);
		session.setAttribute("seHizoBingo", seHizoBingo);
		session.setAttribute("seHizoLinea", false);
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

	@RequestMapping(path = "/linea", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> hacerlinea(HttpSession session) {
		Set<Integer> numerosMarcadosDeLaSesion = (Set<Integer>) session.getAttribute("numerosMarcadosDeLaSesion");
		CartonBingo cartonDeLaSesion = (CartonBingo) session.getAttribute("carton");
		Boolean seHizoLinea = this.servicioBingo.linea(numerosMarcadosDeLaSesion, cartonDeLaSesion);
		session.setAttribute("seHizoLinea", seHizoLinea);
		session.setAttribute("seHizoBingo", false);
		Map<String, Object> respuesta = new HashMap<String, Object>();
		respuesta.put("seHizoLinea", seHizoLinea);
		return respuesta;
	}

	@RequestMapping(path = "/finalizarPartida", method = RequestMethod.POST)
	public ModelAndView finalizar(HttpSession session) throws PartidaConPuntajeNegativoException,
			IllegalArgumentException {
		ModelAndView mav = new ModelAndView();
		Set<Integer> numerosMarcadosDeLaSesion = (Set<Integer>) session.getAttribute("numerosMarcadosDeLaSesion");
		Boolean seHizoLinea = (Boolean) session.getAttribute("seHizoLinea");
		Boolean seHizoBingo = (Boolean) session.getAttribute("seHizoBingo");
		TipoPartidaBingo tipoPartidaBingoDeLaSesion = (TipoPartidaBingo) session
				.getAttribute("tipoPartidaBingo");
		Integer tiradaLimiteDeLaSesion = (Integer) session.getAttribute("tiradaLimiteDeLaSesion");
		Usuario jugador = (Usuario) session.getAttribute("jugadorActual");
		Integer cantidadDeCasillerosMarcados = numerosMarcadosDeLaSesion.size();

		try {
			servicioPlataforma
					.agregarPartida(
							new PartidaBingo(jugador.getId(), Juego.BINGO, numerosMarcadosDeLaSesion, seHizoLinea,
									seHizoBingo,
									tipoPartidaBingoDeLaSesion, tiradaLimiteDeLaSesion, cantidadDeCasillerosMarcados,
									false));
			mav.setViewName("redirect:/irAlBingo");
		} catch (Exception e) {
			mav.setViewName("bingo");
			mav.addObject("mensajeError", "Ocurrió un error al intentar guardar la partida.");
		}

		return mav;
	}

	// esperen de nuvo xddd sorryyyyy

	
	@PostMapping("/reiniciarTirada/{tirada}")
    public Map<String, Object> reiniciarTirada(@PathVariable("tirada") String tirada, HttpSession session) throws PartidaConPuntajeNegativoException {
        session.setAttribute("tiradaLimiteDeLaSesion", tirada);
        this.obtenerNuevoNumero(session);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Tirada reiniciada correctamente");
        return response;
    }
}
