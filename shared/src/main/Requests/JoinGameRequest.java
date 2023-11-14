package Requests;

import Models.AuthData;
import chess.ChessGame;

/**
 * Container consisting of all items that are needed to attempt joining a game.
 */
public class JoinGameRequest {
    /**
     * ID of game to attempt to join.
     */
    int gameIdToJoin;
    /**
     * User object correlating to user attempting to join a game.
     */
    AuthData token;
    ChessGame.TeamColor color;

    public int getGameIdToJoin() {
        return gameIdToJoin;
    }

    public AuthData getUserJoining() {
        return token;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }

    /**
     * creates a new Request to join a specific game.
     *
     * @param gameIdToJoin The database ID of the game that is being attempted to join.
     * @param userJoining  The User class, recieved from UserDAO, of the person trying to join.
     */
    public JoinGameRequest(int gameIdToJoin, AuthData userJoining, ChessGame.TeamColor color) {
        this.gameIdToJoin = gameIdToJoin;
        this.token = userJoining;
        this.color = color;
    }
}
