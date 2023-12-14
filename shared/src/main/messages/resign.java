package messages;

import webSocketMessages.userCommands.UserGameCommand;

public class resign extends UserGameCommand {
    public int gameID;
    public resign(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.RESIGN;
        this.gameID = gameID;
    }
}
