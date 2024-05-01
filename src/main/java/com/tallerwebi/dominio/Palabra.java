package com.tallerwebi.dominio;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class Palabra {
    
    @Id
    // la id se autogenera incrementalmente
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String palabra;
    private Boolean adivinada;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPalabra() {
        return palabra;
    }
    public void setPalabra(String pregunta) {
        this.palabra = pregunta;
    }
    public Boolean getAdivinada() {
        return adivinada;
    }
    public void setAdivinada(Boolean adivinada) {
        this.adivinada = adivinada;
    }

    
}
