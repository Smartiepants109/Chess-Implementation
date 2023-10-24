package Server.Models;

import chess.ChessGame;
import chessGame.ChessGameImp;

/**
 * game model. Contains all information needed.
 */
public class Game {
    /**
     * unique ID identifier of game. Only one of each number should exist at a time.
     */
    int gameID;
    /**
     * Username of player controlling white side of board.
     */
    String usernameW;
    /**
     * username of player controlling black side of board.
     */
    String usernameB;
    /**
     * String containing title of game.
     */
    String gameTitle;
    /**
     * Representation of chess game. To avoid being overly saturated,
     * will likely replace with something else that gives all information neccesary, but without excess.
     */
    ChessGame game;

    /**
     * creates a new "game" with unique ID and no players.
     */
    public Game() {
        gameID = generateUniqueGameID();
        usernameB = null;
        usernameW = null;
        gameTitle = ""; // when and where would this be used? Confused. for now is blank. FIXME
        game = new ChessGameImp();
    }

    public String getUsernameB() {
        return usernameB;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessGame getGame() {
        return game; //? Where will main access point be? FIXME if not neccesary.
    }

    public String getUsernameW() {
        return usernameW;
    }

    /**
     * Attempts to set an empty player slot for white to the username of the player given.
     *
     * @param newUsername username to replace null value.
     * @return true if operation is successful, false otherwise.
     */
    public boolean setUsernameW(String newUsername) {
        if (usernameW == null) {
            usernameW = newUsername;
            return true;
        }
        return false;
    }

    /**
     * Attempts to set an empty player slot for black to the username of the player given.
     *
     * @param newUsername username to replace null value
     * @return true if operation is successful, false otherwise.
     */
    public boolean setUsernameB(String newUsername) {
        if (usernameB == null) {
            usernameB = newUsername;
            return true;
        }
        return false;
    }


    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    /**
     * generates a new game ID that is not repeated anywhere else.
     * @return a new, unique and never used before ID. Will likely be done inside the database for better handling.
     */
    public static int generateUniqueGameID() {
        return 0; //FIXME when have access to database, use to find ID that has not been used yet.
    }
}
