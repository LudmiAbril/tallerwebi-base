package com.tallerwebi.dominio;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;

@Entity
public class PartidaBingo extends Partida {

    @ElementCollection
    @CollectionTable(name = "casilleros_marcados", joinColumns = @JoinColumn(name = "partida_id"))
    @Column(name = "casillero")
    private Set<Integer> casillerosMarcados;
    private Boolean seHizoLinea;
    private Boolean seHizoBingo;
    private TipoPartidaBingo tipoPartidaBingo;
    private Integer tirada;

    public PartidaBingo(Set<Integer> casillerosMarcados, Boolean seHizoLinea, Boolean seHizoBingo,
            TipoPartidaBingo tipoPartidaBingo, Integer tirada) {
        this.casillerosMarcados = casillerosMarcados;
        this.seHizoLinea = seHizoLinea;
        this.seHizoBingo = seHizoBingo;
        this.tipoPartidaBingo = tipoPartidaBingo;
        this.tirada = tirada;
    }

    public Set<Integer> getCasillerosMarcados() {
        return casillerosMarcados;
    }

    public void setCasillerosMarcados(Set<Integer> casillerosMarcados) {
        this.casillerosMarcados = casillerosMarcados;
    }

    public Boolean getSeHizoLinea() {
        return seHizoLinea;
    }

    public void setSeHizoLinea(Boolean seHizoLinea) {
        this.seHizoLinea = seHizoLinea;
    }

    public Boolean getSeHizoBingo() {
        return seHizoBingo;
    }

    public void setSeHizoBingo(Boolean seHizoBingo) {
        this.seHizoBingo = seHizoBingo;
    }

    public TipoPartidaBingo getTipoPartidaBingo() {
        return tipoPartidaBingo;
    }

    public void setTipoPartidaBingo(TipoPartidaBingo tipoPartidaBingo) {
        this.tipoPartidaBingo = tipoPartidaBingo;
    }

    public Integer getTirada() {
        return tirada;
    }

    public void setTirada(Integer tirada) {
        this.tirada = tirada;
    }

}
