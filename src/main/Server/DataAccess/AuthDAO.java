package Server.DataAccess;

import Server.Models.AuthData;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;
import java.util.Set;

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
}


/**
 * DAO  for authentication tokens.
 */
public class AuthDAO {
    /**
     * current database. Will be updated later.
     */
    Set<AuthData> tokens;
    Database db;

    /**
     * generates a new Data Access Object.
     */
    public AuthDAO(Database db) {
        this.db = db;
    }

    /**
     * adds a token to the database.
     *
     * @param token a user's authToken to add to the database?
     * @return whether the insertion was successful.
     * @throws DataAccessException if the database was unable to be accessed.
     */
    public boolean insertToken(AuthData token) throws DataAccessException {
        try (var conn = db.getConnection()) {
            var preparedStatement = conn.prepareStatement("INSERT INTO auth (username, authToken) VALUES (?,?)");
            preparedStatement.setString(1, token.getUsername());
            preparedStatement.setString(2, token.getAuthToken());
            var rs = preparedStatement.executeUpdate();
            return !(rs == 0);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error: db is corrupt");
        }
    }

    /**
     * searches for a username - password combo using the username.
     *
     * @param username the username of the authToken that is to be found.
     * @return the AuthToken object that is found, null if not found.
     * @throws DataAccessException if the database itself is unable to be accessed.
     */
    public AuthData findToken(String username) throws DataAccessException {
        try (var conn = db.getConnection()) {
            var preparedStatement = conn.prepareStatement("SELECT authToken FROM auth WHERE username=?");
            preparedStatement.setString(1, username);
            var rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String token = rs.getString("authToken");
                return new AuthData(username, token);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error: db is corrupt");
        }


        /*for (AuthData a : tokens) {
            if (a.getUsername().equals(username)) {
                return a;
            }
        }
        return null;*/
    }

    /*
    public Set<AuthData> findAll() throws DataAccessException {
        return tokens;
    }
*/
    public boolean remove(AuthData token) throws DataAccessException {
        try (var conn = db.getConnection()) {
            var preparedStatement = conn.prepareStatement("DELETE FROM auth WHERE username=? AND authToken=?");
            preparedStatement.setString(1, token.getUsername());
            preparedStatement.setString(2, token.getAuthToken());
            if (preparedStatement.executeUpdate() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error: db is corrupt");
        }
    }

    public boolean clear() throws DataAccessException {
        try (var conn = db.getConnection()) {
            var preparedStatement = conn.prepareStatement("TRUNCATE auth");
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error: db is corrupt");
        }
    }


    public String generateNewToken() throws DataAccessException {
        String sb = "";
        boolean doUntilFalse = true;
        while (doUntilFalse) {
            sb = getRandomString();
            if (findUsernameFromToken(sb.toString()) == null) {
                doUntilFalse = false;
            }
        }
        return sb.toString();
    }

    private String getRandomString() {

        String lettersAndNumbers = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        while (sb.length() < 40) { // length of the random string.
            int index = (int) (rnd.nextFloat() * lettersAndNumbers.length());
            sb.append(lettersAndNumbers.charAt(index));
        }
        return sb.toString();
    }

    public boolean tokenValid(AuthData userJoining) throws DataAccessException {
        if (userJoining.getUsername() == null) {
            return false;
        }
        if (userJoining.getAuthToken() == null) {
            return false;
        }
        AuthData t = findToken(userJoining.getUsername());
        if (userJoining.getAuthToken().equals(t.getAuthToken())) {
            return true;
        }
        return false;
    }

    public String findUsernameFromToken(String token) throws DataAccessException {
        try (var conn = db.getConnection()) {
            var preparedStatement = conn.prepareStatement("SELECT username FROM auth WHERE authToken=?");
            preparedStatement.setString(1, token);
            var rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String output = rs.getString("username");
                return output;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error: db is corrupt");
        }


        /*for (AuthData a : tokens) {
            if (a.getAuthToken().equals(token)) {
                return a.getUsername();
            }
        }
        return null;*/
    }
}
