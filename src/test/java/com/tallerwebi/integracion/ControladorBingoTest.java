package com.tallerwebi.integracion;

import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.CartonBingo;
import com.tallerwebi.dominio.ServicioBingo;
import com.tallerwebi.infraestructura.ServicioBingoImpl;
import com.tallerwebi.presentacion.ControladorBingo;

public class ControladorBingoTest {

    private ServicioBingo servicioBingo;
    private ControladorBingo controladorBingo;

    @BeforeEach
    public void init() {
        this.servicioBingo = new ServicioBingoImpl();
        this.controladorBingo = new ControladorBingo(servicioBingo);
    }

    @Test
    public void queAlSolicitarLaVistaBingoSeEntregueLaVistaBingo() {
        ModelAndView mav = this.controladorBingo.irAlBingo();
        assertThat(mav.getViewName(), equalToIgnoringCase("bingo"));
    }

    @Test
    public void queAlSolicitarLaVistaBingoSeEntreguenLosModelosCorrespondientes(){
        ModelAndView mav = this.controladorBingo.irAlBingo();
        assertThat(mav.getModel().get("carton"), is(instanceOf(CartonBingo.class)));
        assertThat(mav.getModel().get("numeroCantado"), is(instanceOf(Integer.class)));
    }

    @Test
    public void queElCasilleroQueSeMarcaSeaIgualAlNumeroCantado(){
        
    }
}
