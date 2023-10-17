package Server.Models;

import chess.ChessGame;
import chessGame.ChessGameImp;

public class Game {
    int gameID;
    String usernameW;
    String usernameB;
    String gameTitle;
    ChessGame game;

    public Game() {
        gameID = generateUniqueGameID();
        usernameB = "";
        usernameW = "";
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
     * @return true if operation is successful, false otherwise.
     * @param newUsername username to replace null value.
     *                    Attempts to set an empty player slot for white to the username of the player given.
     */
    public boolean setUsernameW(String newUsername) {
        if (usernameW == null) {
            usernameW = newUsername;
            return true;
        }
        return false;
    }

    /**
     * @return true if operation is successful, false otherwise.
     * @param newUsername username to replace null value
     *                    Attempts to set an empty player slot for black to the username of the player given.
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

    private int generateUniqueGameID() {
        return 0; //FIXME when have access to database, use to find ID that has not been used yet.
    }
}
