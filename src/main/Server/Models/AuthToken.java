package Server.Models;


public class AuthToken {
    String username;
    String authToken;

    /**
     * @param uName Username of owner of token.
     * @param Token Security token.
     */
    public AuthToken(String uName, String Token) {
    }

    protected String getAuthToken() {
        return authToken;
    }

    protected String getUsername() {
        return username;
    }
}
