package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.*;
import com.tallerwebi.punta_a_punta.vistas.VistaHome;
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
    VistaHome vistaHome;
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
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void deberiaNavegarAAccesoJuegosSiElUsuarioExiste() {
        vistaHome.irALogin();
        vistaLogin = new VistaLogin(page);
        vistaLogin.login("mm@gmail.com", "boca");
        String url = vistaLogin.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/spring/acceso-juegos"));
    }

    @Test
    void deberiaDecirUsuarioOClaveIncorrectaSiSeIngresaUnaContraseniaIncorrecta() {
        vistaHome.irALogin();
        vistaLogin = new VistaLogin(page);
        vistaLogin.login("mica@gmail.com", "mica");
        String textoEsperado = "Usuario o clave incorrecta";
        String textoActual = vistaLogin.obtenerTexto("div.error p");
        assertThat(textoEsperado, equalToIgnoringCase(textoActual));
    }

    @Test
    void deberiaDecirUsuarioOClaveIncorrectaSiSeIngresaUnEmailIncorrecto() {
        vistaHome.irALogin();
        vistaLogin = new VistaLogin(page);
        vistaLogin.login("micaelazara@gmail.com", "1234");
        String textoEsperado = "Usuario o clave incorrecta";
        String textoActual = vistaLogin.obtenerTexto("div.error p");
        assertThat(textoEsperado, equalToIgnoringCase(textoActual));
    }

    @Test
    void deberiaNavegarARegistroCuandoSeClickeaRegistrarse() {
        vistaHome.irALogin();
        vistaLogin = new VistaLogin(page);
        vistaLogin.darClick(".boton-margen");
        String url = vistaLogin.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/spring/registro"));
    }

    @Test
    void deberiaPrevenirEnvioDeFormularioSiEmailEstaVacio() {
        vistaHome.irALogin();
        vistaLogin = new VistaLogin(page);
        vistaLogin.ingresarContrasenia("boca");
        vistaLogin.darClick("#btn-login");
        String urlActual = vistaLogin.obtenerURLActual();
        assertThat(urlActual, containsStringIgnoringCase("/spring/login"));
    }

    @Test
    void deberiaPrevenirEnvioDeFormularioSiContraseniaEstaVacia() {
        vistaHome.irALogin();
        vistaLogin = new VistaLogin(page);
        vistaLogin.ingresarEmail("mm2@gmail.com");
        vistaLogin.darClick("#btn-login");
        String urlActual = vistaLogin.obtenerURLActual();
        assertThat(urlActual, containsStringIgnoringCase("/spring/login"));
    }
}
