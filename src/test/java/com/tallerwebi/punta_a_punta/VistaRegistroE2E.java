package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.tallerwebi.punta_a_punta.vistas.VistaLogin;
import com.tallerwebi.punta_a_punta.vistas.VistaRegistro;
import org.junit.jupiter.api.*;

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
    void deberiaDecirUsuarioExistenteSiSeIngresaUnaCorreoYaIngresado() {
        vistaRegistro.ingresarNombre("Celeste");
        vistaRegistro.ingresarEmail("mm@gmail.com");
        vistaRegistro.ingresarContrasenia("1212");
        vistaRegistro.darClick("#btn-registrarme");
        String textoEsperado = "Ya existe un usuario con ese email";
        String textoActual = vistaRegistro.obtenerTexto("div.error p");
        assertThat(textoEsperado, equalToIgnoringCase(textoActual));
    }

    @Test
    void deberiaPrevenirEnvioDeFormularioSiNombreEstaVacio() {
        vistaRegistro.ingresarEmail("celes@gmail.com");
        vistaRegistro.ingresarContrasenia("1212");
        vistaRegistro.darClick("#btn-registrarme");
        String urlActual = vistaRegistro.obtenerURLActual();
        assertThat(urlActual, containsStringIgnoringCase("/spring/registro"));
    }

    @Test
    void deberiaPrevenirEnvioDeFormularioSiEmailEstaVacio() {
        vistaRegistro.ingresarNombre("Celeste");
        vistaRegistro.ingresarContrasenia("1212");
        vistaRegistro.darClick("#btn-registrarme");
        String urlActual = vistaRegistro.obtenerURLActual();
        assertThat(urlActual, containsStringIgnoringCase("/spring/registro"));
    }

    @Test
    void deberiaPrevenirEnvioDeFormularioSiContraseniaEstaVacia() {
        vistaRegistro.ingresarNombre("Celeste");
        vistaRegistro.ingresarEmail("celes@gmail.com");
        vistaRegistro.darClick("#btn-registrarme");
        String urlActual = vistaRegistro.obtenerURLActual();
        assertThat(urlActual, containsStringIgnoringCase("/spring/registro"));
    }

    @Test
    void deberiaNavegarALoginSiTodosLosCamposSonCorrectos() {
        vistaRegistro.ingresarNombre("Celeste");
        vistaRegistro.ingresarEmail("celes@gmail.com");
        vistaRegistro.ingresarContrasenia("1212");
        vistaRegistro.darClick("#btn-registrarme");
        String url = vistaRegistro.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/spring/login"));
    }


}
