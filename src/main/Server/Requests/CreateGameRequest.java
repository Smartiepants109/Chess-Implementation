package Server.Requests;

import Server.Models.User;

/**
 * Container consisting of all items that are needed to attempt creating a game.
 */
public class CreateGameRequest {
    /**
     * ID of game to attempt to create.
     */
    String gameName;
    /**
     * User object correlating to user attempting to create a game.
     */
    User userCreating;

    public String getGameIdToJoin() {
        return gameName;
    }

    public User getUserJoining() {
        return userCreating;
    }

    /**
     * creates a new Request to create a specific game.
     *
     * @param gameName     The database ID of the game that is being attempted to create.
     * @param creatingUser The User class, recieved from UserDAO, of the person trying to create.
     */
    public CreateGameRequest(String gameName, User creatingUser) {
        this.gameName = gameName;
        this.userCreating = creatingUser;
    }
}
