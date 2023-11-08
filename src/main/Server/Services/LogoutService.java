package Server.Services;

import Server.DataAccess.AuthDAO;
import Server.DataAccess.GameDAO;
import Server.DataAccess.UserDAO;
import Server.Models.AuthData;
import Server.Models.User;
import Server.Requests.LogoutRequest;
import Server.Results.LogoutResponse;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class LogoutUnitTests {
    Database d = new Database();
    @Test
    public void worksTest() throws DataAccessException {
        UserDAO users = new UserDAO(d);
        AuthDAO tokens = new AuthDAO(d);
        GameDAO games = new GameDAO(d);
        users.clear();
        games.clear();
        tokens.clear();
        LogoutService ls = new LogoutService(users, games, tokens);
        users.insertUser(new User("user", "pw", "em"));
        tokens.insertToken(new AuthData("user", "e"));
        LogoutResponse lr = ls.logout(new LogoutRequest("user", "e"));
        assertEquals(200, lr.getStatusCode());
    }
    @Test
    public void failsTest() throws DataAccessException {
        UserDAO users = new UserDAO(d);
        AuthDAO tokens = new AuthDAO(d);
        GameDAO games = new GameDAO(d);
        users.clear();
        games.clear();
        tokens.clear();
        LogoutService ls = new LogoutService(users, games, tokens);
        users.insertUser(new User("user", "pw", "em"));
        tokens.insertToken(new AuthData("user", "e"));
        ls.logout(new LogoutRequest("user", "e"));
        LogoutResponse lr = ls.logout(new LogoutRequest("user", "e"));
        assertEquals(401, lr.getStatusCode());
    }
}

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
