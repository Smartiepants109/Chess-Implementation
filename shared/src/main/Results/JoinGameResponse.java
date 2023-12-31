package Results;

import chess.ChessGame;

/**
 * Container consisting of a response code and a more detailed message.
 */
public class JoinGameResponse {
    ChessGame game;
    /**
     * General information about success or failure of the request. See constants for general use codes.
     */
    int responseCode;
    /**
     * Message that is alongside the code.
     */
    String message;
    /**
     * Success. Not much more to say.
     */
    static int SUCCESS = 200;
    /**
     * Request object is somehow broken or otherwise invalid.
     */
    static int BAD_REQUEST = 400;
    /**
     * Login information not accepted or otherwise invalid.
     */
    static int UNAUTHORIZED = 401;
    /**
     * Team in game has already been taken. If no one is there, fix the disconnect funct.
     */
    static int SPOT_TAKEN = 403;
    /**
     * Code for other errors. Add a message for easier debugging.
     */
    static int OTHER = 500;

    /**
     * creates a new response for other classes.
     *
     * @param code    response code. See static variables for options.
     * @param message More detailed message, if neccesary.
     */
    public JoinGameResponse(int code, String message) {
        responseCode = code;
        this.message = message;
    }

    public JoinGameResponse(int code, ChessGame game) {
        responseCode = code;
        this.game = game;
    }

    public int getstatcode() {
        return responseCode;
    }
}
