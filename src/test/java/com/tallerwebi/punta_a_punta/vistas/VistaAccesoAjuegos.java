package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class VistaAccesoAjuegos extends VistaWeb {
    public VistaAccesoAjuegos(Page page) {
        super(page);
        page.navigate("localhost:8080/spring/acceso-juegos");
    }

    public String obtenerTitulo() {
        return this.page.title();
    }

    public void darClick(String selector){
        this.darClickEnElElemento(selector);
    }


    public String obtenerTexto(String selector) {
        return this.page.locator(selector).innerText();
    }

    public void abrirMenu() {
        this.darClickEnElElemento(".menu-icon");
    }

    public void abrirModalConfiguracion() {
        this.darClickEnElElemento(".menu-item:nth-of-type(1)");
    }

    public void abrirModalSalida() {
        this.darClickEnElElemento(".menu-item:nth-of-type(2)");
    }

    public boolean modalConfiguracionEstaVisible() {
        return this.page.locator("#configuracionModal").isVisible();
    }

    public boolean modalSalidaEstaVisible() {
        return this.page.locator("#exitModal").isVisible();
    }

    public String obtenerURLActual() {
        return this.page.url();
    }

    public void seleccionarDimensionCarton(String dimension) {
        this.darClickEnElElemento("input[name='dimensionCarton'][value='" + dimension + "'] + label");
    }


    public void ingresarTirada(String tirada) {
        this.escribirEnElElemento("#cant-numeros", tirada);
    }

    public String obtenerMensaje(String selector){
        return this.obtenerTextoDelElemento(selector);
    }

    public String obtenerMensajeError() {
        return this.obtenerTexto("#mensajeError");
    }




    public void ingresarDuracionBlackjack(String duracion) {
        this.escribirEnElElemento("#blackjack-duracion", duracion);
    }


    public void seleccionarValorAsBlackjack(String valor) {
        this.darClickEnElElemento("input[name='valorAs'][value='" + valor + "'] + label");
    }

    public String obtenerValorAsBlackjack() {
        return this.page.locator("input[name='valorAs']:checked").inputValue();
    }

    public void cerrarModalConfiguracion() {
        this.darClickEnElElemento(".close2");
    }


    public void esperarElementoVisible(String selector) {
        this.page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
    }
}