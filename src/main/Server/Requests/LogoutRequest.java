package Server.Requests;

/**
 * A data container that is passed to the LoginService to be parsed into information.
 */
public class LogoutRequest {
    /**
     * the string that contains a user's username.
     */
    String username;
    /**
     * The string that contains a user's password.
     */
    String password;

    /**
     * creates a new login request.
     * @param username User class identifier that user is trying to log into.
     * @param password password of User.
     */
    public LogoutRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getToken() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
