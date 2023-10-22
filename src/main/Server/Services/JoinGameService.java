package Server.Services;

import Server.DataAccess.GameDAO;
import Server.DataAccess.UserDAO;
import Server.Requests.JoinGameRequest;
import Server.Results.JoinGameResponse;
import dataAccess.DataAccessException;

/**
 * Class to handle joining games on the server.
 */
public class JoinGameService {
    /**
     * data access object for all users that have been registered on the server.
     */
    UserDAO usersOnServer;
    /**
     * data access object for all games on the server's database.
     */
    GameDAO gamesOnServer;

    /**
     * Starts up the game service. Make one, use it only.
     *
     * @param users DAO with all users on it
     * @param games DAO with all the games on it
     * @throws DataAccessException if database is unable to be accessed.
     */
    protected JoinGameService(UserDAO users, GameDAO games) throws DataAccessException {
        usersOnServer = users;
        gamesOnServer = games;
    }

    /**
     * attempts to join a game.
     *
     * @param r request to join certain game for certain user.
     * @return response state from database.
     * @throws DataAccessException if database's connection was unable to be maintained.
     */
    public JoinGameResponse joinGame(JoinGameRequest r) throws DataAccessException {
        return null;
    }


}
