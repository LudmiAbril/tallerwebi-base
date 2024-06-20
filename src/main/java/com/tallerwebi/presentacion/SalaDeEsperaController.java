package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.BingoMultijugador;
import com.tallerwebi.dominio.dto.SalaEsperaMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SalaDeEsperaController {

    private List<String> jugadores = new ArrayList<>();
    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/sala-espera/join")
    @SendTo("/topic/sala-espera")
    public SalaEsperaMessage joinSalaEspera(SalaEsperaMessage message) {
        jugadores.add(message.getMessage());
        template.convertAndSend("/topic/sala-espera", new SalaEsperaMessage("join", jugadores));
        if (jugadores.size() >= 2) {
            SalaEsperaMessage startMessage = new SalaEsperaMessage("start", jugadores);
            template.convertAndSend("/topic/sala-espera", startMessage);
            return startMessage;
        }
        return new SalaEsperaMessage("join", jugadores);
    }

}
