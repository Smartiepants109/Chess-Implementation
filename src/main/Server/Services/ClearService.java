package Server.Services;

import Server.DataAccess.AuthDAO;
import Server.DataAccess.GameDAO;
import Server.DataAccess.UserDAO;
import Server.Models.AuthData;
import Server.Models.Game;
import Server.Models.User;
import Server.Requests.ClearRequest;
import Server.Results.ClearResponse;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitTests {
    @Test
    public void worksTest() throws DataAccessException {
        UserDAO users = new UserDAO();
        GameDAO games = new GameDAO();
        AuthDAO tokens = new AuthDAO();
        users.insertUser(new User("user", "pass", "email"));
        games.insertGame(new Game(1));
        tokens.insertToken(new AuthData("user", "e"));
        ClearService cs = new ClearService(users, games, tokens);
        assertNotNull(users.findToken("user"));
        cs.clear(new ClearRequest());
        assertNull(cs.users.findToken("user"));
    }
}

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
