package Server.Services;

import Server.DataAccess.AuthDAO;
import Server.DataAccess.UserDAO;
import Server.Models.AuthToken;
import Server.Models.User;
import Server.Requests.LoginRequest;
import Server.Results.LoginResponse;
import Server.Results.RegistrationResponse;
import dataAccess.DataAccessException;

/**
 * Service in charge of logging you into your account.
 */
public class LoginService {

    AuthDAO tokens;
    /**
     * Database access object for User database.
     */
    UserDAO users;

    /**
     * Creates the login service.
     *
     * @param userDAO database access object for user information.
     * @throws DataAccessException if database is unable to be accessed.
     */
    public LoginService(UserDAO userDAO, AuthDAO tokens) throws DataAccessException {
        users = userDAO;
        this.tokens = tokens;
    }

    /**
     * Attempts to login to a user account based on the request.
     *
     * @param r request object containing information for login attempt.
     * @return whether or not you logged in correctly.
     * @throws DataAccessException if database's connection was unable to be maintained.
     */
    public LoginResponse login(LoginRequest r) throws DataAccessException {
        User u = users.findToken(r.getUsername());
        String username, password;
        username = r.getUsername();
        password = r.getPassword();
        if (u == null) {
            return new LoginResponse(500, "Error: User does not exist. Try registering them first.");
        }
        if (u.getPw().equals(r.getPassword())) {
            AuthToken authToken = new AuthToken(username, tokens.generateNewToken());
            tokens.insertToken(authToken);
            return new LoginResponse(200, "{\"username\":\"" + username + "\", \"authToken\":\"" + authToken.toString() + "\"}");
        } else {
            return new LoginResponse(401, "Error: unauthorized");
        }
    }
}
