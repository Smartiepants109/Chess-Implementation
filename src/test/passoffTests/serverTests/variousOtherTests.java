package passoffTests.serverTests;


import Models.AuthData;
import Models.Game;
import Models.User;
import Requests.*;
import Results.*;
import Server.DataAccess.AuthDAO;
import Server.DataAccess.GameDAO;
import Server.DataAccess.UserDAO;
import Server.Services.*;
import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AuthUnitTests {
    @Test
    public void clearWorksTest() throws DataAccessException {
        Database db = new Database();
        AuthDAO auths = new AuthDAO(db);
        auths.clear();
        auths.insertToken(new AuthData("user", "totallyRealAuthData"));
        assertEquals(1, getSizeOfAuth(db));
        auths.clear();
        assertEquals(0, getSizeOfAuth(db));
    }

    private int getSizeOfAuth(Database db) throws DataAccessException {
        try (var conn = db.getConnection()) {
            var qury = conn.prepareStatement("""
                                    
                        SELECT COUNT(*) FROM auth
                    """);
            var rs = qury.executeQuery();
            rs.next();
            return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("DB error");
        }
    }

    @Test
    public void insertWorksTest() throws DataAccessException {
        Database db = new Database();
        AuthDAO auths = new AuthDAO(db);
        assertNull(auths.findToken("u"));
        auths.insertToken(new AuthData("u", "p"));
        assertNotNull(auths.findToken("u"));
    }

    @Test
    public void insertFailsTest() throws DataAccessException {
        Database db = new Database();
        AuthDAO auth = new AuthDAO(db);
        auth.clear();
        assertNull(auth.findToken("u"));
        auth.insertToken(new AuthData("u", "p"));
        boolean b = false;
        try {
            auth.insertToken(new AuthData("u", "p"));
        } catch (Exception e) {
            //supposed to throw error.
            b = true;
        }
        assertTrue(b);
    }

    @Test
    public void findSuccTest() throws DataAccessException {
        Database db = new Database();
        AuthDAO auths = new AuthDAO(db);
        auths.clear();
        auths.insertToken(new AuthData("u", "p"));
        assertNotNull(auths.findToken("u"));
    }

    @Test
    public void findFailTest() throws DataAccessException {
        Database db = new Database();
        AuthDAO auths = new AuthDAO(db);
        auths.clear();
        assertNull(auths.findToken("u"));
    }

    @Test
    public void findUFTSuccTest() throws DataAccessException {
        Database db = new Database();
        AuthDAO auths = new AuthDAO(db);
        auths.clear();
        auths.insertToken(new AuthData("u", "p"));
        assertNotNull(auths.findUsernameFromToken("p"));
    }

    @Test
    public void findUFTFailTest() throws DataAccessException {
        Database db = new Database();
        AuthDAO auths = new AuthDAO(db);
        auths.clear();
        assertNull(auths.findUsernameFromToken("p"));
    }

    @Test
    public void delSuccTest() throws DataAccessException {
        Database db = new Database();
        AuthDAO auths = new AuthDAO(db);
        auths.clear();
        auths.insertToken(new AuthData("u", "p"));
        assertTrue(auths.remove(new AuthData("u", "p")));
    }

    @Test
    public void delFailTest() throws DataAccessException {
        Database db = new Database();
        AuthDAO auths = new AuthDAO(db);
        auths.clear();
        assertFalse(auths.remove(new AuthData("u", "p")));

    }

    @Test
    public void tokenValidTest() throws DataAccessException {
        Database d = new Database();
        AuthDAO auths = new AuthDAO(d);
        auths.clear();
        auths.insertToken(new AuthData("u", "pw"));
        assertTrue(auths.tokenValid(new AuthData("u", "pw")));
    }

    @Test
    public void tokenInValidTest() throws DataAccessException {
        Database d = new Database();
        AuthDAO auths = new AuthDAO(d);
        auths.clear();
        auths.insertToken(new AuthData("u", "pw"));
        assertFalse(auths.tokenValid(new AuthData("alksdfjkdjfgh", "pw")));
    }

    @Test
    public void generateNewTokenTest() throws DataAccessException {
        Database d = new Database();
        AuthDAO auths = new AuthDAO(d);
        auths.clear();
        assertEquals(0, getSizeOfAuth(d));
        for (int i = 0; i < 200; i++) {
            assertEquals(i, getSizeOfAuth(d));
            auths.insertToken(new AuthData("pp", auths.generateNewToken()));
        }
    }
}

