package Results;

/**
 * Container for response information for specific attacks.
 */
public class LogoutResponse {
    /**
     * int corresponding to one of the static response codes. See static variables for more info.
     */
    int responseCode;
    /**
     * String containing more return information than the response code..
     */
    String message;
    /**
     * code for when a request is carried out successfully
     */
    static int SUCCESS = 200;
    /**
     * code for when you try to access a user when you aren't logged in.
     */
    static int UNAUTHORIZED = 401;
    /**
     * code for other errors.
     */
    static int OTHER = 500;

    /**
     * creates a response using the provided code and message
     *
     * @param code    the integer code that corresponds with the success or error state.
     * @param message a more detailed textual version of the code provided previously.
     */
    public LogoutResponse(int code, String message) {
        responseCode = code;
        this.message = message;
    }

    public int getStatusCode() {
        return responseCode;
    }
}
