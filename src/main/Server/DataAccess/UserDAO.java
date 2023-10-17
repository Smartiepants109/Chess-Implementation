package Server.DataAccess;

import Server.Models.Game;
import Server.Models.User;
import dataAccess.DataAccessException;

import java.util.HashSet;
import java.util.Set;

public class UserDAO {
    Set<User> users = new HashSet<>();

    /**
     * @param user user to be added.
     * @return if the insertion was successful.
     * @throws if the database is unable to be accessed.
     */
    public boolean insertUser(User user) throws DataAccessException {
        return users.add(user);
    }

    /**
     * @param username username of account that is being found.
     * @return null if user wasn't found, otherwise returns User object.
     * @throws if the database is unable to be accessed.
     */
    public User findToken(String username) throws DataAccessException {//username? Email? Find out which.
        return null;
    }

}
