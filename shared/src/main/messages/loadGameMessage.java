package messages;

import webSocketMessages.serverMessages.ServerMessage;

public class loadGameMessage extends ServerMessage {
    public String game;

    public loadGameMessage(String game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }
}
