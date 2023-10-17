package Server.Requests;

import Server.Models.User;

public class JoinGameRequest {
    int gameIdToJoin;
    User userJoining;

    public int getGameIdToJoin() {
        return gameIdToJoin;
    }

    public User getUserJoining() {
        return userJoining;
    }

    /**
     * @param gameIdToJoin The database ID of the game that is being attempted to join.
     * @param userJoining The User class, recieved from UserDAO, of the person trying to join.
     */
    public JoinGameRequest(int gameIdToJoin, User userJoining){
        this.gameIdToJoin = gameIdToJoin;
        this.userJoining = userJoining;
    }
}
