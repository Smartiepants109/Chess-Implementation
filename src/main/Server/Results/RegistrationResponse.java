package Server.Results;

/**
 * Response container class containing a response code and a message.
 */
public class RegistrationResponse {
    /**
     * integer corresponding to the response code.
     */
    int responseCode;
    /**
     * message containing more details about result.
     */
    String message;
    String username;
    String authToken;

    /**
     * response code for a success.
     */
    static int SUCCESS = 200;
    /**
     * response code for a bad request.
     */
    static int BAD_REQUEST = 400;
    /**
     * response code for a username already being taken.
     */
    static int USERNAME_TAKEN = 403;
    /**
     * response code for other errors.
     */
    static int OTHER = 500;

    /**
     * Creates a new response for an attempted registration.
     *
     * @param code    integer corresponding to response code. See static variables for more details.
     * @param message message with more details of the response.
     */
    public RegistrationResponse(int code, String message) {
        responseCode = code;
        this.message = message;
        username = null;
        authToken = null;
    }

    public RegistrationResponse(int code, String username, String authToken) {
        responseCode = code;
        this.username = username;
        this.authToken = authToken;
        message = null;
    }

    public int getStatusCode() {
        return responseCode;
    }
}
