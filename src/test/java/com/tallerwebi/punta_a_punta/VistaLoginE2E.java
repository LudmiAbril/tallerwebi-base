package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.*;
import com.tallerwebi.punta_a_punta.vistas.VistaLogin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class VistaLoginE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaLogin vistaLogin;
    Page page;

    @BeforeAll
    static void abrirNavegador() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        // browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
    }

    @AfterAll
    static void cerrarNavegador() {
        playwright.close();
    }

    @BeforeEach
    void crearContextoYPagina() {
        context = browser.newContext();
        page = context.newPage();
        vistaLogin = new VistaLogin(page);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void deberiaDecirBienvenidoDeNuevoJefe() {
        String texto = vistaLogin.obtenerTexto("#texto1");
        assertThat("¡Bienvenido de nuevo, jefe!", equalToIgnoringCase(texto));
    }

    // que se muestre el mensaje de error
    // @Test
    // void deberiaDarUnErrorAlNoCompletarTodasLasConfiguracionesYTocarElBoton() {
    //     page.waitForSelector("blackjack-duracion");
    //     vistaLogin.ingresarDuracion(""); // Simula la entrada vacía
    //     vistaLogin.ingresarTirada(""); // Simula la entrada vacía
    //     vistaLogin.darClickEnGuardar();
    //     String texto = vistaLogin.obtenerMensaje("p.mensajeError");
    //     assertThat(texto, equalToIgnoringCase("Los datos no pueden estar vacíos"));
    // }
    
    // @Test
    // void deberiaNavegarAlHomeSiElUsuarioExiste() {
    //     vistaLogin.ingresarDuracion("test@unlam.edu.ar");
    //     vistaLogin.ingresarTirada("test");
    //     vistaLogin.darClickEnGuardar();
    //     String url = vistaLogin.obtenerURLActual();
    //     assertThat(url, containsStringIgnoringCase("/spring/home"));
    // }
}
