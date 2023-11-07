package Server.DataAccess;

import Server.Models.Game;
import chess.ChessGame;
import chessGame.ChessGameImp;
import com.google.gson.*;
import dataAccess.DataAccessException;
import dataAccess.Database;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Data access object for all Game models on the server.
 */
public class GameDAO {
    /**
     * Database placeholder for DAO. Replacing later.
     */
    Database db;

    class ChessAdapter implements JsonDeserializer<ChessGame> {
        @Override
        public ChessGame deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new Gson().fromJson(jsonElement, ChessGameImp.class);
        }
    }

    /**
     * Opens the interface. FIXME will implement server instead of hashmap
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
            db.returnConnection(conn);
            return !(rs == 0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
                db.returnConnection(conn);
                var builder = new GsonBuilder();
                builder.registerTypeAdapter(ChessGame.class, new ChessAdapter());

                return new Game(gameID, gameName, wU, bU, builder.create().fromJson(game, ChessGame.class));
            } else {
                db.returnConnection(conn);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clear() {
        try (var conn = db.getConnection()) {
            var preparedStatement = conn.prepareStatement("TRUNCATE games");
            preparedStatement.executeUpdate();
            db.returnConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Game> findall() {
        try (var conn = db.getConnection()) {
            var preparedStatement = conn.prepareStatement("SELECT gameID, game, gameName, whiteUsername, blackUsername FROM games");
            var rs = preparedStatement.executeQuery();
            HashSet<Game> gameSet = new HashSet<>();
            while (rs.next()) {
                String game = rs.getString("game");
                String gameName = rs.getString("gameName");
                String bU = rs.getString("blackUsername");
                String wU = rs.getString("whiteUsername");
                int gameID = rs.getInt("gameID");
                db.returnConnection(conn);
                var builder = new GsonBuilder();
                builder.registerTypeAdapter(ChessGame.class, new ChessAdapter());

                gameSet.add(new Game(gameID, gameName, wU, bU, builder.create().fromJson(game, ChessGame.class)));
            }
            return gameSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public int generateUniqueID() {
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
