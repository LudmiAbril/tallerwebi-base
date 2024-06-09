
package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.BingoManager;
import com.tallerwebi.dominio.BingoMultijugador;
import com.tallerwebi.dominio.EstadoJuego;
import com.tallerwebi.dominio.dto.JoinMensaje;
import com.tallerwebi.dominio.dto.MensajeBingo;
import com.tallerwebi.dominio.dto.MensajeJugador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

public class ControladorMensajes {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private HttpSession session;

/**
 * Manager for the Bingo games.
 */

    private final BingoManager bingoManager = new BingoManager();
    @MessageMapping("/game.join")
    @SendTo("/topic/game.state")
    public Object joinGame(@Payload JoinMensaje message, SimpMessageHeaderAccessor headerAccessor) {
        BingoMultijugador game = (BingoMultijugador) bingoManager.joinGame(message.getPlayer());
        if (game == null) {
            MensajeBingo errorMessage = new MensajeBingo();
            errorMessage.setType("error");
            errorMessage.setContent("No fue posible entrar al juego. El juego está lleno o es un error interno.");
            return errorMessage;
        }
        headerAccessor.getSessionAttributes().put("gameId", game.getGameId());
        headerAccessor.getSessionAttributes().put("player", message.getPlayer());

        MensajeBingo gameMessage= gameToMessage(game);
        gameMessage.setType("game.joined");
        return gameMessage;
    }



/**
 * Handles a request from a client to leave a Tic-Tac-Toe game.
 * If the player is successfully removed from the game, a message is sent to subscribers
 * of the game's topic indicating that the player has left.
 *
 * @param message the message from the client containing the player's name
 */


@MessageMapping("/game.leave")
public void leaveGame(@Payload MensajeJugador message) {
    BingoMultijugador game = bingoManager.abandonarJuego(message.getPlayer()); //leaveGame
    if (game != null) {
        MensajeBingo gameMessage = gameToMessage(game);
        gameMessage.setType("game.left");
        messagingTemplate.convertAndSend("/topic/game." + game.getGameId(), gameMessage);
    }
}

/**
 * Handles a request from a client to make a move in a Bingo game.
 * If the move is valid, the game state is updated and sent to all subscribers of the game's topic.
 * If the game is over, a message is sent indicating the result of the game.
 *
 * @param message the message from the client containing the player's name, game ID, and move
 */
@MessageMapping("/game.move")
public void makeMove(@Payload MensajeBingo message) {
    String player = message.getSender();
    String gameId = message.getGameId();
    int move = message.getMove();
    BingoMultijugador game = bingoManager.getGame(gameId);

    if (game == null || game.isGameOver()) {
        MensajeBingo errorMessage = new MensajeBingo();
        errorMessage.setType("error");
        errorMessage.setContent("Game not found or is already over.");
        this.messagingTemplate.convertAndSend("/topic/game." + gameId, errorMessage);
        return;
    }

    if (game.getGameState().equals(EstadoJuego.WAITING_FOR_PLAYER)) {
        MensajeBingo errorMessage = new MensajeBingo();
        errorMessage.setType("error");
        errorMessage.setContent("El juego está esperando que se una un nuevo jugador...");
        this.messagingTemplate.convertAndSend("/topic/game." + gameId, errorMessage);
        return;
    }

    if (game.getTurn().equals(player)) {
        Set<Integer> numerosEntregados = (Set<Integer>) session.getAttribute("numerosEntregadosDeLaSesion");
        if (numerosEntregados == null) {
            numerosEntregados = new HashSet<>();
        }

        MensajeBingo numerosEntregadosMessage = new MensajeBingo();
        numerosEntregadosMessage.setType("game.numerosEntregados");
        numerosEntregadosMessage.setContent(numerosEntregados.toString());
        this.messagingTemplate.convertAndSend("/topic/game." + gameId, numerosEntregadosMessage);

        if (game.isGameOver()) {
            MensajeBingo gameOverMessage = gameToMessage(game);
            gameOverMessage.setType("game.gameOver");
            this.messagingTemplate.convertAndSend("/topic/game." + gameId, gameOverMessage);
            bingoManager.eliminarJuego(gameId);
        }
    }
}

    @EventListener
    public void SessionDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String gameId = headerAccessor.getSessionAttributes().get("gameId").toString();
        String player = headerAccessor.getSessionAttributes().get("player").toString();
        BingoMultijugador game = bingoManager.getGame(gameId);
        if (game != null) {
            if (game.getNombreJugador().equals(player)) {
                game.setNombreJugador(null);
                if (game.getNombreJugador2() != null) {
                    game.setGameState(EstadoJuego.PLAYER2_WON);
                    game.setWinner(game.getNombreJugador2());
                } else {
                    bingoManager.eliminarJuego(gameId);
                }
            } else if (game.getNombreJugador2() != null && game.getNombreJugador2().equals(player)) {
                game.setNombreJugador2(null);
                if (game.getNombreJugador() != null) {
                    game.setGameState(EstadoJuego.PLAYER1_WON);
                    game.setWinner(game.getNombreJugador());
                } else {
                    bingoManager.eliminarJuego(gameId);
                }
            }
            MensajeBingo gameMessage = gameToMessage(game);
            gameMessage.setType("game.gameOver");
            messagingTemplate.convertAndSend("/topic/game." + gameId, gameMessage);
            bingoManager.eliminarJuego(gameId);
        }
    }

    private MensajeBingo gameToMessage(BingoMultijugador game) {
        MensajeBingo message = new MensajeBingo();
        message.setGameId(game.getGameId());
        message.setPlayer1(game.getNombreJugador());
        message.setPlayer2(game.getNombreJugador2());
        message.setTurn(game.getTurn());
        message.setGameState(game.getGameState());
        message.setWinner(game.getWinner());
        return message;
    }

}

