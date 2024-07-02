package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

public class VistaBlackjack extends VistaWeb {

    public VistaBlackjack(Page page) {
        super(page);
    }


    public void darClick(String selector) {
        this.darClickEnElElemento(selector);
    }

    public void accederYJugarBlackjack(String email, String password) {
        VistaAccesoAjuegos vistaAccesoAjuegos = new VistaAccesoAjuegos(this.page);
        vistaAccesoAjuegos.irAccesoAjuegos(email, password);
        vistaAccesoAjuegos.darClick("#accederBj");
        this.darClick("#jugarBlackjack");
    }

    public boolean modalResultadoDePartidaBlackjackEstaVisible() {
        int maxRetries = 10; // Número máximo de intentos
        int delay = 500; // Milisegundos entre cada intento

        for (int i = 0; i < maxRetries; i++) {
            if (this.page.locator("#resultadoPartida").isVisible()) {
                return true;
            }
            try {
                Thread.sleep(delay); // Espera antes de volver a intentar
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public boolean isVisible(String selector) {
        return !this.page.locator(selector).allInnerTexts().isEmpty();
    }

    public void ingresarTiempoLimiteBlackjack(String duracion) {
        this.escribirEnElElemento("#tiempoLimite", duracion);
    }



}
