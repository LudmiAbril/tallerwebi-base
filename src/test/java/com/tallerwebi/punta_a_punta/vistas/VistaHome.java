package com.tallerwebi.punta_a_punta.vistas;


import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class VistaHome extends VistaWeb {

    public VistaHome(Page page) {
        super(page);
        page.navigate("localhost:8080/spring/home");
    }



    public void irARegistro() {
        this.darClickEnElElemento("#btn-registro a");
    }

    public void irALogin() {
        this.darClickEnElElemento("#btn-loguin a");
    }
}
