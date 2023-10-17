package Server.Services;

import Server.DataAccess.UserDAO;
import Server.Requests.RegistrationRequest;
import Server.Results.RegistrationResponse;
import dataAccess.DataAccessException;

public class RegistrationService {
    UserDAO users;

    /**
     * @param userDAO database access object for user information.
     * @throws DataAccessException if database is unable to be connected to.
     */
    public RegistrationService(UserDAO userDAO) throws DataAccessException {
        users = userDAO;
    }

    /**
     * @param r request object containing information for login attempt.
     * @return response information about your registration attempt.
     * @throws DataAccessException if database is unable to be connected to.
     */
    RegistrationResponse register(RegistrationRequest r) throws DataAccessException {
        return null;
    }
}
