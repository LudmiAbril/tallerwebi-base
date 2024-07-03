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
import static org.hamcrest.Matchers.is;

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
        vistaAccesoAjuegos.irAccesoAjuegos("mm@gmail.com", "boca");
        String frase1 = vistaAccesoAjuegos.obtenerTexto(".frase .animated-text:nth-of-type(1)");
        String frase2 = vistaAccesoAjuegos.obtenerTexto(".frase .animated-text:nth-of-type(2)");
        assertThat(frase1, equalToIgnoringCase("Todo la diversión y el descanso a su alcance"));
        assertThat(frase2, equalToIgnoringCase("Disfruta de nuestro catsino"));
    }

    @Test
    void deberiaAbrirModalDeConfiguracion() {
        vistaAccesoAjuegos.irAccesoAjuegos("mm@gmail.com", "boca");
        vistaAccesoAjuegos.abrirMenu();
        vistaAccesoAjuegos.abrirModalConfiguracion();
        assertThat(vistaAccesoAjuegos.modalConfiguracionEstaVisible(),is(true));
    }

        @Test
   void deberiaEvitarEnviarFormularioConCampoVacio() {
        vistaAccesoAjuegos.irAccesoAjuegos("mm@gmail.com", "boca");
        vistaAccesoAjuegos.abrirMenu();
       vistaAccesoAjuegos.abrirModalConfiguracion();
       vistaAccesoAjuegos.ingresarDuracionBlackjack("1");
       vistaAccesoAjuegos.seleccionarValorAsBlackjack("11");
       vistaAccesoAjuegos.seleccionarDimensionCarton("4");
       vistaAccesoAjuegos.ingresarTirada(" ");
       vistaAccesoAjuegos.darClick("#guardar");
       assertThat(vistaAccesoAjuegos.modalConfiguracionEstaVisible(),is(true));

   }

    @Test
    void deberiaGuardarLaConfiguracionCorrectamenteSiIngresoQueLaTiradaSeaMayorAlCarton() {
        vistaAccesoAjuegos.irAccesoAjuegos("mm@gmail.com", "boca");
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
        vistaAccesoAjuegos.irAccesoAjuegos("mm@gmail.com", "boca");
        vistaAccesoAjuegos.abrirMenu();
        vistaAccesoAjuegos.abrirModalConfiguracion();
        vistaAccesoAjuegos.seleccionarDimensionCarton("4");
        vistaAccesoAjuegos.ingresarTirada("-5");
        vistaAccesoAjuegos.darClick("#guardar");

        assertThat(vistaAccesoAjuegos.modalConfiguracionEstaVisible(), is(true));
    }


    @Test
    void deberiaEvitarEnviarFormularioBlackjackConDuracionNegativa() {
        vistaAccesoAjuegos.irAccesoAjuegos("mm@gmail.com", "boca");
        vistaAccesoAjuegos.abrirMenu();
        vistaAccesoAjuegos.abrirModalConfiguracion();

        // Simular una duración negativa en el campo de duración del Blackjack
        vistaAccesoAjuegos.ingresarDuracionBlackjack("-2");
        vistaAccesoAjuegos.darClick("#guardar");

        assertThat(vistaAccesoAjuegos.modalConfiguracionEstaVisible(), is(true));
    }
    @Test
    void deberiaSeleccionarValorAsBlackjack() {
        vistaAccesoAjuegos.irAccesoAjuegos("mm@gmail.com", "boca");
        vistaAccesoAjuegos.abrirMenu();
        vistaAccesoAjuegos.abrirModalConfiguracion();
        String valorSeleccionado = "11";
        vistaAccesoAjuegos.seleccionarValorAsBlackjack(valorSeleccionado);

        assertThat(vistaAccesoAjuegos.obtenerValorAsBlackjack(), equalTo(valorSeleccionado));
    }


    @Test
    void deberiaCerrarModalConfiguracion() {
        vistaAccesoAjuegos.irAccesoAjuegos("mm@gmail.com", "boca");
        vistaAccesoAjuegos.abrirMenu();
        vistaAccesoAjuegos.abrirModalConfiguracion();
        vistaAccesoAjuegos.cerrarModalConfiguracion();
        boolean modalVisibilidad = vistaAccesoAjuegos.modalConfiguracionEstaVisible();
        assertThat(modalVisibilidad, is(false));
    }


    @Test
    void deberiaAbrirModalDeSalida() {
        vistaAccesoAjuegos.irAccesoAjuegos("mm@gmail.com", "boca");
        vistaAccesoAjuegos.abrirMenu();
        vistaAccesoAjuegos.abrirModalSalida();
        assertThat(vistaAccesoAjuegos.modalSalidaEstaVisible(), is(true));
    }

    @Test
    void deberiaDirigirAlInicioDeLaPaginaSiApretasElBotnDeSalida() {
        vistaAccesoAjuegos.irAccesoAjuegos("mm@gmail.com", "boca");
        vistaAccesoAjuegos.abrirMenu();
        vistaAccesoAjuegos.abrirModalSalida();
        vistaAccesoAjuegos.darClick("#btn-salir");
        String url = vistaAccesoAjuegos.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/spring/salir"));
    }

    @Test
    void deberiaNavegarABingo() {
        vistaAccesoAjuegos.irAccesoAjuegos("mm@gmail.com", "boca");
        vistaAccesoAjuegos.darClick("#accederBingo");
        String url = vistaAccesoAjuegos.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/irAlBingo"));
    }

    @Test
    void deberiaNavegarASenku() {
        vistaAccesoAjuegos.irAccesoAjuegos("mm@gmail.com", "boca");
        vistaAccesoAjuegos.darClick("#accederSenku");
        String url = vistaAccesoAjuegos.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/irAlSenku"));
    }

    @Test
    void deberiaNavegarABlackjack() {
        vistaAccesoAjuegos.irAccesoAjuegos("mm@gmail.com", "boca");
        vistaAccesoAjuegos.darClick("#accederBj");
        String url = vistaAccesoAjuegos.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/inicio-blackjack"));
    }

   



}
