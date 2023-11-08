package Server.Services;

import Server.DataAccess.AuthDAO;
import Server.DataAccess.GameDAO;
import Server.DataAccess.UserDAO;
import Server.Models.AuthData;
import Server.Models.Game;
import Server.Models.User;
import Server.Requests.CreateGameRequest;
import Server.Results.CreateGameResponse;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateUnitTests {
    Database db = new Database();

    @Test
    public void worksTest() throws DataAccessException {
        UserDAO users = new UserDAO(db);
        GameDAO games = new GameDAO(db);
        AuthDAO tokens = new AuthDAO(db);
        users.clear();
        games.clear();
        tokens.clear();
        users.insertUser(new User("user", "pass", "email"));
        tokens.insertToken(new AuthData("user", "e"));
        CreateGameService cs = new CreateGameService(users, games, tokens);
        assertEquals(0, cs.games.findall().size());
        cs.createGame(new CreateGameRequest("newgame", "user", "e"));
        assertEquals(1, cs.games.findall().size());
    }

    @Test
    public void failsTest() throws DataAccessException {
        UserDAO users = new UserDAO(db);
        GameDAO games = new GameDAO(db);
        AuthDAO tokens = new AuthDAO(db);
        users.clear();
        games.clear();
        tokens.clear();
        users.insertUser(new User("user", "pass", "email"));
        tokens.insertToken(new AuthData("user", "e"));
        CreateGameService cs = new CreateGameService(users, games, tokens);
        assertEquals(0, cs.games.findall().size());
        cs.createGame(new CreateGameRequest("newgame", "user", "f"));
        assertEquals(0, cs.games.findall().size());
        //fails to add with false authToken.
    }
}

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
        AuthData ad = new AuthData(r.getUserJoining(), r.getPw());
        if (!tokens.tokenValid(ad)) {
            return new CreateGameResponse(401, "Error: unauthorized");
        }
        Game game = new Game(games.generateUniqueID());
        game.setGameName(r.getGameName());
        games.insertGame(game);
        CreateGameResponse res = new CreateGameResponse(200, game.getGameID());
        return res;
    }
}
