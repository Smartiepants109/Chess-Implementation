package Server;

import Server.DataAccess.AuthDAO;
import Server.DataAccess.GameDAO;
import Server.DataAccess.UserDAO;
import Server.Models.AuthData;
import Server.Requests.*;
import Server.Results.*;
import Server.Services.*;
import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.Map;


public class Server {
    AuthDAO tokens = new AuthDAO();
    GameDAO games = new GameDAO();
    UserDAO users = new UserDAO();
    Gson gson = new Gson();

    public static void main(String[] args) {
        new Server().run();
    }

    private void run() {
        // Specify the port you want the server to listen on
        Spark.port(8080);

        // Register a directory for hosting static files
        Spark.externalStaticFileLocation("web");

        // Register handlers for each endpoint using the method reference syntax
        Spark.post("/session", this::loginHandler);
        Spark.post("/user", this::registerHandler);
        Spark.delete("/session", this::logoutHandler);
        Spark.get("/game", this::listGameHandler);
        Spark.post("/game", this::createGameHandler);
        Spark.put("/game", this::joinGameHandler);
        Spark.delete("db", this::clearDBHandler);

    }

    private Object joinGameHandler(Request request, Response response) throws DataAccessException {
        AuthData authData = new AuthData(request.params(":username"), request.params("password"));
        ChessGame.TeamColor color;
        if (request.params(":playerColor").equals("WHITE")) {
            color = ChessGame.TeamColor.WHITE;
        } else {
            if (request.params(":playerColor").equals("BLACK")) {
                color = ChessGame.TeamColor.BLACK;
            } else {
                throw new DataAccessException("should not attempt to connect to a game as NULL player.");
            }
        }
        JoinGameRequest joinGameRequest = new JoinGameRequest(Integer.parseInt(request.params(":gameID")), authData, color);
        JoinGameService ls = new JoinGameService(users, games, tokens);
        JoinGameResponse lr = ls.joinGame(joinGameRequest);
        return gson.toJson(lr);
    }

    private Object clearDBHandler(Request request, Response response) throws DataAccessException {
        ClearRequest clearRequest = new ClearRequest();
        ClearService ls = new ClearService(users, games, tokens);
        ClearResponse lr = ls.clear(clearRequest);
        return gson.toJson(lr);
    }

    private Object createGameHandler(Request request, Response response) throws DataAccessException {
        CreateGameRequest createGameRequest = new CreateGameRequest(request.params(":gameName"), request.params(":username"), request.params(":password"));
        CreateGameService ls = new CreateGameService(users, games, tokens);
        CreateGameResponse lr = ls.createGame(createGameRequest);
        return gson.toJson(lr);
    }

    private Object listGameHandler(Request request, Response response) throws DataAccessException {
        AuthData token = gson.fromJson(getAuthToken(request), AuthData.class);
        GameListRequest gameListRequest = new GameListRequest(token.getUsername(), token.getAuthToken());
        GameListService ls = new GameListService(users, games, tokens);
        GameListResponse lr = ls.listGames(gameListRequest);
        return gson.toJson(lr);
    }

    private Object loginHandler(Request req, Response res) throws DataAccessException {
        String body = req.body();
        LoginRequest loginRequest = new LoginRequest(getParameter(body, "username"), getParameter(body, "password"));
        LoginService ls = new LoginService(users, tokens);
        LoginResponse lr = ls.login(loginRequest);
        return gson.toJson(lr);
    }

    private static String getAuthToken(Request req) {
        return req.headers("authorization");
    }

    private Object logoutHandler(Request req, Response res) throws DataAccessException {
        AuthData token = gson.fromJson(getAuthToken(req), AuthData.class);
        LogoutRequest logoutRequest = new LogoutRequest(token.getUsername(), token.getAuthToken());
        LogoutService ls = new LogoutService(users, games, tokens);
        LogoutResponse lr = ls.logout(logoutRequest);
        return gson.toJson(lr);
    }

    private Object registerHandler(Request req, Response res) throws DataAccessException {
        String body = req.body();
        String username = getParameter(body, "username");
        String pw = getParameter(body, "password");
        String email = getParameter(body, "email");
        RegistrationRequest registrationRequest = new RegistrationRequest(username,
                pw, email);
        RegistrationService rs = new RegistrationService(users, tokens);
        RegistrationResponse rr = rs.register(registrationRequest);
        return gson.toJson(rr);
    }

    private String getParameter(String body, String parameter) {
        Map<String, String> pain = gson.fromJson(body, Map.class);
        return pain.get(parameter);
    }


}