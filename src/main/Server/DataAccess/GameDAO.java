package Server.DataAccess;

import Models.Game;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chessGame.ChessBoardImp;
import chessGame.ChessGameImp;
import chessGame.ChessPieceImp;
import chessGame.ChessPositionImp;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import dataAccess.DataAccessException;
import dataAccess.Database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;


/**
 * Data access object for all Game models on the server.
 */
public class GameDAO {
    /**
     * Database placeholder for DAO. Replacing later.
     */
    Database db;

    public boolean updateData(String varToChange, int gameID, String toChangeTo) throws DataAccessException {
        try (var conn = db.getConnection()) {
            var stmt = conn.prepareStatement("UPDATE games SET " + varToChange + " = ? WHERE gameID = ?");
            stmt.setString(1, toChangeTo);
            stmt.setInt(2, gameID);
            var rs = stmt.executeUpdate();
            return !(rs == 0);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error: db is corrupt");
        }
    }


    class BoardAdapter extends TypeAdapter<ChessGame> {

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


    /**
     * Opens the interface.
     */
    public GameDAO(Database database) {
        db = database;
    }

    /**
     * attempts to insert a game into the database.
     *
     * @param game game to be added to the database
     * @return whether or not the game object was successfully added.
     * @throws DataAccessException if the database is unable to be accessed.
     */
    public boolean insertGame(Game game) throws DataAccessException {
        try (var conn = db.getConnection()) {
            var preparedStatement = conn.prepareStatement("INSERT INTO games (gameID, gameName, whiteUsername, blackUsername, game) VALUES (?,?,?,?,?)");
            preparedStatement.setInt(1, game.getGameID());
            preparedStatement.setString(2, game.getGameName());
            preparedStatement.setString(3, game.getWhiteUsername());
            preparedStatement.setString(4, game.getBlackUsername());
            GsonBuilder gb = new GsonBuilder();
            gb.registerTypeAdapter(ChessGameImp.class, new BoardAdapter());
            preparedStatement.setString(5, gb.create().toJson(game.getGame()));
            var rs = preparedStatement.executeUpdate();
            return !(rs == 0);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error: db is corrupt");
        }
    }

    /**
     * tries to find a game from the database.
     *
     * @param gameID ID of the game to be found.
     * @return the Game object of the found Game. Returns null if not found.
     * @throws DataAccessException if the database is unable to be accessed.
     */
    public Game findGame(int gameID) throws DataAccessException {
        try (var conn = db.getConnection()) {
            var preparedStatement = conn.prepareStatement("SELECT game, gameName, whiteUsername, blackUsername FROM games WHERE gameID=?");
            preparedStatement.setInt(1, gameID);
            var rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String game = rs.getString("game");
                String gameName = rs.getString("gameName");
                String bU = rs.getString("blackUsername");
                String wU = rs.getString("whiteUsername");
                return DeserializeGame(gameID, gameName, wU, bU, game);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error: db is corrupt");
        }
    }

    private Game DeserializeGame(int gameID, String gameName, String wU, String bU, String game) {
        var builder = new GsonBuilder();

        builder.registerTypeAdapter(ChessGameImp.class, new BoardAdapter());

        return new Game(gameID, gameName, wU, bU, builder.create().fromJson(game, ChessGameImp.class));
    }

    public void clear() throws DataAccessException {
        try (var conn = db.getConnection()) {
            var preparedStatement = conn.prepareStatement("TRUNCATE TABLE games");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error: db is corrupt");
        }
    }

    public ArrayList<Game> findall() throws DataAccessException {
        try (var conn = db.getConnection()) {
            var preparedStatement = conn.prepareStatement("SELECT gameID, game, gameName, whiteUsername, blackUsername FROM games");
            var rs = preparedStatement.executeQuery();
            ArrayList<Game> gameSet = new ArrayList<>();
            while (rs.next()) {
                String game = rs.getString("game");
                String gameName = rs.getString("gameName");
                String bU = rs.getString("blackUsername");
                String wU = rs.getString("whiteUsername");
                int gameID = rs.getInt("gameID");


                gameSet.add(DeserializeGame(gameID, gameName, wU, bU, game));
            }
            return gameSet;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error: db is corrupt");
        }
    }

    public int generateUniqueID() throws DataAccessException {
        Random r = new Random();
        int result = 0;
        boolean canLeave = false;
        while (!canLeave) {
            canLeave = true;
            result = r.nextInt();
            if (result < 0) {
                result = result * -1;
            }
            for (Game t : findall()) {
                if (t.getGameID() == result) {
                    canLeave = false;
                    break;
                }
            }
        }
        return result;
    }
}
