package Server.Services;

import Server.DataAccess.AuthDAO;
import Server.DataAccess.GameDAO;
import Server.DataAccess.UserDAO;
import Server.Requests.ClearRequest;
import Server.Requests.LoginRequest;
import Server.Results.ClearResponse;
import Server.Results.LoginResponse;
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
     * @param r request object containing information for clear attempt.
     * @return whether or not you cleared correctly.
     * @throws DataAccessException if database's connection was unable to be maintained.
     */
    public ClearResponse login(ClearRequest r) throws DataAccessException {
        return null;
    }
}
