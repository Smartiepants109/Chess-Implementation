package Server.Services;

import Server.DataAccess.UserDAO;
import Server.Requests.RegistrationRequest;
import Server.Results.RegistrationResponse;
import dataAccess.DataAccessException;

/**
 * Service in charge of allowing users to register for a new account.
 */
public class RegistrationService {
    /**
     * user data access object so that service can register a user to the database
     */
    UserDAO users;

    /**
     * creates a new service.
     * @param userDAO database access object for user information.
     * @throws DataAccessException if database is unable to be connected to.
     */
    public RegistrationService(UserDAO userDAO) throws DataAccessException {
        users = userDAO;
    }

    /**
     * Attempts to register a new account.
     * @param r request object containing information for login attempt.
     * @return response information about your registration attempt.
     * @throws DataAccessException if database is unable to be connected to.
     */
    RegistrationResponse register(RegistrationRequest r) throws DataAccessException {
        return null;
    }
}
