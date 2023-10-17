package Server.Requests;

import Server.Models.User;

public class JoinGameRequest {
    /**
     * ID of game to attempt to join.
     */
    int gameIdToJoin;
    /**
     * User object correlating to user attempting to join a game.
     */
    User userJoining;

    public int getGameIdToJoin() {
        return gameIdToJoin;
    }

    public User getUserJoining() {
        return userJoining;
    }

    /**
     * creates a new Request to join a specific game.
     * @param gameIdToJoin The database ID of the game that is being attempted to join.
     * @param userJoining The User class, recieved from UserDAO, of the person trying to join.
     */
    public JoinGameRequest(int gameIdToJoin, User userJoining){
        this.gameIdToJoin = gameIdToJoin;
        this.userJoining = userJoining;
    }
}
