package messages;

import webSocketMessages.userCommands.UserGameCommand;

public class joinObserver extends UserGameCommand {
    public int gameID;
    public joinObserver(String authToken, int id) {
        super(authToken);
        this.commandType = CommandType.JOIN_OBSERVER;
    }
}
