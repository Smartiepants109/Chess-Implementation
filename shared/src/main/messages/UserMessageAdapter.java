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
                jw.beginObject();
                jw.name("start");
                jw.beginObject();
                jw.name("row");
                jw.value(mm.move.getStartPosition().getRow());
                jw.name("column");
                jw.value(mm.move.getStartPosition().getColumn());
                jw.endObject();
                jw.name("end");
                jw.beginObject();
                jw.name("row");
                jw.value(mm.move.getEndPosition().getRow());
                jw.name("column");
                jw.value(mm.move.getEndPosition().getColumn());
                jw.endObject();
                jw.endObject();
                jw.name("promotion");
                jw.value(mm.move.getPromotionPiece().toString());

                jw.endObject();
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
                                jsonReader.endObject();
                                return output;
                            } else {
                                output = new JoinPlayer(auth, gameID, ChessGame.TeamColor.BLACK);
                                jsonReader.endObject();
                                return output;
                            }
                        }

                    }
                    case JOIN_OBSERVER -> {
                        if (gameID != -1 && auth != null) {
                            output = new joinObserver(auth, gameID);
                            jsonReader.endObject();
                            return output;
                        }
                    }
                    case MAKE_MOVE -> {
                        if (name.equals("move")) {
                            jsonReader.beginObject();
                            int bx, by, ex, ey;
                            jsonReader.nextName();
                            jsonReader.beginObject();
                            jsonReader.nextName();
                            bx = jsonReader.nextInt();
                            jsonReader.nextName();
                            by = jsonReader.nextInt();
                            jsonReader.endObject();
                            jsonReader.nextName();
                            jsonReader.beginObject();
                            jsonReader.nextName();
                            ex = jsonReader.nextInt();
                            jsonReader.nextName();
                            ey = jsonReader.nextInt();
                            jsonReader.endObject();
                            jsonReader.endObject();
                            ChessPiece.PieceType pt = null;
                            if (jsonReader.peek().equals(JsonToken.NAME)) {
                                jsonReader.nextName();
                                pt = ChessPiece.PieceType.valueOf(jsonReader.nextString());
                            }

                            jsonReader.endObject();
                            output = new makeMove(auth, gameID, new ChessMoveImp(new ChessPositionImp(bx + 1, by + 1),
                                    new ChessPositionImp(ex + 1, ey + 1), pt));

                            return output;
                        }

                    }
                    case LEAVE -> {
                        if (gameID != -1 && auth != null) {
                            output = new leave(auth, gameID);
                            jsonReader.endObject();
                            return output;
                        }
                    }
                    case RESIGN -> {
                        if (gameID != -1 && auth != null) {
                            output = new resign(auth, gameID);
                            jsonReader.endObject();
                            return output;
                        }
                    }
                }
            }

        }
        jsonReader.endObject();
        return output;
    }
}
