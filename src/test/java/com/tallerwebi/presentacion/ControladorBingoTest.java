package com.tallerwebi.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.CartonBingo;
import com.tallerwebi.dominio.ServicioBingo;
import com.tallerwebi.infraestructura.ServicioBingoImpl;

public class ControladorBingoTest {

    private ControladorBingo controladorBingo;
    private ServicioBingoImpl servicioBingo;

    @BeforeEach
    public void init() {
        this.servicioBingo = new ServicioBingoImpl();
        this.controladorBingo = new ControladorBingo(servicioBingo);
    }

    // @Test
    // public void queElCasilleroDelCartonQueSeMarcaSeaIgualAlNumeroCantado() {
    //     ModelAndView mav = this.controladorBingo.comenzarJuegoBingo(null, null);
    //     // obtener el numero cantado que se envio
    //     Integer numeroCantadoEntregado = (Integer) mav.getModel().get("numeroCantado");
    //     // obtener el carton
    //     CartonBingo cartonEntregado = (CartonBingo) mav.getModel().get("carton");
    //     // si son iguales el test da true

    //     // el usuario tiene que marcar un casillero
    //     // tengo que guardar de alguna forma el casillero del carton que marca
    //     // tengo que comparar eso con el que se entrego
    //     // si son iguales se marca
    // }

    @Test
    public void queSePuedaHacerBingo() {

    }

    @Test
    public void queSeEntregueUnNumeroAleatorioCantadoHastaQueSeHagaBingo() {

    }

}
