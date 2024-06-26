package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class VistaRegistro extends VistaWeb {
    public VistaRegistro(Page page) {
        super(page);
        page.navigate("localhost:8080/spring/registro");
    }

    public String obtenerTexto(String selector){
        return this.obtenerTextoDelElemento(selector);
    }
    public void ingresarNombre(String nombre){this.escribirEnElElemento("#nombre", nombre);}

    public void ingresarEmail(String email){
        this.escribirEnElElemento("#email", email);
    }

    public void ingresarContrasenia(String contrasenia){
        this.escribirEnElElemento("#password", contrasenia);
    }

    public void darClick(String selector){
        this.darClickEnElElemento(selector);
    }
}
