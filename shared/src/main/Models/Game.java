package Models;

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
    String whiteUsername;
    /**
     * username of player controlling black side of board.
     */
    String blackUsername;
    /**
     * String containing title of game.
     */
    String gameName;
    /**
     * Representation of chess game. To avoid being overly saturated,
     * will likely replace with something else that gives all information neccesary, but without excess.
     */
    ChessGame game;

    /**
     * creates a new "game" with unique ID and no players.
     */
    public Game(int id) {
        gameID = id;
        blackUsername = null;
        whiteUsername = null;
        gameName = "";
        game = new ChessGameImp();
        game.getBoard().resetBoard();
    }

    public Game(int gameID, String gameName, String wU, String bU, ChessGame chessGame) {
        this.gameName = gameName;
        this.gameID = gameID;
        this.whiteUsername = wU;
        this.blackUsername = bU;
        this.game = chessGame;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessGame getGame() {
        return game;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    /**
     * Attempts to set an empty player slot for white to the username of the player given.
     *
     * @param newUsername username to replace null value.
     * @return true if operation is successful, false otherwise.
     */
    public boolean setUsernameW(String newUsername) {
        if (whiteUsername == null) {
            whiteUsername = newUsername;
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
        if (blackUsername == null) {
            blackUsername = newUsername;
            return true;
        }
        return false;
    }


    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }


}
