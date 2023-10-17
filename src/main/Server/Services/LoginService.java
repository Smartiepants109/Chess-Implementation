package Server.Services;

import Server.DataAccess.UserDAO;
import Server.Requests.LoginRequest;
import Server.Results.LoginResponse;
import dataAccess.DataAccessException;

/**
 * Service in charge of logging you into your account.
 */
public class LoginService {

    /**
     * Database access object for User database.
     */
    UserDAO users;

    /**
     * Creates the login service.
     * @param userDAO database access object for user information.
     * @throws DataAccessException if database is unable to be accessed.
     */
    public LoginService(UserDAO userDAO) throws DataAccessException {
        users = userDAO;
    }

    /**
     * Attempts to login to a user account based on the request.
     * @param r request object containing information for login attempt.
     * @return whether or not you logged in correctly.
     * @throws DataAccessException if database's connection was unable to be maintained.
     */
    public LoginResponse login(LoginRequest r) throws DataAccessException{
        return null;
    }
}
