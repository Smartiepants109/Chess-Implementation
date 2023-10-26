package Server.DataAccess;

import Server.Models.User;
import dataAccess.DataAccessException;

import java.util.HashSet;
import java.util.Set;

/**
 * Allows a user to interact with the database of users.
 */
public class UserDAO {
    /**
     * placeholder for database.
     */
    Set<User> users;

    /**
     * creates the database access object.
     */
    public UserDAO() {
        users = new HashSet<>();
    }

    /**
     * attempts to insert a user to the database.
     *
     * @param user user to be added.
     * @return if the insertion was successful.
     * @throws DataAccessException if the database is unable to be accessed.
     */
    public boolean insertUser(User user) throws DataAccessException {
        return users.add(user);
    }

    /**
     * attempts to find the user with the username provided.
     *
     * @param username username of account that is being found.
     * @return null if user wasn't found, otherwise returns User object.
     * @throws DataAccessException when the database is unable to be accessed.
     */
    public User findToken(String username) throws DataAccessException {//username? Email? Find out which.
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    public void clear() {
        users = new HashSet<>();
    }
}
