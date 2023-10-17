package Server.Results;

import Server.Requests.JoinGameRequest;

public class JoinGameResponse {
    int responseCode;
    String responseMessage;
    static int SUCCESS = 200;
    static int BAD_REQUEST = 400;
    static int UNAUTHORIZED = 401;
    static int SPOT_TAKEN = 403;
    static int OTHER = 500;

    public JoinGameResponse(int code, String message) {
        responseCode = code;
        responseMessage = message;
    }
}
