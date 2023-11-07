package Server.DataAccess;

import Server.Models.AuthData;
import dataAccess.DataAccessException;
import dataAccess.Database;

import java.sql.SQLException;
import java.util.Random;
import java.util.Set;

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
            db.returnConnection(conn);
            return !(rs == 0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
                db.returnConnection(conn);
                return new AuthData(username, token);
            } else {
                db.returnConnection(conn);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
                db.returnConnection(conn);
                return true;
            } else {
                db.returnConnection(conn);
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean clear() throws DataAccessException {
        try (var conn = db.getConnection()) {
            var preparedStatement = conn.prepareStatement("TRUNCATE auth");
            preparedStatement.executeUpdate();
            db.returnConnection(conn);
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public String generateNewToken() {
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

    public String findUsernameFromToken(String token) {
        try (var conn = db.getConnection()) {
            var preparedStatement = conn.prepareStatement("SELECT username FROM auth WHERE authToken=?");
            preparedStatement.setString(1, token);
            var rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String output = rs.getString("username");
                db.returnConnection(conn);
                return output;
            } else {
                db.returnConnection(conn);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }


        /*for (AuthData a : tokens) {
            if (a.getAuthToken().equals(token)) {
                return a.getUsername();
            }
        }
        return null;*/
    }
}
