package Server.Services;

import Server.DataAccess.AuthDAO;
import Server.DataAccess.GameDAO;
import Server.DataAccess.UserDAO;
import Server.Requests.ClearRequest;
import Server.Requests.CreateGameRequest;
import Server.Results.ClearResponse;
import Server.Results.CreateGameResponse;
import dataAccess.DataAccessException;

/**
 * Service. As named.
 */
public class CreateGameService {


    UserDAO users;
    GameDAO games;
    AuthDAO tokens;

    /**
     * Creates the service.
     *
     * @param userDAO database access object for user information.
     * @throws DataAccessException if database is unable to be accessed.
     */
    public CreateGameService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) throws DataAccessException {
        users = userDAO;
        games = gameDAO;
        tokens = authDAO;
    }

    /**
     * Attempts to perform the service.
     *
     * @param r request object containing information for attempt.
     * @return response containing whether operation was successful.
     * @throws DataAccessException if database's connection was unable to be maintained.
     */
    public CreateGameResponse createGame(CreateGameRequest r) throws DataAccessException {

        return null;
    }
}