class GameUnitTests {
    @Test
    public void clearWorksTest() throws DataAccessException {
        Database db = new Database();
        GameDAO games = new GameDAO(db);
        games.clear();
        games.insertGame(new Game(1));
        assertEquals(1, getSizeOfGames(db));
        games.clear();
        assertEquals(0, getSizeOfGames(db));
    }

    private int getSizeOfGames(Database db) throws DataAccessException {
        try (var conn = db.getConnection()) {
            var qury = conn.prepareStatement("""            
                        SELECT COUNT(1) FROM games
                    """);
            var rs = qury.executeQuery();
            rs.next();
            return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("DB error");
        }
    }

    @Test
    public void insertWorksTest() throws DataAccessException {
        Database db = new Database();
        GameDAO games = new GameDAO(db);
        assertNull(games.findGame(2));
        games.insertGame(new Game(2));
        assertNotNull(games.findGame(2));
    }

    @Test
    public void insertFailsTest() throws DataAccessException {
        Database db = new Database();
        GameDAO games = new GameDAO(db);
        games.clear();
        try {
            games.insertGame(new Game(1));
            assertFalse(games.insertGame(new Game(1)));
        } catch (Exception e) {
            //it is supposed to return an error. It isn't supposed to happen. Right?
        }
    }

    @Test
    public void findSuccTest() throws DataAccessException {
        Database db = new Database();
        GameDAO games = new GameDAO(db);
        games.clear();
        games.insertGame(new Game(1));
        assertNotNull(games.findGame(1));
    }

    @Test
    public void findFailTest() throws DataAccessException {
        Database db = new Database();
        GameDAO games = new GameDAO(db);
        games.clear();
        assertNull(games.findGame(1));
    }

    @Test
    public void finallSucTest() throws DataAccessException {
        Database db = new Database();
        GameDAO games = new GameDAO(db);
        games.clear();
        assertEquals(0, games.findall().size());
        games.insertGame(new Game(1));
        assertEquals(1, games.findall().size());
    }

    @Test
    public void finallFalTest() throws DataAccessException {
        Database db = new Database();
        GameDAO games = new GameDAO(db);
        games.clear();
        assertEquals(0, games.findall().size());
        boolean b = false;
        games.insertGame(new Game(1));
        try {
            games.insertGame(new Game(1));
        } catch (Exception e) {
            //I know it errors. It is supposed to.
            b = true;
        }
        assertTrue(b);
        assertEquals(1, games.findall().size());
    }
}

class UserUnitTests {
    @Test
    public void clearWorksTest() throws DataAccessException {
        Database db = new Database();
        UserDAO users = new UserDAO(db);
        users.clear();
        users.insertUser(new User("user", "pw", "em"));
        assertEquals(1, getSizeOfUsers(db));
        users.clear();
        assertEquals(0, getSizeOfUsers(db));
    }

    private int getSizeOfUsers(Database db) throws DataAccessException {
        try (var conn = db.getConnection()) {
            var qury = conn.prepareStatement("""
                                    
                        SELECT COUNT(*) FROM users
                    """);
            var rs = qury.executeQuery();
            rs.next();
            return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("DB error");
        }
    }

    @Test
    public void insertWorksTest() throws DataAccessException {
        Database db = new Database();
        UserDAO users = new UserDAO(db);
        users.clear();
        assertNull(users.findToken("u"));
        users.insertUser(new User("u", "p", "e"));
        assertNotNull(users.findToken("u"));
    }

    @Test
    public void insertFailsTest() throws DataAccessException {
        Database db = new Database();
        UserDAO users = new UserDAO(db);
        assertNull(users.findToken("u"));
        users.insertUser(new User("u", "p", "e"));
        boolean b = false;
        try {
            assertFalse(users.insertUser(new User("u", "p", "e")));
        } catch (Exception e) {
            b = true;
        }
        assertTrue(b);
    }

    @Test
    public void findSuccTest() throws DataAccessException {
        Database db = new Database();
        UserDAO users = new UserDAO(db);
        users.clear();
        users.insertUser(new User("u", "p", "e"));
        assertNotNull(users.findToken("u"));
    }

    @Test
    public void findFailTest() throws DataAccessException {
        Database db = new Database();
        UserDAO users = new UserDAO(db);
        users.clear();
        assertNull(users.findToken("u"));
    }
}

