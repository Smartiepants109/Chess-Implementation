package Server.Models;


/**
 * Data container with a username and a password.
 */
public class AuthToken {
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
     * @param uName Username of owner of token.
     * @param Token Security token.
     */
    public AuthToken(String uName, String Token) {
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }
}
