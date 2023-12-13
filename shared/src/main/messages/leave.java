package messages;

import webSocketMessages.userCommands.UserGameCommand;

public class leave extends UserGameCommand {
    int gameID;

    public leave(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.LEAVE;
        this.gameID = gameID;
    }
}
