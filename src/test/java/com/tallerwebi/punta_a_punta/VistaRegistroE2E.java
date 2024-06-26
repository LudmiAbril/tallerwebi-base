package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.*;
import com.tallerwebi.punta_a_punta.vistas.VistaLogin;
import com.tallerwebi.punta_a_punta.vistas.VistaRegistro;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class VistaRegistroE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaRegistro vistaRegistro;
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
        vistaRegistro = new VistaRegistro(page);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void deberiaDecirEntraAlJuegoJefe() {
        String texto = vistaRegistro.obtenerTexto("#texto1");
        assertThat("¡Entra al juego, jefe!", equalToIgnoringCase(texto));
    }

    @Test
    void deberiaNavegarALoginSiSePudoRegistrarElUsuario() {
        vistaRegistro.ingresarEmail("milanesadejava@gmail.com");
        vistaRegistro.ingresarContrasenia("java");
        vistaRegistro.darClick("#btn-registrarme");
        String url = vistaRegistro.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/spring/acceso-juegos"));
    }

    // @Test
    // void deberiaDecirUsuarioOClaveIncorrectaSiSeIngresaUnaContraseniaIncorrecta() {
    //     vistaRegistro.ingresarEmail("mica@gmail.com");
    //     vistaRegistro.ingresarContrasenia("mica");
    //     vistaRegistro.darClick("#btn-registrarme");
    //     String textoEsperado = "Usuario o clave incorrecta";
    //     String textoActual = vistaRegistro.obtenerTexto("div.error p");
    //     assertThat(textoEsperado, equalToIgnoringCase(textoActual));
    // }

    // @Test
    // void deberiaDecirUsuarioOClaveIncorrectaSiSeIngresaUnEmailIncorrecto() {
    //     vistaRegistro.ingresarEmail("micaelazara@gmail.com");
    //     vistaRegistro.ingresarContrasenia("1234");
    //     vistaRegistro.darClick("#btn-registrarme");
    //     String textoEsperado = "Usuario o clave incorrecta";
    //     String textoActual = vistaRegistro.obtenerTexto("div.error p");
    //     assertThat(textoEsperado, equalToIgnoringCase(textoActual));
    // }

    // @Test
    // void deberiaNavegarARegistroCuandoSeClickeaRegistrarse(){
    //     vistaRegistro.darClick(".boton-margen");
    //     String url = vistaRegistro.obtenerURLActual();
    //     assertThat(url, containsStringIgnoringCase("/spring/registro"));
    // }

}
