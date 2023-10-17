package Server.Requests;

public class RegistrationRequest {
    String username;
    String password;
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
