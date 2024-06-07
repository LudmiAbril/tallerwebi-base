package com.tallerwebi.dominio;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BingoManager {
    private final Map<String, BingoMultijugador> games;
    /**
     * Map of players waiting to join a Bingo game, with the player's name as the key.
     */
    protected final Map<String, String> waitingPlayers;

    public BingoManager() {
        games = new ConcurrentHashMap<>();
        waitingPlayers = new ConcurrentHashMap<>();
    }

    public synchronized BingoMultijugador joinGame(String player) {
        if (games.values().stream().anyMatch(game -> game.getNombreJugador().equals(player) || (game.getNombreJugador2() != null && game.getNombreJugador2().equals(player)))) {
            return games.values().stream().filter(game -> game.getNombreJugador().equals(player) || game.getNombreJugador2().equals(player)).findFirst().get();
        }

        for (BingoMultijugador game : games.values()) {
            if (game.getNombreJugador() != null && game.getNombreJugador2() == null) {
                game.setNombreJugador2(player);
                game.setGameState(EstadoJuego.PLAYER1_TURN);
                return game;
            }
        }
        //String player1, String player2
        BingoMultijugador game = new BingoMultijugador(player, null);
        games.put(game.getGameId(), game);
        waitingPlayers.put(player, game.getGameId());
        return game;
    }

    /**
     * Removes a player from their Bingo game. If the player was the only player in the game,
     * the game is removed.
     *
     * @param player the name of the player
     */
   public synchronized BingoMultijugador abandonarJuego(String player) {
        String gameId = getGameByPlayer(player) != null ? getGameByPlayer(player).getGameId() : null;
       Integer dimension;
        if (gameId != null) {
            waitingPlayers.remove(player);
            BingoMultijugador game = games.get(gameId);
            if (player.equals(game.getNombreJugador())) {
                if (game.getNombreJugador2() != null) {
                    game.setNombreJugador(game.getNombreJugador2());
                    game.setNombreJugador2(null);
                    game.setGameState(EstadoJuego.WAITING_FOR_PLAYER);
                    dimension = game.getDimension();

                    game.setCartonJugador1(game.servicioBingo.generarCarton(dimension)); //= ;
                    waitingPlayers.put(game.getNombreJugador(), game.getGameId());
                } else {
                    games.remove(gameId);
                    return null;
                }
            } else if (player.equals(game.getNombreJugador2())) {
                game.setNombreJugador2(null);
                game.setGameState(EstadoJuego.WAITING_FOR_PLAYER);
                dimension = game.getDimension();
                game.setCartonJugador2(game.servicioBingo.generarCarton(dimension));
                waitingPlayers.put(game.getNombreJugador(), game.getGameId());
            }
            return game;
        }
        return null;
    }

    /**
     * Returns the Bingo game with the given game ID.
     *
     * @param gameId the ID of the game
     * @return the Bingo game with the given game ID, or null if no such game exists
     */
    public BingoMultijugador getGame(String gameId) {
        return games.get(gameId);
    }

    /**
     * Returns the Bingo game the given player is in.
     *
     * @param player the name of the player
     * @return the Bingo game the given player is in, or null if the player is not in a game
     */
    public BingoMultijugador getGameByPlayer(String player) {
        return games.values().stream().filter(game -> game.getNombreJugador().equals(player) || (game.getNombreJugador2() != null &&
                game.getNombreJugador2().equals(player))).findFirst().orElse(null);
    }

    /**
     * Removes the Bingo game with the given game ID.
     *
     * @param gameId the ID of the game to remove
     */
    public void eliminarJuego(String gameId) {
        games.remove(gameId);
    }

}