class ClearUnitTests {
    @Test
    public void worksTest() throws DataAccessException {
        Database db = new Database();
        UserDAO users = new UserDAO(db);
        GameDAO games = new GameDAO(db);
        AuthDAO tokens = new AuthDAO(db);
        users.clear();
        games.clear();
        tokens.clear();
        users.insertUser(new User("user", "pass", "email"));
        games.insertGame(new Game(1));
        tokens.insertToken(new AuthData("user", "e"));
        ClearService cs = new ClearService(users, games, tokens);
        assertNotNull(users.findToken("user"));
        cs.clear(new ClearRequest());
        assertNull(users.findToken("user"));
    }
}

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
        assertEquals(0, games.findall().size());
        cs.createGame(new CreateGameRequest("newgame", "user", "e"));
        assertEquals(1, games.findall().size());
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
        assertEquals(0, games.findall().size());
        cs.createGame(new CreateGameRequest("newgame", "user", "f"));
        assertEquals(0, games.findall().size());
        //fails to add with false authToken.
    }
}

class ListUnitTests {
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
        GameListService cs = new GameListService(users, games, tokens);
        GameListResponse glr = cs.listGames(new GameListRequest("user", "e"));
        assertEquals(200, glr.getCode());
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
        GameListService cs = new GameListService(users, games, tokens);
        GameListResponse glr = cs.listGames(new GameListRequest("user", "f"));
        assertNotEquals(200, glr.getCode());
    }
}

class JoinUnitTests {
    Database db = new Database();

    @Test
    public void worksTest() throws DataAccessException {
        UserDAO users = new UserDAO(db);
        GameDAO games = new GameDAO(db);
        AuthDAO tokens = new AuthDAO(db);
        users.clear();
        games.clear();
        tokens.clear();
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
        UserDAO users = new UserDAO(db);
        GameDAO games = new GameDAO(db);
        AuthDAO tokens = new AuthDAO(db);
        users.clear();
        games.clear();
        tokens.clear();
        games.insertGame(new Game(1));
        users.insertUser(new User("user", "pass", "email"));
        tokens.insertToken(new AuthData("user", "e"));
        JoinGameService cs = new JoinGameService(users, games, tokens);
        JoinGameRequest jgrq = new JoinGameRequest(1, new AuthData("user", "f"), ChessGame.TeamColor.BLACK);
        JoinGameResponse glr = cs.joinGame(jgrq);
        assertEquals(401, glr.getstatcode());
    }
}

class LoginUnitTests {
    Database db = new Database();

    @Test
    public void worksTest() throws DataAccessException {
        UserDAO users = new UserDAO(db);
        AuthDAO tokens = new AuthDAO(db);
        users.clear();
        tokens.clear();
        LoginService ls = new LoginService(users, tokens);
        users.insertUser(new User("user", "pw", "em"));
        LoginResponse lr = ls.login(new LoginRequest("user", "pw"));
        assertEquals(200, lr.getResponseCode());
    }

    @Test
    public void failsTest() throws DataAccessException {
        UserDAO users = new UserDAO(db);
        AuthDAO tokens = new AuthDAO(db);
        users.clear();
        tokens.clear();
        LoginService ls = new LoginService(users, tokens);
        users.insertUser(new User("user", "pw", "em"));
        LoginResponse lr = ls.login(new LoginRequest("user", "lksadjflkajdshfgjershgljrshfd khdnochev hkjgerskjghnslfghlersi"));
        assertNotEquals(200, lr.getResponseCode());
    }
}

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

class RegiUnitTests {
    Database d = new Database();

    @Test
    public void worksTest() throws DataAccessException {
        UserDAO users = new UserDAO(d);
        AuthDAO tokens = new AuthDAO(d);
        users.clear();
        tokens.clear();
        RegistrationService rs = new RegistrationService(users, tokens);
        RegistrationResponse rr = rs.register(new RegistrationRequest("user", "pw", "email"));
        assertEquals(200, rr.getStatusCode());
    }

    @Test
    public void failsTest() throws DataAccessException {
        UserDAO users = new UserDAO(d);
        AuthDAO tokens = new AuthDAO(d);
        users.clear();
        tokens.clear();
        RegistrationService rs = new RegistrationService(users, tokens);
        RegistrationResponse rr = rs.register(new RegistrationRequest("user", "pw", "email"));
        rr = rs.register(new RegistrationRequest("user", "pw", "email"));
        //attempts to register twice. bad.
        assertEquals(403, rr.getStatusCode());
    }
}


public class variousOtherTests {
}
