package com.tallerwebi.presentacion;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import javax.servlet.http.HttpSession;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.PartidaConPuntajeNegativoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.ServicioBingo;
import com.tallerwebi.dominio.ServicioPlataforma;
import com.tallerwebi.dominio.TipoPartidaBingo;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.CartonBingo;
import com.tallerwebi.dominio.PartidaBingo;


@RestController
public class ControladorBingo {

    private final BingoManager bingoManager = new BingoManager();
	private ServicioBingo servicioBingo;
	private ServicioPlataforma servicioPlataforma;
    private SimpMessagingTemplate template;
	private Set<WebSocketSession> sessions = new HashSet<>();
    private final List<String> nombresJugadores = new ArrayList<>();
	private Map<WebSocketSession, HttpSession> webSocketToHttpSessionMap = new HashMap<>();
	private SalaEspera salaDeEspera = new SalaEspera();

	@Autowired
	public ControladorBingo(ServicioBingo servicioBingo, ServicioPlataforma servicioPlataforma, SimpMessagingTemplate template) {
		this.servicioBingo = servicioBingo;
		this.servicioPlataforma = servicioPlataforma;
		this.template = template;
    }

	@RequestMapping(path = "/irAlBingo", method = RequestMethod.GET)
	public ModelAndView irAlBingo(HttpSession session) {
		ModelMap model = new ModelMap();
		model.put("nuevoJugador", new Usuario());
		model.put("nombreJugador", new Usuario().getNombre());
		String sessionId = session.getId();
		Usuario user = (Usuario) session.getAttribute("jugadorActual");
		if (user != null) {
			model.put("nombreUsuario", user.getNombre());
		} else {
			// Si no hay nombre de usuario en la sesión, puedes manejarlo aquí
			model.put("nombreUsuario", "Invitado");
		}
		return new ModelAndView("irAlBingo", model);
	}

	@RequestMapping(path = "/bingo-multijugador", method = RequestMethod.GET)
	public ModelAndView jugarMultijugador() {
		ModelMap model = new ModelMap();
		model.put("nuevoJugador", new Usuario());

		return new ModelAndView("bingo-multijugador", model);
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

		Set<Integer> numerosEntregados = new LinkedHashSet<Integer>();
		Integer numeroNuevo = this.servicioBingo.entregarNumeroAleatorio(numerosEntregados);
		Integer numeroCantadoAleatorio = numeroNuevo;
		numerosEntregados.add(numeroNuevo);
		session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);
		session.setAttribute("numeroAleatorioCantado", numeroCantadoAleatorio);

		session.setAttribute("carton", carton);

		String nombreJugador = usuario.getNombre();
		model.put("nombreJugador", nombreJugador);

		session.setAttribute("nombreJugador", nombreJugador);

		TipoPartidaBingo tipoPartidaBingo = TipoPartidaBingo.valueOf(tipo.toUpperCase());
		session.setAttribute("tipoPartidaBingo", tipoPartidaBingo);

