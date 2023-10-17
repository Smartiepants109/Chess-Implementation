package Server.DataAccess;

import Server.Models.Game;
import dataAccess.DataAccessException;

import java.util.HashSet;
import java.util.Set;

public class GameDAO {
    Set<Game> games = new HashSet<>();

    /**
     * @param game game to be added to the database
     * @return whether or not the game object was successfully added.
     * @throws DataAccessException if the database is unable to be accessed.
     */
    public boolean insertGame(Game game) throws DataAccessException {
        return games.add(game);
    }

    /**
     * @param gameID ID of the game to be found.
     * @return the Game object of the found Game.
     * @throws DataAccessException if the database is unable to be accessed.
     */
    public Game findGame(int gameID) throws DataAccessException {
        return null;
    }
}
