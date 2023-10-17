package Server.DataAccess;

import Server.Models.AuthToken;
import dataAccess.DataAccessException;

import java.util.HashSet;
import java.util.Set;

public class AuthDAO {
    Set<AuthToken> tokens = new HashSet<>();

    /**
     * @param token a user's authToken to add to the database?
     * @return whether the insertion was successful.
     * @throws DataAccessException if the database was unable to be accessed.
     */
    public boolean insertToken(AuthToken token) throws DataAccessException {
        return tokens.add(token);
    }

    /**
     * @param username the username of the authToken that is to be found.
     * @return the AuthToken object that is found, null if not found.
     * @throws DataAccessException if the database itself is unable to be accessed.
     */
    private AuthToken findToken(String username) throws DataAccessException {
        return null;
    }
}
