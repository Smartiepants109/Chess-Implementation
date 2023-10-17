package Server.Results;

public class RegistrationResponse {
    int responseCode;
    String responseMessage;
    static int SUCCESS = 200;
    static int BAD_REQUEST = 400;
    static int USERNAME_TAKEN = 403;
    static int OTHER = 500;

    public RegistrationResponse(int code, String message) {
        responseCode = code;
        responseMessage = message;
    }
}
