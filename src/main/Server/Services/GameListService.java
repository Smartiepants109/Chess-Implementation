package Server.Services;

import Server.DataAccess.AuthDAO;
import Server.DataAccess.GameDAO;
import Server.DataAccess.UserDAO;
import Server.Requests.GameListRequest;
import Server.Results.GameListResponse;
import dataAccess.DataAccessException;



/**
 * Service. As named.
 */
public class GameListService {


    UserDAO users;
    GameDAO games;
    AuthDAO tokens;

    /**
     * Creates the service.
     *
     * @param userDAO database access object for user information.
     * @throws DataAccessException if database is unable to be accessed.
     */
    public GameListService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) throws DataAccessException {
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
    public GameListResponse listGames(GameListRequest r) throws DataAccessException {
        if (tokens.tokenValid(r.getToken())) {
            return new GameListResponse(200, games.findall());
        } else {
            return new GameListResponse(401, "Error: unauthorized");
        }
    }
}
