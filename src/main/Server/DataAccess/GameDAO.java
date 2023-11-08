package Server.DataAccess;

import Server.Models.Game;
import chess.ChessBoard;
import chess.ChessPiece;
import chessGame.ChessBoardImp;
import chessGame.ChessGameImp;
import chessGame.ChessPieceImp;
import com.google.gson.*;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class UnitTests {
    @Test
    public void clearWorksTest() throws DataAccessException {
        Database db = new Database();
        GameDAO games = new GameDAO(db);
        games.clear();
        games.insertGame(new Game(1));
        assertEquals(1, getSizeOfGames(db));
        games.clear();
        assertEquals(0, getSizeOfGames(db));
    }

    private int getSizeOfGames(Database db) throws DataAccessException {
        try (var conn = db.getConnection()) {
            var qury = conn.prepareStatement("""            
                        SELECT COUNT(1) FROM games
                    """);
            var rs = qury.executeQuery();
            rs.next();
            return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("DB error");
        }
    }

    @Test
    public void insertWorksTest() throws DataAccessException {
        Database db = new Database();
        GameDAO games = new GameDAO(db);
        assertNull(games.findGame(2));
        games.insertGame(new Game(2));
        assertNotNull(games.findGame(2));
    }

    @Test
    public void insertFailsTest() throws DataAccessException {
        Database db = new Database();
        GameDAO games = new GameDAO(db);
        games.clear();
        try {
            games.insertGame(new Game(1));
            assertFalse(games.insertGame(new Game(1)));
        } catch (Exception e) {
            //it is supposed to return an error. It isn't supposed to happen. Right?
        }
    }

    @Test
    public void findSuccTest() throws DataAccessException {
        Database db = new Database();
        GameDAO games = new GameDAO(db);
        games.clear();
        games.insertGame(new Game(1));
        assertNotNull(games.findGame(1));
    }

    @Test
    public void findFailTest() throws DataAccessException {
        Database db = new Database();
        GameDAO games = new GameDAO(db);
        games.clear();
        assertNull(games.findGame(1));
    }

    @Test
    public void finallSucTest() throws DataAccessException {
        Database db = new Database();
        GameDAO games = new GameDAO(db);
        games.clear();
        assertEquals(0, games.findall().size());
        games.insertGame(new Game(1));
        assertEquals(1, games.findall().size());
    }

    @Test
    public void finallFalTest() throws DataAccessException {
        Database db = new Database();
        GameDAO games = new GameDAO(db);
        games.clear();
        assertEquals(0, games.findall().size());
        boolean b = false;
        games.insertGame(new Game(1));
        try {
            games.insertGame(new Game(1));
        } catch (Exception e) {
            //I know it errors. It is supposed to.
            b = true;
        }
        assertTrue(b);
        assertEquals(1, games.findall().size());
    }
}


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


    class BoardAdapter implements JsonDeserializer<ChessBoard> {
        @Override
        public ChessBoard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            var builder = new GsonBuilder();
            builder.registerTypeAdapter(ChessPiece.class, new PieceAdapter());
            return new Gson().fromJson(jsonElement, ChessBoardImp.class);
        }
    }

    class PieceAdapter implements JsonDeserializer<ChessPiece> {
        @Override
        public ChessPiece deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new Gson().fromJson(jsonElement, ChessPieceImp.class);
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
            preparedStatement.setString(5, new Gson().toJson(game.getGame()));
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

        builder.registerTypeAdapter(ChessBoard.class, new BoardAdapter());
        builder.registerTypeAdapter(ChessPiece.class, new PieceAdapter());

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

    public ArrayList<Game> findall() throws DataAccessException { //FIXME
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
                }
            }
        }
        return result;
    }
}
