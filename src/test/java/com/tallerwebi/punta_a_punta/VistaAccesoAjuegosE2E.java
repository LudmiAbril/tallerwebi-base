package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.tallerwebi.punta_a_punta.vistas.VistaAccesoAjuegos;
import org.junit.jupiter.api.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class VistaAccesoAjuegosE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaAccesoAjuegos vistaAccesoAjuegos;
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
        vistaAccesoAjuegos = new VistaAccesoAjuegos(page);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }
    @Test
    void deberiaMostrarFrasesEnLaPagina() {
        String frase1 = vistaAccesoAjuegos.obtenerTexto(".frase .animated-text:nth-of-type(1)");
        String frase2 = vistaAccesoAjuegos.obtenerTexto(".frase .animated-text:nth-of-type(2)");
        assertThat(frase1, equalToIgnoringCase("Todo la diversión y el descanso a su alcance"));
        assertThat(frase2, equalToIgnoringCase("Disfruta de nuestro catsino"));
    }


    @Test
    void deberiaAbrirModalDeConfiguracion() {
        vistaAccesoAjuegos.abrirMenu();
        vistaAccesoAjuegos.abrirModalConfiguracion();
        assertThat(vistaAccesoAjuegos.modalConfiguracionEstaVisible(), equalToIgnoringCase("true"));
    }

   /* @Test
    void deberiaMostrarMensajeErrorCuandoTiradaMenorQue16En4x4() {
        vistaAccesoAjuegos.abrirMenu();
        vistaAccesoAjuegos.abrirModalConfiguracion();
        vistaAccesoAjuegos.seleccionarDimensionCarton("4");
        vistaAccesoAjuegos.ingresarTirada("13"); // Menos de 4*4
        vistaAccesoAjuegos.darClick("#guardar");
        String mensajeErrorEsperado = "Ingrese al menos 16 numeros.";
        String mensajeErrorActual = vistaAccesoAjuegos.obtenerMensaje("div.error p");
        assertThat(mensajeErrorActual, equalToIgnoringCase(mensajeErrorEsperado));


         @Test
    void deberiaEvitarEnviarFormularioConCampoVacio() {
        vistaAccesoAjuegos.abrirMenu();
        vistaAccesoAjuegos.abrirModalConfiguracion();
        vistaAccesoAjuegos.seleccionarDimensionCarton("5");
        vistaAccesoAjuegos.ingresarTirada("");
        vistaAccesoAjuegos.darClick("#guardar");
        assertThat(vistaAccesoAjuegos.modalConfiguracionEstaVisible(), equalToIgnoringCase("true"));
    }
    }*/
    @Test
    void deberiaGuardarLaConfiguracionCorrectamenteSiIngresoQueLaTiradaSeaMayorAlCarton() {
        vistaAccesoAjuegos.abrirMenu();
        vistaAccesoAjuegos.abrirModalConfiguracion();
        vistaAccesoAjuegos.seleccionarDimensionCarton("4");
        vistaAccesoAjuegos.ingresarTirada("17");
        vistaAccesoAjuegos.darClick("#guardar");
        String mensajeErrorActual = vistaAccesoAjuegos.obtenerMensajeError();
        assertThat(mensajeErrorActual, equalToIgnoringCase(""));
    }



    @Test
    void deberiaEvitarEnviarFormularioConNumerosNegativos() {
        vistaAccesoAjuegos.abrirMenu();
        vistaAccesoAjuegos.abrirModalConfiguracion();
        vistaAccesoAjuegos.seleccionarDimensionCarton("4");
        vistaAccesoAjuegos.ingresarTirada("-5");
        vistaAccesoAjuegos.darClick("#guardar");

        // Verificar que el modal sigue visible
        assertThat(vistaAccesoAjuegos.modalConfiguracionEstaVisible(), equalToIgnoringCase("true"));
    }


    @Test
    void deberiaEvitarEnviarFormularioBlackjackConDuracionNegativa() {
        vistaAccesoAjuegos.abrirMenu();
        vistaAccesoAjuegos.abrirModalConfiguracion();

        // Simular una duración negativa en el campo de duración del Blackjack
        vistaAccesoAjuegos.ingresarDuracionBlackjack("-2");
        vistaAccesoAjuegos.darClick("#guardar");

        assertThat(vistaAccesoAjuegos.modalConfiguracionEstaVisible(), equalToIgnoringCase("true"));
    }



    @Test
    void deberiaAbrirModalDeSalida() {
        vistaAccesoAjuegos.abrirMenu();
        vistaAccesoAjuegos.abrirModalSalida();
        assertThat(vistaAccesoAjuegos.modalSalidaEstaVisible(), equalToIgnoringCase("true"));
    }

    @Test
    void deberiaCerrarModalConfiguracion() {
        vistaAccesoAjuegos.abrirMenu();
        vistaAccesoAjuegos.abrirModalConfiguracion();
        vistaAccesoAjuegos.cerrarModalConfiguracion();
        String modalVisibilidad = vistaAccesoAjuegos.modalConfiguracionEstaVisible();
        assertThat(modalVisibilidad, equalToIgnoringCase("false"));
    }
    @Test
    void deberiaNavegarABingo() {
        vistaAccesoAjuegos.darClick("#accederBingo");
        String url = vistaAccesoAjuegos.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/irAlBingo"));
    }

    @Test
    void deberiaNavegarASenku() {
        vistaAccesoAjuegos.darClick("#accederSenku");
        String url = vistaAccesoAjuegos.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/irAlSenku"));
    }

    @Test
    void deberiaNavegarABlackjack() {
        vistaAccesoAjuegos.darClick("#accederBj");

        String url = vistaAccesoAjuegos.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/inicio-blackjack"));
    }

    @Test
    void deberiaNavegarAAhorcado() {
        vistaAccesoAjuegos.darClick("#accederAhorcado");
        String url = vistaAccesoAjuegos.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/irAlAhorcado"));
    }
    @Test
    void deberiaSeleccionarValorAsBlackjack() {
        vistaAccesoAjuegos.abrirMenu();
        vistaAccesoAjuegos.abrirModalConfiguracion();
        String valorSeleccionado = "11";
        vistaAccesoAjuegos.seleccionarValorAsBlackjack(valorSeleccionado);

        assertThat(vistaAccesoAjuegos.obtenerValorAsBlackjack(), equalTo(valorSeleccionado));
    }

}
