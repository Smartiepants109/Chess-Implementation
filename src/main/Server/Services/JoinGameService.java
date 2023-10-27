package Server.Services;

import Server.DataAccess.AuthDAO;
import Server.DataAccess.GameDAO;
import Server.DataAccess.UserDAO;
import Server.Models.AuthData;
import Server.Models.Game;
import Server.Models.User;
import Server.Requests.JoinGameRequest;
import Server.Results.JoinGameResponse;
import chess.ChessGame;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class JoinUnitTests {
    @Test
    public void worksTest() throws DataAccessException {
        UserDAO users = new UserDAO();
        GameDAO games = new GameDAO();
        AuthDAO tokens = new AuthDAO();
        games.insertGame(new Game(1));
        users.insertUser(new User("user", "pass", "email"));
        tokens.insertToken(new AuthData("user", "e"));
        JoinGameService cs = new JoinGameService(users, games, tokens);
        JoinGameRequest jgrq = new JoinGameRequest(1, new AuthData("user", "e"), ChessGame.TeamColor.BLACK);
        JoinGameResponse glr = cs.joinGame(jgrq);
        assertEquals(200, glr.getstatcode());
    }

    @Test
    public void failsTest() throws DataAccessException {
        UserDAO users = new UserDAO();
        GameDAO games = new GameDAO();
        AuthDAO tokens = new AuthDAO();
        games.insertGame(new Game(1));
        users.insertUser(new User("user", "pass", "email"));
        tokens.insertToken(new AuthData("user", "e"));
        JoinGameService cs = new JoinGameService(users, games, tokens);
        JoinGameRequest jgrq = new JoinGameRequest(1, new AuthData("user", "f"), ChessGame.TeamColor.BLACK);
        JoinGameResponse glr = cs.joinGame(jgrq);
        assertEquals(401, glr.getstatcode());
    }
}

/**
 * Class to handle joining games on the server.
 */
public class JoinGameService {
    /**
     * data access object for all users that have been registered on the server.
     */
    UserDAO usersOnServer;
    /**
     * data access object for all games on the server's database.
     */
    GameDAO gamesOnServer;
    AuthDAO tokens;

    /**
     * Starts up the game service. Make one, use it only.
     *
     * @param users DAO with all users on it
     * @param games DAO with all the games on it
     * @throws DataAccessException if database is unable to be accessed.
     */
    public JoinGameService(UserDAO users, GameDAO games, AuthDAO tokens) throws DataAccessException {
        usersOnServer = users;
        gamesOnServer = games;
        this.tokens = tokens;
    }

    /**
     * attempts to join a game.
     *
     * @param r request to join certain game for certain user.
     * @return response state from database.
     * @throws DataAccessException if database's connection was unable to be maintained.
     */
    public JoinGameResponse joinGame(JoinGameRequest r) throws DataAccessException {
        if (tokens.tokenValid(r.getUserJoining())) {
            if (gamesOnServer.findGame(r.getGameIdToJoin()) == null) {
                return new JoinGameResponse(400, "Error: Game does not exist.");
            } else {
                Game g = (gamesOnServer.findGame(r.getGameIdToJoin()));
                if (r.getColor() == null) {
                    return new JoinGameResponse(200, null);
                }
                if (r.getColor().equals(ChessGame.TeamColor.WHITE)) {
                    if (g.getWhiteUsername() == null) {
                        g.setUsernameW(r.getUserJoining().getUsername());
                        return new JoinGameResponse(200, null);
                    } else {
                        return new JoinGameResponse(403, "Error: already taken");
                    }
                } else {
                    if (g.getBlackUsername() == null) {
                        g.setUsernameB(r.getUserJoining().getUsername());
                        return new JoinGameResponse(200, "");
                    } else {
                        return new JoinGameResponse(403, "Error: already taken");
                    }
                }
            }
        } else {
            return new JoinGameResponse(401, "Error: unauthorized");
        }
    }


}
