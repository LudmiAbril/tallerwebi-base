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
        // browser = playwright.chromium().launch(new
        // BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
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

    /**@Test
    void deberiaDecirBienvenidoDeNuevoJefe() {
        String texto = vistaLogin.obtenerTexto("#texto1");
        assertThat("¡Bienvenido de nuevo, jefe!", equalToIgnoringCase(texto));
    }*/

    @Test
    void deberiaNavegarAAccesoJuegosSiElUsuarioExiste() {
        vistaLogin.ingresarEmail("mm@gmail.com");
        vistaLogin.ingresarContrasenia("boca");
        vistaLogin.darClick("#btn-login");
        String url = vistaLogin.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/spring/acceso-juegos"));
    }

    @Test
    void deberiaDecirUsuarioOClaveIncorrectaSiSeIngresaUnaContraseniaIncorrecta() {
        vistaLogin.ingresarEmail("mica@gmail.com");
        vistaLogin.ingresarContrasenia("mica");
        vistaLogin.darClick("#btn-login");
        String textoEsperado = "Usuario o clave incorrecta";
        String textoActual = vistaLogin.obtenerTexto("div.error p");
        assertThat(textoEsperado, equalToIgnoringCase(textoActual));
    }

    @Test
    void deberiaDecirUsuarioOClaveIncorrectaSiSeIngresaUnEmailIncorrecto() {
        vistaLogin.ingresarEmail("micaelazara@gmail.com");
        vistaLogin.ingresarContrasenia("1234");
        vistaLogin.darClick("#btn-login");
        String textoEsperado = "Usuario o clave incorrecta";
        String textoActual = vistaLogin.obtenerTexto("div.error p");
        assertThat(textoEsperado, equalToIgnoringCase(textoActual));
    }

    @Test
    void deberiaNavegarARegistroCuandoSeClickeaRegistrarse(){
        vistaLogin.darClick(".boton-margen");
        String url = vistaLogin.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/spring/registro"));
    }

    @Test
    void deberiaPrevenirEnvioDeFormularioSiEmailEstaVacio() {
        vistaLogin.ingresarContrasenia("boca");
        vistaLogin.darClick("#btn-login");
        String urlActual = vistaLogin.obtenerURLActual();
        assertThat(urlActual, containsStringIgnoringCase("/spring/login"));
    }

    @Test
    void deberiaPrevenirEnvioDeFormularioSiContraseniaEstaVacia() {
        vistaLogin.ingresarEmail("mm2@gmail.com");
        vistaLogin.darClick("#btn-login");
        String urlActual = vistaLogin.obtenerURLActual();
        assertThat(urlActual, containsStringIgnoringCase("/spring/login"));
    }

}
