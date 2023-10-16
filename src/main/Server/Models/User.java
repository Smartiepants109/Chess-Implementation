package Server.Models;

public class User {
    String username;
    String pw;
    String email;

    /**
     * @param username User's display name.
     * @param password User's security token
     * @param email address of the user.
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.pw = password;
        this.email = email;
    }
}
