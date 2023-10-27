package Server.Results;

public class ClearResponse {
    /**
     * General information about success or failure of the request. See constants for general use codes.
     */
    int responseCode;
    /**
     * Message that is alongside the code.
     */
    String message;
    /**
     * Success. Not much more to say.
     */
    static int SUCCESS = 200;
    /**
     * Code for other errors. Add a message for easier debugging.
     */
    static int OTHER = 500;

    /**
     * creates a new response for other classes.
     * @param code    response code. See static variables for options.
     * @param message More detailed message, if neccesary.
     */
    public ClearResponse(int code, String message) {
        responseCode = code;
        this.message = message;
    }
}
