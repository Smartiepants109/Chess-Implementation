package Server.Services;

import Server.DataAccess.AuthDAO;
import Server.DataAccess.GameDAO;
import Server.DataAccess.UserDAO;
import Models.AuthData;
import Models.Game;
import Requests.CreateGameRequest;
import Results.CreateGameResponse;
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
