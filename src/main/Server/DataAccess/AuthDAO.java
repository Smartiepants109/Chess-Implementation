package Server.DataAccess;

import Server.Models.AuthData;
import dataAccess.DataAccessException;

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
    Set<AuthData> tokens;

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
    public boolean insertToken(AuthData token) throws DataAccessException {
        return tokens.add(token);
    }

    /**
     * searches for a username - password combo using the username.
     *
     * @param username the username of the authToken that is to be found.
     * @return the AuthToken object that is found, null if not found.
     * @throws DataAccessException if the database itself is unable to be accessed.
     */
    public AuthData findToken(String username) throws DataAccessException {
        for (AuthData a : tokens) {
            if (a.getUsername().equals(username)) {
                return a;
            }
        }
        return null;
    }

    public Set<AuthData> findAll() throws DataAccessException {
        return tokens;
    }

    public boolean remove(AuthData token) throws DataAccessException {
        return tokens.remove(token);
    }

    public boolean clear() throws DataAccessException {
        tokens = new HashSet<>();
        return true;
    }


    public String generateNewToken() {
        String sb = "";
        boolean doUntilFalse = true;
        while (doUntilFalse) {
            doUntilFalse = false;
            sb = getRandomString();

            for (AuthData t : tokens) {
                if (t.getAuthToken().equals(sb)) {
                    doUntilFalse = true;
                }
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
        for (AuthData a : tokens) {
            if (a.getAuthToken().equals(token)) {
                return a.getUsername();
            }
        }
        return null;
    }
}
