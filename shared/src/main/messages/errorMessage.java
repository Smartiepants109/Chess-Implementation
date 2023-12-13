package messages;

import webSocketMessages.serverMessages.ServerMessage;

public class errorMessage extends ServerMessage {
    String errorMessage;

    public errorMessage(String message) {
        super(ServerMessageType.ERROR);
        errorMessage = message;
    }
}
