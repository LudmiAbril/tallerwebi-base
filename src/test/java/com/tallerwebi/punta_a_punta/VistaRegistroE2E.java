package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.*;
import com.tallerwebi.punta_a_punta.vistas.VistaHome;
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
    void deberiaDecirUsuarioExistenteSiSeIngresaUnaCorreoYaIngresado() {
        vistaHome.irARegistro();
        vistaRegistro = new VistaRegistro(page);
        vistaRegistro.registrarUsuario("Celeste", "mm@gmail.com", "1212");
        String textoEsperado = "Ya existe un usuario con ese email";
        String textoActual = vistaRegistro.obtenerTexto("div.error p");
        assertThat(textoEsperado, equalToIgnoringCase(textoActual));
    }

    @Test
    void deberiaPrevenirEnvioDeFormularioSiNombreEstaVacio() {
        vistaHome.irARegistro();
        vistaRegistro = new VistaRegistro(page);
        vistaRegistro.ingresarEmail("celes@gmail.com");
        vistaRegistro.ingresarContrasenia("1212");
        vistaRegistro.darClick("#btn-registrarme");
        String urlActual = vistaRegistro.obtenerURLActual();
        assertThat(urlActual, containsStringIgnoringCase("/spring/registro"));
    }

    @Test
    void deberiaPrevenirEnvioDeFormularioSiEmailEstaVacio() {
        vistaHome.irARegistro();
        vistaRegistro = new VistaRegistro(page);
        vistaRegistro.ingresarNombre("Celeste");
        vistaRegistro.ingresarContrasenia("1212");
        vistaRegistro.darClick("#btn-registrarme");
        String urlActual = vistaRegistro.obtenerURLActual();
        assertThat(urlActual, containsStringIgnoringCase("/spring/registro"));
    }

    @Test
    void deberiaPrevenirEnvioDeFormularioSiContraseniaEstaVacia() {
        vistaHome.irARegistro();
        vistaRegistro = new VistaRegistro(page);
        vistaRegistro.ingresarNombre("Celeste");
        vistaRegistro.ingresarEmail("celes@gmail.com");
        vistaRegistro.darClick("#btn-registrarme");
        String urlActual = vistaRegistro.obtenerURLActual();
        assertThat(urlActual, containsStringIgnoringCase("/spring/registro"));
    }

}
