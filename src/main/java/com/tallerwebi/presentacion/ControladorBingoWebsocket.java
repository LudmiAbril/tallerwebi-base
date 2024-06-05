package com.tallerwebi.presentacion;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ControladorBingoWebsocket {

    @MessageMapping("/bingo")
    @SendTo("/topic/updates")
    public BingoMessage handleBingoMessage(BingoMessage message) throws Exception {
        // Lógica para manejar los mensajes de bingo
        return message;
    }

    public static class BingoMessage {
        private String content;
        // getters y setters
    }
}
