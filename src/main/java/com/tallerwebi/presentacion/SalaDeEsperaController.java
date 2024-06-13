package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.dto.SalaEsperaMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SalaDeEsperaController {

    private List<String> jugadores = new ArrayList<>();

    @MessageMapping("/sala-espera/join")
    @SendTo("/topic/sala-espera")
    public SalaEsperaMessage joinSalaEspera(SalaEsperaMessage message) {
        jugadores.add(message.getMessage());
        if (jugadores.size() >= 2) {
            return new SalaEsperaMessage("start", jugadores);
        }
        return new SalaEsperaMessage("join", jugadores);
    }
}
