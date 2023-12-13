package messages;

import chessGame.ChessMoveImp;
import webSocketMessages.userCommands.UserGameCommand;

public class makeMove extends UserGameCommand {
    int gameID;
    ChessMoveImp move;
    public makeMove(String authToken, int gameID, ChessMoveImp move) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.move = move;
        this.gameID = gameID;
    }
}
