package messages;

import chess.ChessGame;
import chess.ChessPiece;
import chessGame.ChessMoveImp;
import chessGame.ChessPositionImp;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

public class UserMessageAdapter extends TypeAdapter<UserGameCommand> {
    @Override
    public void write(JsonWriter jw, UserGameCommand ugc) throws IOException {
        jw.beginObject();
        jw.name("commandType");
        jw.value(ugc.getCommandType().toString());

        switch (ugc.getCommandType()) {
            case LEAVE:
                leave lv = (leave) ugc;
                jw.name("gameID");
                jw.value(lv.gameID);
                jw.name("authToken");
                jw.value(lv.getAuthString());
                break;
            case RESIGN:
                resign rs = (resign) ugc;
                jw.name("gameID");
                jw.value(rs.gameID);
                jw.name("authToken");
                jw.value(rs.getAuthString());
                break;
            case MAKE_MOVE:
                makeMove mm = (makeMove) ugc;
                jw.name("gameID");
                jw.value(mm.gameID);
                jw.name("authToken");
                jw.value(mm.getAuthString());
                jw.name("move");
                jw.beginArray();
                jw.value(mm.move.getStartPosition().getRow());
                jw.value(mm.move.getStartPosition().getColumn());
                jw.value(mm.move.getEndPosition().getRow());
                jw.value(mm.move.getEndPosition().getColumn());
                jw.value(mm.move.getPromotionPiece().toString());
                jw.endArray();
                break;
            case JOIN_PLAYER:
                JoinPlayer jp = (JoinPlayer) ugc;
                jw.name("gameID");
                jw.value(jp.gameID);
                jw.name("authToken");
                jw.value(jp.getAuthString());
                jw.name("playerColor");
                jw.value(jp.playerColor == ChessGame.TeamColor.WHITE);
                break;
            case JOIN_OBSERVER:
                joinObserver jo = (joinObserver) ugc;
                jw.name("gameID");
                jw.value(jo.gameID);
                jw.name("authToken");
                jw.value(jo.getAuthString());
                break;
        }
        jw.endObject();
    }

    @Override
    public UserGameCommand read(JsonReader jsonReader) throws IOException {
        UserGameCommand output = null;
        jsonReader.beginObject();
        String auth = null;
        int gameID = -1;
        UserGameCommand.CommandType ct = null;
        String name = null;
        while (jsonReader.hasNext()) {
            JsonToken token = jsonReader.peek();
            if (token.equals(JsonToken.NAME)) {
                name = jsonReader.nextName();
            }
            if (name.equals("gameID")) {
                gameID = jsonReader.nextInt();
            }
            if (name.equals("authToken")) {
                auth = jsonReader.nextString();
            }
            if (name.equals("NAME")) {
                jsonReader.nextString();
            }
            if (name.equals("commandType")) {
                ct = UserGameCommand.CommandType.valueOf(jsonReader.nextString());

            }
            if (ct != null) {
                switch (ct) {
                    case JOIN_PLAYER -> {
                        if (name.equals("playerColor")) {
                            if (jsonReader.nextString().equals("WHITE")) {
                                output = new JoinPlayer(auth, gameID, ChessGame.TeamColor.WHITE);
                            } else {
                                output = new JoinPlayer(auth, gameID, ChessGame.TeamColor.BLACK);
                            }
                        }

                    }
                    case MAKE_MOVE -> {
                        if (name.equals("move")) {
                            jsonReader.beginArray();
                            output = new makeMove(auth, gameID, new ChessMoveImp(new ChessPositionImp(jsonReader.nextInt(), jsonReader.nextInt()),
                                    new ChessPositionImp(jsonReader.nextInt(), jsonReader.nextInt()),
                                    ChessPiece.PieceType.valueOf(jsonReader.nextString())));
                        }

                    }
                    case LEAVE -> {
                        if (gameID != -1 && auth != null) {
                            output = new leave(auth, gameID);
                        }
                    }
                    case RESIGN -> {
                        if (gameID != -1 && auth != null) {
                            output = new resign(auth, gameID);
                        }
                    }
                }
            }

        }
        jsonReader.endObject();
        return output;
    }
}
