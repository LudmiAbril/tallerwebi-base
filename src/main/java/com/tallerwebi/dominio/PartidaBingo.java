package com.tallerwebi.dominio;

import java.util.Set;

import javax.persistence.*;

@Entity
public class PartidaBingo extends Partida {
    @ElementCollection
    @CollectionTable(name = "casilleros_marcados", joinColumns = @JoinColumn(name = "partida_id"))
    @Column(name = "casillero")
    private Set<Integer> casillerosMarcados;

    private Boolean seHizoLinea;
    private Boolean seHizoBingo;
    @Enumerated(EnumType.STRING)
    private TipoPartidaBingo tipoPartidaBingo;
    private Integer tirada;
    private Integer cantidadDeCasillerosMarcados;
    private Boolean seHizoBingoBot;

    public PartidaBingo(Long idJugador, Juego juego, Set<Integer> casillerosMarcados, Boolean seHizoLinea, Boolean seHizoBingo,
            TipoPartidaBingo tipoPartidaBingo, Integer tirada, Integer cantidadDeCasillerosMarcados, Boolean seHizoBingoBot) {
                super(idJugador, juego);
        this.casillerosMarcados = casillerosMarcados;
        this.seHizoLinea = seHizoLinea;
        this.seHizoBingo = seHizoBingo;
        this.tipoPartidaBingo = tipoPartidaBingo;
        this.tirada = tirada;
        this.cantidadDeCasillerosMarcados = cantidadDeCasillerosMarcados;
        this.seHizoBingoBot = seHizoBingoBot;
    }

    public PartidaBingo() {
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

    public Integer getCantidadDeCasillerosMarcados() {
        return cantidadDeCasillerosMarcados;
    }

    public void setCantidadDeCasillerosMarcados(Integer cantidadDeCasillerosMarcados) {
        this.cantidadDeCasillerosMarcados = cantidadDeCasillerosMarcados;
    }

    public Boolean getSeHizoBingoBot() {
        return seHizoBingoBot;
    }

    public void setSeHizoBingoBot(Boolean seHizoBingoBot) {
        this.seHizoBingoBot = seHizoBingoBot;
    }

    
}
