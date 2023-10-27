package Server.Services;

import Server.DataAccess.AuthDAO;
import Server.DataAccess.UserDAO;
import Server.Models.AuthData;
import Server.Models.User;
import Server.Requests.RegistrationRequest;
import Server.Results.RegistrationResponse;
import com.google.gson.Gson;
import dataAccess.DataAccessException;

/**
 * Service in charge of allowing users to register for a new account.
 */
public class RegistrationService {
    /**
     * user data access object so that service can register a user to the database
     */
    UserDAO users;
    AuthDAO tokens;

    /**
     * creates a new service.
     *
     * @param userDAO database access object for user information.
     * @throws DataAccessException if database is unable to be connected to.
     */
    public RegistrationService(UserDAO userDAO, AuthDAO tokens) throws DataAccessException {
        users = userDAO;
        this.tokens = tokens;
    }

    /**
     * Attempts to register a new account.
     *
     * @param r request object containing information for login attempt.
     * @return response information about your registration attempt.
     * @throws DataAccessException if database is unable to be connected to.
     */
    public RegistrationResponse register(RegistrationRequest r) throws DataAccessException {
        String username, password, email;
        username = r.getUsername();
        password = r.getPassword();
        email = r.getEmail();
        if (username != null && password != null && email != null) {
            if (users.findToken(username) == null) {
                if (users.insertUser(new User(username, password, email))) {
                    AuthData authData = new AuthData(username, tokens.generateNewToken());
                    tokens.insertToken(authData);
                    return new RegistrationResponse(200, new Gson().toJson(authData));
                } else {
                    throw new DataAccessException("Somehow, Palpati- I mean, this failed.");
                }

            } else {
                return new RegistrationResponse(403, "Error: already taken");
            }
        } else {
            return new RegistrationResponse(400, "Error: bad request");
        }
    }
}
