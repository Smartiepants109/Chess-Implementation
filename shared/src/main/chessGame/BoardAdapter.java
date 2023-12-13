package chessGame;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class BoardAdapter extends TypeAdapter<ChessGame> {

    @Override
    public void write(JsonWriter jw, ChessGame board) throws IOException {
        jw.beginObject();
        jw.name("currentTurn");
        if (board.getTeamTurn().equals(ChessGame.TeamColor.WHITE)) {
            jw.value("WHITE"); //white is 1, black is 0.
        } else {
            jw.value("BLACK");
        }
        //now for the game board. hoo boy.
        jw.name("board");
        jw.beginArray();
        ChessBoard board1 = board.getBoard();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = board1.getPiece(new ChessPositionImp(i, j));
                jw.value(ChessPieceImp.GoToNum((ChessPieceImp) piece));
            }
        }
        jw.endArray();
        jw.endObject();

    }

    @Override
    public ChessGame read(JsonReader jsonReader) throws IOException {
        ChessGameImp cgi = new ChessGameImp();
        jsonReader.beginObject();
        String name = null;
        while (jsonReader.hasNext()) {
            JsonToken token = jsonReader.peek();
            if (token.equals(JsonToken.NAME)) {
                name = jsonReader.nextName();
            }
            if (name.equals("currentTurn")) {
                token = jsonReader.peek();
                cgi.setTeamTurn(jsonReader.nextString());
            }
            if (name.equals("board")) {
                ChessBoardImp cbi = new ChessBoardImp();
                token = jsonReader.peek();
                jsonReader.beginArray();
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        cbi.addPiece(new ChessPositionImp(i + 1, j + 1), ChessPieceImp.GetFromNum(jsonReader.nextInt()));
                    }
                }
                jsonReader.endArray();
                cgi.setBoard(cbi);
            }
        }
        jsonReader.endObject();
        return cgi;
    }
}
