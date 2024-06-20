package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

public class VistaLogin extends VistaWeb {

    public VistaLogin(Page page) {
        super(page);
        page.navigate("localhost:8080/spring/login");
    }

    public String obtenerTexto(String selector){
        return this.obtenerTextoDelElemento(selector);
    }

    public String obtenerMensaje(String selector){
        return this.obtenerTextoDelElemento(selector);
    }

    public void ingresarEmail(String email){
        this.escribirEnElElemento("#email", email);
    }

    public void ingresarContrasenia(String contrasenia){
        this.escribirEnElElemento("#password", contrasenia);
    }

    public void darClickEnIngresar(){
        this.darClickEnElElemento("#btn-registrarme");
    }
}
