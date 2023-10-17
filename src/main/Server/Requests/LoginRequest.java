package Server.Requests;

public class LoginRequest {
    String username;
    String password;

    /**
     * @param username User class identifier that user is trying to log into.
     * @param password password of User.
     */
    public LoginRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
