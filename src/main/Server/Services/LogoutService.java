package Server.Services;

import Server.DataAccess.AuthDAO;
import Server.DataAccess.GameDAO;
import Server.DataAccess.UserDAO;
import Models.AuthData;
import Requests.LogoutRequest;
import Results.LogoutResponse;
import dataAccess.DataAccessException;




/**
 * Service. As named.
 */
public class LogoutService {


    UserDAO users;
    GameDAO games;
    AuthDAO tokens;

    /**
     * Creates the service.
     *
     * @param userDAO database access object for user information.
     * @throws DataAccessException if database is unable to be accessed.
     */
    public LogoutService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) throws DataAccessException {
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
    public LogoutResponse logout(LogoutRequest r) throws DataAccessException {
        String token = r.getToken();
        String username = r.getUsername();
        AuthData t = tokens.findToken(username);
        if (t == null) {
            return new LogoutResponse(401, "Error: Unauthorized. Related User not found");
        }
        if (t.getAuthToken().equals(token)) {
            tokens.remove(tokens.findToken(username));
            return new LogoutResponse(200, null);
        } else {
            return new LogoutResponse(401, "Error: invalid credentials.");
        }
    }
}
