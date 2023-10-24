package Server.DataAccess;

import Server.Models.AuthToken;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.ClassOrderer;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * DAO  for authentication tokens.
 */
public class AuthDAO {
    /**
     * current database. Will be updated later.
     */
    Set<AuthToken> tokens;

    /**
     * generates a new Data Access Object.
     */
    public AuthDAO() {
        tokens = new HashSet<>();
    }

    /**
     * adds a token to the database.
     *
     * @param token a user's authToken to add to the database?
     * @return whether the insertion was successful.
     * @throws DataAccessException if the database was unable to be accessed.
     */
    public boolean insertToken(AuthToken token) throws DataAccessException {
        return tokens.add(token);
    }

    /**
     * searches for a username - password combo using the username.
     *
     * @param username the username of the authToken that is to be found.
     * @return the AuthToken object that is found, null if not found.
     * @throws DataAccessException if the database itself is unable to be accessed.
     */
    protected AuthToken findToken(String username) throws DataAccessException {
        return null;
    }

    protected Set<AuthToken> findAll() throws DataAccessException {
        return tokens;
    }

    protected boolean remove(AuthToken token) throws DataAccessException {
        return false;
    }

    public boolean clear() throws DataAccessException {
        return false;
    }


    public String generateNewToken() {
        StringBuilder sb = new StringBuilder();
        boolean doUntilFalse = true;
        while (doUntilFalse) {
            doUntilFalse = false;
            for (int i = 0; i < 40; i++) {
                Random r = new Random();
                sb.append((char) r.nextInt(255));
            }
            for (AuthToken t : tokens) {
                if (t.getAuthToken().equals(sb.toString())) {
                    doUntilFalse = true;
                }
            }

        }
        return sb.toString();
    }
}
