package Server.DataAccess;

import Server.Models.Game;
import dataAccess.DataAccessException;

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
    Set<Game> games;

    /**
     * Opens the interface. FIXME will implement server instead of hashmap
     */
    public GameDAO() {
        games = new HashSet<>();
    }

    /**
     * attempts to insert a game into the database.
     *
     * @param game game to be added to the database
     * @return whether or not the game object was successfully added.
     * @throws DataAccessException if the database is unable to be accessed.
     */
    public boolean insertGame(Game game) throws DataAccessException {
        return games.add(game);
    }

    /**
     * tries to find a game from the database.
     *
     * @param gameID ID of the game to be found.
     * @return the Game object of the found Game. Returns null if not found.
     * @throws DataAccessException if the database is unable to be accessed.
     */
    public Game findGame(int gameID) throws DataAccessException {
        for (Game g: games) {
            if(g.getGameID() == gameID){
                return g;
            }
        }
        return null;
    }

    public void clear() {
        games = new HashSet<>();
    }

    public Set<Game> findall() {
        return games;
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
            for (Game t : games) {
                if (t.getGameID() == result) {
                    canLeave = false;
                }
            }
        }
        return result;
    }
}
