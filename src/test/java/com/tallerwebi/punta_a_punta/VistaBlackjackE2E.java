package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.*;
import com.tallerwebi.punta_a_punta.vistas.VistaAccesoAjuegos;
import org.junit.jupiter.api.*;
import com.tallerwebi.punta_a_punta.vistas.VistaLogin;
import com.tallerwebi.punta_a_punta.vistas.VistaHome;
import com.tallerwebi.punta_a_punta.vistas.VistaBlackjack;

import java.util.Arrays;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class VistaBlackjackE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaHome vistaHome;
    VistaLogin vistaLogin;
    VistaAccesoAjuegos vistaAccesoAjuegos;
    VistaBlackjack vistaBlackjack;
    Page page;

    @BeforeAll
    static void abrirNavegador() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
    }

    @AfterAll
    static void cerrarNavegador() {
        playwright.close();
    }

    @BeforeEach
    void crearContextoYPagina() {
        context = browser.newContext();
        page = context.newPage();
        vistaHome = new VistaHome(page);
        vistaLogin = new VistaLogin(page);
        vistaAccesoAjuegos = new VistaAccesoAjuegos(page);
        vistaBlackjack = new VistaBlackjack(page);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }
    @Test
    void deberiaJugarBlackjackDesdeInicio() {
        vistaAccesoAjuegos.irAccesoAjuegos("mm@gmail.com", "boca");
        vistaAccesoAjuegos.darClick("#accederBj");
        vistaBlackjack.darClick("#jugarBlackjack");
        String url = page.url();
        assertThat(url, containsStringIgnoringCase("/spring/blackjack"));
    }


  @Test
    void deberiaPedirCartaYMostrarEnInterfaz() {
      vistaBlackjack.accederYJugarBlackjack("mm@gmail.com", "boca");
      vistaBlackjack.darClick("#pedirCarta");
      boolean cartasDelJugador=vistaBlackjack.isVisible("#cartasJugador .carta");
        // Verificar que las cartas del jugador se muestran
        assertThat(cartasDelJugador, is(true));
    }

    @Test
    void deberiaMostrarLasCartasDelCroupierCuandoIniciaElJuego() {
        vistaBlackjack.accederYJugarBlackjack("mm@gmail.com", "boca");
        page.waitForSelector("#cartasCasa .carta");
        boolean  cartasDelCroupier= vistaBlackjack.isVisible("#cartasCasa .carta");
        assertThat(cartasDelCroupier, is(true));
    }


    @Test
    void deberiaPlantarseYMostrarResultado() {
        vistaBlackjack.accederYJugarBlackjack("mm@gmail.com", "boca");
        vistaBlackjack.darClick("#plantarse");
        boolean resultadoPartida = vistaBlackjack.modalResultadoDePartidaBlackjackEstaVisible();
        // Verificar que el resultado de la partida se muestra
        assertThat(resultadoPartida, is(true));
    }

    @Test
    void deberiaMostrarCronometroAlInicio() {
        vistaBlackjack.accederYJugarBlackjack("mm@gmail.com", "boca");
        // Esperar a que el cronómetro esté visible
        page.waitForSelector("#tiempoBj");
        assertThat(vistaBlackjack.isVisible("#tiempoBj"), is(true));

        String tiempoTranscurrido = page.locator("#tiempoBj").textContent().trim();
        assertThat(tiempoTranscurrido, not(equalTo("00:00:00")));
    }



    @Test
    void deberiaAbandonarJuegoCorrectamente() {
        vistaBlackjack.accederYJugarBlackjack("mm@gmail.com", "boca");
        vistaBlackjack.darClick("#openCustomExitModal");

        assertThat(vistaBlackjack.isVisible("#customExitModal"), is(true));
        vistaBlackjack.darClick("#customExitModal .boton-abandonar-custom");

        String url = page.url();
        assertThat(url, containsStringIgnoringCase("/volverAlMenu"));
    }

    @Test
    void deberiaReiniciarJuegoCorrectamente() {
        vistaBlackjack.accederYJugarBlackjack("mm@gmail.com", "boca");
        vistaBlackjack.darClick("#plantarse");
        boolean resultadoPartida = vistaBlackjack.modalResultadoDePartidaBlackjackEstaVisible();

        // Verificar que el modal de finalización de partida se hace visible
        assertThat(resultadoPartida, is(true));
        vistaBlackjack.darClick("#modalFinPartida .boton-reiniciar");
        String url = page.url();
        assertThat(url, containsStringIgnoringCase("/spring/reiniciarBlackjack"));
    }


    @Test
    void deberiaMostrarPuntajeInicialDeLaManoDelJugador() {
        vistaBlackjack.accederYJugarBlackjack("mm@gmail.com", "boca");
        page.waitForSelector("#puntaje");

        // Verificar que el texto contiene "Tu mano: " seguido de un número
        String puntajeTexto = page.locator(".puntaje-mano").textContent().trim();
        String textoEsperado = "Tu mano: \\d+";
        assertThat(puntajeTexto.matches(textoEsperado), is(true));
    }

    @Test
    void deberiaMostrarElHistorialVacioDeLJugadorSiAunNoTienePartidasJugadas() {
        vistaBlackjack.accederYJugarBlackjack("mm2@gmail.com", "1212");
        String historialTexto = page.locator(".mensaje-partidas").textContent().trim();
        String textoEsperado = "aun no hay partidas registradas.";
        assertThat(historialTexto.matches(textoEsperado), is(true));
    }

    @Test
    void deberiaMostrarUnaPartidaEnElHistorial() {
        vistaBlackjack.accederYJugarBlackjack("mm@gmail.com", "boca");
        String fecha = "2024-07-04 17:00:36";
        int puntaje = 20;

        // Construir el contenido esperado
        String contenidoEsperado = fecha + " puntaje alcanzado: " + puntaje;

        // Obtener el contenido del historial de partidas
        String historialContenido = page.locator(".c-partidas").textContent().trim();

        // Dividir las partidas por el patrón de fechas
        String[] partidasArray = historialContenido.split("(?=\\d{4}-\\d{2}-\\d{2})");

        boolean partidaEncontrada = Arrays.stream(partidasArray)
                .map(String::trim)
                .anyMatch(partida -> partida.equals(contenidoEsperado));

        assertThat(partidaEncontrada, equalTo(true));
    }

    @Test
    void deberiaIrAlRankingDelBlackjack() {
        vistaAccesoAjuegos.irAccesoAjuegos("mm@gmail.com", "boca");
        vistaAccesoAjuegos.darClick("#accederBj");
        vistaBlackjack.darClick("#rankingBlackjack");
        String url = page.url();

        String subtituloBj = page.locator("span.subtitulojuego").first().textContent().trim();
        String textoEsperado = "BLACKJACK";

        assertThat(url, containsStringIgnoringCase("/spring/verRanking"));
        assertThat(subtituloBj.matches(textoEsperado), is(true));

    }

    @Test
    void deberiaMostrarElModalDeTiempoSiLoSeleccionas() {
        vistaAccesoAjuegos.irAccesoAjuegos("mm@gmail.com", "boca");
        vistaAccesoAjuegos.darClick("#accederBj");
        vistaBlackjack.darClick("#playWithTimeButton");
       boolean modalTiempo= vistaBlackjack.isVisible("#timeModal");
       assertThat(modalTiempo, is(true));

    }

    @Test
    void deberiaIngresarAlBlackjacjCuandoIngresoUnTiempo() {
        vistaAccesoAjuegos.irAccesoAjuegos("mm@gmail.com", "boca");
        vistaAccesoAjuegos.darClick("#accederBj");
        vistaBlackjack.darClick("#playWithTimeButton");
        vistaBlackjack.ingresarTiempoLimiteBlackjack("1");
        vistaBlackjack.darClick("#timeModal .btn");

        String url = page.url();
        assertThat(url, containsStringIgnoringCase("/spring/blackjack"));

    }




}
