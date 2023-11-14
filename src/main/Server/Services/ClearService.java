package Server.Services;

import Server.DataAccess.AuthDAO;
import Server.DataAccess.GameDAO;
import Server.DataAccess.UserDAO;
import Requests.ClearRequest;
import Results.ClearResponse;
import dataAccess.DataAccessException;


/**
 * Service in charge of logging you into your account.
 */
public class ClearService {

    /**
     * Database access object for User database.
     */
    UserDAO users;
    GameDAO games;
    AuthDAO tokens;

    /**
     * Creates the clear service.
     *
     * @param userDAO database access object for user information.
     * @throws DataAccessException if database is unable to be accessed.
     */
    public ClearService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) throws DataAccessException {
        users = userDAO;
        games = gameDAO;
        tokens = authDAO;
    }

    /**
     * Attempts to clear all everything.
     *
     * @param r request object containing information for attempt.
     * @return response containing whether operation was successful.
     * @throws DataAccessException if database's connection was unable to be maintained.
     */
    public ClearResponse clear(ClearRequest r) throws DataAccessException {
        users.clear();
        games.clear();
        tokens.clear();
        return new ClearResponse(200, "");
    }
}
