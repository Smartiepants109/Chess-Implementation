package Results;

/**
 * Container for response information for specific attacks.
 */
public class LoginResponse {
    private final String username;
    private final String authToken;
    /**
     * int corresponding to one of the static response codes. See static variables for more info.
     */
    int responseCode;

    public int getResponseCode() {
        return responseCode;
    }

    /**
     * String containing more return information than the response code..
     */
    String message;


    /**
     * code for when a request is carried out successfully
     */
    static int SUCCESS = 200;
    /**
     * code for when a request isn't built properly or is in a different
     */
    static int BAD_REQUEST = 400;
    /**
     * incorrect credentials
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
    public LoginResponse(int code, String message) {
        responseCode = code;
        this.message = message;
        username = null;
        authToken = null;
    }

    public LoginResponse(int code, String username, String authToken) {
        this.responseCode = code;
        message = null;
        this.username = username;
        this.authToken = authToken;
    }
}
