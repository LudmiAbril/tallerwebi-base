package com.tallerwebi.presentacion;
import com.tallerwebi.dominio.BingoManager;
import com.tallerwebi.dominio.dto.Mensaje;
import com.tallerwebi.dominio.dto.MensajeBingo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

@Controller
public class ControladorBingoWebsocket {

    @Autowired
    private HttpSession session;


    @MessageMapping("/bingo-multijugador")
    @SendTo("/topic/updates")
    public MensajeBingo handleBingoMessage(MensajeBingo message) throws Exception {
        Set<Integer> numerosEntregados = (Set<Integer>) session.getAttribute("numerosEntregadosDeLaSesion");
        if (numerosEntregados == null) {
            numerosEntregados = new HashSet<>();
        }
        int nuevoNumero = Integer.parseInt(message.getContent());
        numerosEntregados.add(nuevoNumero);
        session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);
        MensajeBingo response = new MensajeBingo();
        response.setContent(numerosEntregados.toString());
        return response;
    }


}
