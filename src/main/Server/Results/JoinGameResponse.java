package Server.Results;

public class JoinGameResponse {
    /**
     * General information about success or failure of the request. See constants for general use codes.
     */
    int responseCode;
    /**
     * Message that is alongside the code.
     */
    String responseMessage;
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
     * Team in game has already been taken. If no one is there, fix the disconnect funct.
     */
    static int SPOT_TAKEN = 403;
    /**
     * Code for other errors. Add a message for easier debugging.
     */
    static int OTHER = 500;

    /**
     * @param code    response code. See static variables for options.
     * @param message More detailed message, if neccesary.
     */
    public JoinGameResponse(int code, String message) {
        responseCode = code;
        responseMessage = message;
    }
}
