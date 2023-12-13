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
        ServerMessage.ServerMessageType smt = null;
        while (jsonReader.hasNext()) {
            JsonToken token = jsonReader.peek();
            if (token.equals(JsonToken.NAME)) {
                name = jsonReader.nextName();
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
            } else {
                if (smt == null) {
                    throw new IOException("bad");
                } else {
                    switch (smt) {
                        case ERROR:
                            return new errorMessage(jsonReader.nextString());
                        case NOTIFICATION:
                            return new notificationMessage(jsonReader.nextString());
                        case LOAD_GAME:
                            return new loadGameMessage(jsonReader.nextString());
                    }
                }
            }
        }
        jsonReader.endObject();
        return new errorMessage("A loop in ServerMessageAdapter was exited. This means that the gson " +
                "parsed here didn't have all the information it should have. Check the class.");
    }
}
