package Server.Services;

import Server.DataAccess.UserDAO;
import Server.Requests.LoginRequest;
import Server.Results.LoginResponse;
import dataAccess.DataAccessException;

public class LoginService {

    /**
     * Database access object for User database.
     */
    UserDAO users;

    /**
     * @param userDAO database access object for user information.
     * @throws DataAccessException if database is unable to be accessed.
     */
    public LoginService(UserDAO userDAO) throws DataAccessException {
        users = userDAO;
    }

    /**
     * @param r request object containing information for login attempt.
     * @return whether or not you logged in correctly.
     * @throws DataAccessException if database's connection was unable to be maintained.
     */
    public LoginResponse login(LoginRequest r) throws DataAccessException{
        return null;
    }
}
