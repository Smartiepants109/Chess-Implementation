package Models;


/**
 * Data container with a username and a password.
 */
public class AuthData {
    /**
     * String field containing username of user.
     */
    String username;
    /**
     * String field containing user's password.
     */
    String authToken;

    /**
     * creates a new authentication token.
     *
     * @param uName Username of owner of token.
     * @param Token Security token.
     */
    public AuthData(String uName, String Token) {
        this.authToken = Token;
        this.username = uName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }
}
