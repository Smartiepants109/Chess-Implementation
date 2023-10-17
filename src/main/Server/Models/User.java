package Server.Models;

public class User {
    String username;
    String pw;
    String email;
    int gameIDConnectedTo = 0;

    /**
     * @param username User's display name.
     * @param password User's security token
     * @param email    address of the user.
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.pw = password;
        this.email = email;
    }

    public int getGameIDConnectedTo() {
        return gameIDConnectedTo;
    }

    /**
     * @param newGameID gameID of game that is being connected to.
     * @return if operation is successful.
     * Must not be connected to a game already.
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
