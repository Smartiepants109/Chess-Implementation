package Results;

/**
 * Container consisting of a response code and a more detailed message.
 */
public class CreateGameResponse {
    /**
     * General information about success or failure of the request. See constants for general use codes.
     */
    int responseCode;
    Integer gameID;
    /**
     * Message that is alongside the code.
     */
    String message;
    /**
     * Success. Not much more to say.
     */
    static int SUCCESS = 200;
    /**
     * Request object is somehow broken or otherwise invalid.
     */
    static int BAD_REQUEST = 400;
    /**
     * Login information not accepted or otherwise invalid.
     */
    static int UNAUTHORIZED = 401;

    /**
     * Code for other errors. Add a message for easier debugging.
     */
    static int OTHER = 500;

    /**
     * creates a new response for other classes.
     *
     * @param code    response code. See static variables for options.
     * @param message More detailed message, if neccesary.
     */
    public CreateGameResponse(int code, String message) {
        responseCode = code;
        this.message = message;
        this.gameID = null;
    }

    public CreateGameResponse(int code, int gameID) {
        responseCode = code;
        this.gameID = gameID;
        message = null;
    }

    public int getStatus() {
        return responseCode;
    }
}