		TipoPartidaBingo tipoPartidaBingoDeLaSesion = (TipoPartidaBingo) session.getAttribute("tipoPartidaBingo");
		model.put("tipoPartidaBingoDeLaSesion", tipoPartidaBingoDeLaSesion);
		return new ModelAndView("bingo", model);
		// get config. get cantidad de bolas. guardar en la sesion.
	}




	@RequestMapping(path = "/obtenerEstadoPartida", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> obtenerEstadoPartida(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        BingoMultijugador partida = (BingoMultijugador) session.getAttribute("partidaMultijugador");
        if (partida != null) {
            response.put("nombreJugador2", partida.getNombreJugador2());
        } else {
            response.put("nombreJugador2", null);
        }
        return response;
    }
    private int jugadoresConectados = 0;

	@MessageMapping("/bingo-multijugador/connect")
	public void manejarConexion(Map<String, String> jugador, WebSocketSession session, HttpSession httpSession) {
		String nombreJugador = jugador.get("nombreJugador");
		Usuario usuario = (Usuario) httpSession.getAttribute("jugadorActual");
		usuario.setNombre(nombreJugador);
		nombresJugadores.add(nombreJugador);
		httpSession.setAttribute("webSocketSession", session);

		// Añadir la sesión webSocket a la lista de sesiones
		synchronized (sessions) {
			sessions.add(session);
			webSocketToHttpSessionMap.put(session, httpSession);
		}

		//template.convertAndSend("/topic/estadoJugadores", nombresJugadores);
		if (sessions.size() == 2) {
			List<Usuario> jugadores = new ArrayList<>();
			for (WebSocketSession sessionx : sessions) {
				HttpSession httpSessionTemp = webSocketToHttpSessionMap.get(session);
				Usuario jugadorx = (Usuario) httpSessionTemp.getAttribute("jugadorActual");
				jugadores.add(jugadorx);
			}
			if (jugadores.size() == 2) {
				iniciarPartida(jugadores.get(0), jugadores.get(1), httpSession);
			}
		}
	}
	@MessageMapping("/bingo-multijugador/disconnect")
	public void manejarDesconexion(WebSocketSession session) {
		synchronized (sessions) {
			sessions.remove(session);
			HttpSession httpSession = webSocketToHttpSessionMap.remove(session);
			if (httpSession != null) {
				Usuario usuario = (Usuario) httpSession.getAttribute("jugadorActual");
				if (usuario != null) {
					nombresJugadores.remove(usuario.getNombre());
				}
			}
		}
		template.convertAndSend("/topic/estadoJugadores", nombresJugadores);
	}
	private void iniciarPartida(Usuario jugador1, Usuario jugador2, HttpSession httpsession) {
		int dimensionJugador1 = jugador1.getConfig().getCantidadDePelotas();
		int dimensionJugador2 = jugador2.getConfig().getCantidadDePelotas();

		int dimension;
		if (dimensionJugador1 == dimensionJugador2) {
			dimension = dimensionJugador1;
		} else {
			dimension = Math.min(dimensionJugador1, dimensionJugador2);
		}
		BingoMultijugador partida = new BingoMultijugador();

		partida.setNombreJugador(jugador1.getNombre());
		partida.setNombreJugador2(jugador2.getNombre());
		partida.setGameState(EstadoJuego.PLAYER1_TURN);

		partida.setCartonJugador1(servicioBingo.generarCarton(dimension));
		partida.setCartonJugador2(servicioBingo.generarCarton(dimension));
		template.convertAndSend("/topic/updates", partida);
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

		Map<String, Object> respuesta = new HashMap<>();
		respuesta.put("carton", carton);
		respuesta.put("numeroAleatorioCantado", numeroCantadoAleatorio);
		respuesta.put("tipoPartidaBingo", tipoPartidaBingo);
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
		if (tiradaLimiteDeLaSesion == null) {
			respuesta.put("error", "La tirada límite de la sesión no está establecida.");
			return respuesta;
		}
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
			numerosEntregados.add(nuevoNumero);
			session.setAttribute("numeroAleatorioCantado", nuevoNumero);
			session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);
			respuesta.put("nuevoNumero", nuevoNumero);
			respuesta.put("limiteAlcanzado", false);
		}
		return respuesta;
	}
	@RequestMapping(path = "/sala-espera", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView salaEspera(HttpSession session) {
		ModelMap model = new ModelMap();
		Usuario jugador = (Usuario) session.getAttribute("jugadorActual");

		synchronized (salaDeEspera) {
			if (jugador != null && !salaDeEspera.getJugadores().contains(jugador)) {
				salaDeEspera.agregarJugador(jugador);
			}

			// Verificar si hay dos jugadores y comenzar la partida
			if (salaDeEspera.hayDosJugadores()) {
				salaDeEspera.setPartidaIniciada(true);
				iniciarPartida(salaDeEspera.getJugador1(), salaDeEspera.getJugador2(), session);
			}
		}

		model.addAttribute("nombreUsuario", jugador != null ? jugador.getNombre() : "Invitado");
		model.addAttribute("usuarioConfig", jugador != null ? jugador.getConfig() : null);

		return new ModelAndView("sala-espera", model);
	}

	@RequestMapping(path = "/obtenerEstadoJugadores", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> obtenerEstadoJugadores(HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		response.put("jugadores", nombresJugadores);
		return response;
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
	public ModelAndView finalizar(HttpSession session) throws PartidaConPuntajeNegativoException {

		Set<Integer> numerosMarcadosDeLaSesion = (Set<Integer>) session.getAttribute("numerosMarcadosDeLaSesion");
		Boolean seHizoLinea = (Boolean) session.getAttribute("seHizoLinea");
		Boolean seHizoBingo = (Boolean) session.getAttribute("seHizoBingo");
		TipoPartidaBingo tipoPartidaBingoDeLaSesion = (TipoPartidaBingo) session
				.getAttribute("tipoPartidaBingoDeLaSesion");
		Integer tiradaLimiteDeLaSesion = (Integer) session.getAttribute("tiradaLimiteDeLaSesion");
		Usuario jugador = (Usuario) session.getAttribute("jugadorActual");

		servicioPlataforma
				.agregarPartida(new PartidaBingo(jugador.getId(), Juego.BINGO, numerosMarcadosDeLaSesion, seHizoLinea,
						seHizoBingo,
						tipoPartidaBingoDeLaSesion, tiradaLimiteDeLaSesion));

		return new ModelAndView("redirect:/acceso-juegos");
	}

	@MessageMapping("/bingo/movimiento")
	@SendTo("/topic/updates")
	public Map<String, Object> realizarMovimiento(@RequestBody MovimientoRequest movimientoRequest, HttpSession session) {
		BingoMultijugador partida = (BingoMultijugador) session.getAttribute("partidaMultijugador");
		//partida.realizarMovimiento(movimientoRequest.getJugador(), movimientoRequest.getMovimiento());

		// Obtener los números entregados
		Set<Integer> numerosEntregados = (Set<Integer>) session.getAttribute("numerosEntregadosDeLaSesion");

		// Crear la respuesta con el estado de la partida y los números entregados
		Map<String, Object> response = new HashMap<>();
		response.put("estadoPartida", partida);
		response.put("numerosEntregados", numerosEntregados);

		// Enviar el mensaje con los números entregados
		template.convertAndSend("/topic/numeros", numerosEntregados);

		return response;
	}


}
