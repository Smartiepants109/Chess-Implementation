package Server.Results;

public class LoginResponse {
    int responseCode;
    String responseMessage;
    static int SUCCESS = 200;
    static int BAD_REQUEST = 400;
    static int UNAUTHORIZED = 401;
    static int OTHER = 500;

    public LoginResponse(int code, String message) {
        responseCode = code;
        responseMessage = message;
    }
}
