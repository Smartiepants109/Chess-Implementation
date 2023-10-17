package Server.DataAccess;

import Server.Models.Game;
import Server.Models.User;

import java.util.HashSet;
import java.util.Set;

public class UserDAO {
    Set<User> users = new HashSet<>();

    /**
     * @param user user to be added.
     * @return if the insertion was successful.
     */
    public boolean insertUser(User user) {
        return users.add(user);
    }

    /**
     * @param username username of account that is being found.
     * @return null if user wasn't found, otherwise returns User object.
     */
    public User findToken(String username) {//username? Email? Find out which.
        return null;
    }

}
