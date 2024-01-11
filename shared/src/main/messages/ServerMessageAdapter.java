package messages;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;

public class ServerMessageAdapter extends TypeAdapter<ServerMessage> {
    @Override
    public void write(JsonWriter jw, ServerMessage serverMessage) throws IOException {
        jw.beginObject();
        jw.name("ServerMessageType");
        var smt = serverMessage.getServerMessageType();
        jw.value(smt.ordinal());
        switch (smt) {
            case ERROR:
                var e = (errorMessage) serverMessage;
                jw.name("errorMessage");
                jw.value("error: " + e.errorMessage);
                break;
            case NOTIFICATION:
                var n = (notificationMessage) serverMessage;
                jw.name("message");
                jw.value(n.message);
                break;
            case LOAD_GAME:
                var l = (loadGameMessage) serverMessage;
                jw.name("game");
                jw.value(l.game);
                break;
        }
        jw.endObject();

    }

    @Override
    public ServerMessage read(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        String name = null;
        String contents = null;
        ServerMessage.ServerMessageType smt = null;
        while (jsonReader.hasNext()) {
            JsonToken token = jsonReader.peek();
            if (token.equals(JsonToken.NAME)) {
                name = jsonReader.nextName();
            }
            if (name.equals("serverMessageType")) {
                switch (jsonReader.nextString()) {
                    case "LOAD_GAME":
                        smt = ServerMessage.ServerMessageType.LOAD_GAME;
                        break;
                    case "NOTIFICATION":
                        smt = ServerMessage.ServerMessageType.NOTIFICATION;
                        break;
                    case "ERROR":
                        smt = ServerMessage.ServerMessageType.ERROR;
                        break;
                }
            }
            if (name.equals("ServerMessageType")) {
                token = jsonReader.peek();
                switch (jsonReader.nextInt()) {
                    case 0: // loadGame
                        smt = ServerMessage.ServerMessageType.LOAD_GAME;
                        break;
                    case 1: // error
                        smt = ServerMessage.ServerMessageType.ERROR;
                        break;
                    case 2: // notif
                        smt = ServerMessage.ServerMessageType.NOTIFICATION;
                        break;
                }
            }
            if (name.equals("game")) {
                contents = jsonReader.nextString();
            }
            if (name.equals("message")) {
                contents = jsonReader.nextString();
            }
            if (name.equals("errorMessage")) {
                contents = jsonReader.nextString();
            }
            if (jsonReader.peek().equals(JsonToken.END_OBJECT)) {
                jsonReader.endObject();
            }
        }
        switch (smt) {
            case LOAD_GAME -> {
                return new loadGameMessage(contents);
            }
            case ERROR -> {
                return new errorMessage(contents);
            }
            case NOTIFICATION -> {
                return new notificationMessage(contents);
            }
            default -> {
                return new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            }
        }
    }
}
