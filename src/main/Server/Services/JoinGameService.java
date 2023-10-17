package Server.Services;

import Server.DataAccess.GameDAO;
import Server.DataAccess.UserDAO;
import Server.Requests.JoinGameRequest;
import Server.Results.JoinGameResponse;
import dataAccess.DataAccessException;

public class JoinGameService {
    UserDAO usersOnServer;
    GameDAO gamesOnServer;

    /**
     * @param users DAO with all users on it
     * @param games DAO with all of the open games
     *              Starts up the game service. Make one, use it only.
     * @throws DataAccessException if database is unable to be accessed.
     */
    public JoinGameService(UserDAO users, GameDAO games) throws DataAccessException {
        usersOnServer = users;
        gamesOnServer = games;
    }

    /**
     * @param r request to join certain game for certain user.
     * @return response state from database.
     * @throws DataAccessException if database's connection was unable to be maintained.
     */
    public JoinGameResponse joinGame(JoinGameRequest r) throws DataAccessException {
        return null;
    }


}
