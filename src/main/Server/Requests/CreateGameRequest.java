package Server.Requests;

/**
 * Container consisting of all items that are needed to attempt creating a game.
 */
public class CreateGameRequest {
    /**
     * ID of game to attempt to create.
     */
    String gameName;

    public String getGameName() {
        return gameName;
    }

    /**
     * User object correlating to user attempting to create a game.
     */
    String userCreating;
    String pw;

    public String getPw() {
        return pw;
    }

    public String getUserJoining() {
        return userCreating;
    }

    /**
     * creates a new Request to create a specific game.
     *
     * @param gameName The database name of the game that is being attempted to create.
     */
    public CreateGameRequest(String gameName, String username, String password) {
        this.gameName = gameName;
        this.userCreating = username;
        this.pw = password;
    }
}
