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

    public void ingresarDuracion(String duracion){
        this.escribirEnElElemento("blackjack-duracion", duracion);
    }

    public void ingresarTirada(String tirada){
        this.escribirEnElElemento("#cant-numeros", tirada);
    }

    public void darClickEnGuardar(){
        this.darClickEnElElemento(".save-button");
    }
}
