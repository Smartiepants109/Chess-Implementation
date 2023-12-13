package messages;

import webSocketMessages.serverMessages.ServerMessage;

public class notificationMessage extends ServerMessage {

    public String message;

    public notificationMessage(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }
}
