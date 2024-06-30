package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

public class VistaLogin extends VistaWeb {

    public VistaLogin(Page page) {
        super(page);
    }

    public String obtenerTexto(String selector) {
        return this.obtenerTextoDelElemento(selector);
    }

    public void ingresarEmail(String email) {
        this.escribirEnElElemento("#email", email);
    }

    public void ingresarContrasenia(String contrasenia) {
        this.escribirEnElElemento("#password", contrasenia);
    }

    public void darClick(String selector) {
        this.darClickEnElElemento(selector);
    }

    public void login(String email, String contrasenia) {
        this.ingresarEmail(email);
        this.ingresarContrasenia(contrasenia);
        this.darClick("#btn-login");
    }
}
