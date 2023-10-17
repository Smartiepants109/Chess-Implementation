package Server.Models;

/**
 * User class. Contains a username, password, and an email address.
 */
public class User {
    /**
     * string containing user's display name.
     */
    String username;
    /**
     * string containing user's password.
     */
    String pw;
    /**
     * string containing user's email address.
     */
    String email;
    /**
     * ID of the game that the user is connected to.
     */
    int gameIDConnectedTo = 0;

    /**
     * creates a new User profile
     * @param username User's display name.
     * @param password User's security token
     * @param email    address of the user.
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.pw = password;
        this.email = email;
        gameIDConnectedTo = 0;
    }

    public int getGameIDConnectedTo() {
        return gameIDConnectedTo;
    }

    /**
     * Alters which game you are reported to be connected to. Potential use in future.
     * @param newGameID gameID of game that is being connected to.
     * @return if operation is successful.
     * Must not be connected to a game already. Sets the game it is connected to to the new ID
     */
    public boolean setConnectedGame(int newGameID) {
        if (this.gameIDConnectedTo == 0) {
            this.gameIDConnectedTo = newGameID;
            return true;
        }
        return false;
    }

    /**
     * Removes gameID of previously connected game. Must be connected to a game to perform operation.
     * @return if operation is successful
     */
    public boolean disconnect() {
        if (this.gameIDConnectedTo != 0) {
            this.gameIDConnectedTo = 0;
            return true;
        }
        return false;
    }

    protected String getPassword() {
        return pw;
    }

    /**
     * Attempts to change the password. Not needed at the moment, but perhaps in the future, depending on how
     * I code this in the future.
     * @param previousPassword password before change
     * @param newPassword      password after change
     * @return true if operation successful, false if not.
     * If password has not been set (I.E. Null), then anyone can set it to any value.
     */
    public boolean changePassword(String previousPassword, String newPassword) {
        if (pw.equals(previousPassword) || pw == null) {
            pw = newPassword;
            return true;
        }
        return false;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
