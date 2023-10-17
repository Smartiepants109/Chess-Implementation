package Server.Requests;

/**
 * Container with username, password, and email. Used to create a new account.
 */
public class RegistrationRequest {
    /**
     * String containing username for algorithm to check for duplicity later.
     */
    String username;
    /**
     * password of user to add to database.
     */
    String password;
    /**
     * email address of the user. TODO verify that it is valid email address.
     */
    String email;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    /**
     * creates a new registration request
     * @param username username of new User
     * @param password security key of new User
     * @param email    email address of new User
     */
    public RegistrationRequest(String username, String password, String email) {
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
