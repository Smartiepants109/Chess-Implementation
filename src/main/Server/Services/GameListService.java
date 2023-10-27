package Server.Services;

import Server.DataAccess.AuthDAO;
import Server.DataAccess.GameDAO;
import Server.DataAccess.UserDAO;
import Server.Models.AuthData;
import Server.Models.User;
import Server.Requests.GameListRequest;
import Server.Results.GameListResponse;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


class ListUnitTests {
    @Test
    public void worksTest() throws DataAccessException {
        UserDAO users = new UserDAO();
        GameDAO games = new GameDAO();
        AuthDAO tokens = new AuthDAO();
        users.insertUser(new User("user", "pass", "email"));
        tokens.insertToken(new AuthData("user", "e"));
        GameListService cs = new GameListService(users, games, tokens);
        GameListResponse glr = cs.listGames(new GameListRequest("user", "e"));
        assertEquals(200, glr.getCode());
    }

    @Test
    public void failsTest() throws DataAccessException {
        UserDAO users = new UserDAO();
        GameDAO games = new GameDAO();
        AuthDAO tokens = new AuthDAO();
        users.insertUser(new User("user", "pass", "email"));
        tokens.insertToken(new AuthData("user", "e"));
        GameListService cs = new GameListService(users, games, tokens);
        GameListResponse glr = cs.listGames(new GameListRequest("user", "f"));
        assertNotEquals(200, glr.getCode());
    }
}


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
