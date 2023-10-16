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

    public String getGameTitle() {
        return gameTitle;
    }

    private int generateUniqueGameID() {
        return 0; //FIXME when have access to database, use to find ID that has not been used yet.
    }
}
