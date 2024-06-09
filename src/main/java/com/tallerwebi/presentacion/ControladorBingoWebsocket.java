package com.tallerwebi.presentacion;
import com.tallerwebi.dominio.BingoManager;
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


    @MessageMapping("/bingo")
    @SendTo("/topic/updates")
    public BingoMessage handleBingoMessage(BingoMessage message) throws Exception {
        Set<Integer> numerosEntregados = (Set<Integer>) session.getAttribute("numerosEntregadosDeLaSesion");
        if (numerosEntregados == null) {
            numerosEntregados = new HashSet<>();
        }
        int nuevoNumero = Integer.parseInt(message.getContent());
        numerosEntregados.add(nuevoNumero);
        session.setAttribute("numerosEntregadosDeLaSesion", numerosEntregados);
        BingoMessage response = new BingoMessage();
        response.setContent(numerosEntregados.toString());
        return response;
    }

    public static class BingoMessage {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
