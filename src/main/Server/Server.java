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
import dataAccess.Database;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.sql.SQLException;
import java.util.Map;


public class Server {
    Database db = new Database();
    AuthDAO tokens = new AuthDAO(db);
    GameDAO games = new GameDAO(db);
    UserDAO users = new UserDAO(db);
    Gson gson = new Gson();

    public static void main(String[] args) {
        new Server().run();
    }

    private void run() {
        // Specify the port you want the server to listen on
        Spark.port(8080);
        //check if database exists. If not, create it.
        configureDB();


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

    private void configureDB() {
        try (var conn = db.getConnection()) {
            var createAuthTable = """
                    CREATE TABLE IF NOT EXISTS auth (
                    username VARCHAR(255) NOT NULL,
                    authToken VARCHAR(255) NOT NULL,
                    PRIMARY KEY (authToken), 
                    INDEX (username)
                    )""";
            var createGameTable = """
                    CREATE TABLE IF NOT EXISTS games (
                    gameID INT NOT NULL,
                    gameName VARCHAR(255),
                    whiteUsername VARCHAR(255),
                    blackUsername VARCHAR(255),
                    game TEXT,
                    PRIMARY KEY (gameID),
                    INDEX (gameName)
                    )""";
            var createUserTable = """
                    CREATE TABLE IF NOT EXISTS users (
                    username VARCHAR(255) NOT NULL,
                    pw VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    PRIMARY KEY (username), 
                    INDEX (email)
                    )""";
            try (var authStatement = conn.prepareStatement(createAuthTable)) {
                authStatement.executeUpdate();
            }
            try (var userStatement = conn.prepareStatement(createUserTable)) {
                userStatement.executeUpdate();
            }
            try (var gameStatement = conn.prepareStatement(createGameTable)) {
                gameStatement.executeUpdate();
            }
            db.returnConnection(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object joinGameHandler(Request request, Response response) throws DataAccessException {
        AuthData authData = getAuthData(request);

        ChessGame.TeamColor color = null;
        String body = request.body();
        String colorSTR = getParameter(body, "playerColor");
        if (colorSTR == null) {
            color = null;
        } else {
            if (colorSTR.equals("WHITE")) {
                color = ChessGame.TeamColor.WHITE;
            } else {
                color = ChessGame.TeamColor.BLACK;
            }
        }
        JoinGameRequest joinGameRequest = new JoinGameRequest(getIntParameter(body, "gameID"), authData, color);
        JoinGameService ls = new JoinGameService(users, games, tokens);
        JoinGameResponse lr = ls.joinGame(joinGameRequest);
        response.status(lr.getstatcode());
        return gson.toJson(lr);
    }

    private AuthData getAuthData(Request request) {
        String token = getAuthToken(request);
        String user = tokens.findUsernameFromToken(token);
        return new AuthData(user, token);
    }

    private Object clearDBHandler(Request request, Response response) throws DataAccessException {
        ClearRequest clearRequest = new ClearRequest();
        ClearService ls = new ClearService(users, games, tokens);
        ClearResponse lr = ls.clear(clearRequest);
        return gson.toJson(lr);
    }

    private Object createGameHandler(Request request, Response response) throws DataAccessException {
        String body = request.body();
        String authToken = getAuthToken(request);
        String username = tokens.findUsernameFromToken(authToken);
        CreateGameRequest createGameRequest = new CreateGameRequest((getParameter(body, "gameName")),
                username, authToken);
        CreateGameService ls = new CreateGameService(users, games, tokens);
        CreateGameResponse lr = ls.createGame(createGameRequest);
        response.status(lr.getStatus());
        return gson.toJson(lr);
    }

    private Object listGameHandler(Request request, Response response) throws DataAccessException {

        AuthData token = getAuthData(request);
        GameListRequest gameListRequest = new GameListRequest(token.getUsername(), token.getAuthToken());
        GameListService ls = new GameListService(users, games, tokens);
        GameListResponse lr = ls.listGames(gameListRequest);
        response.status(lr.getCode());
        return gson.toJson(lr);
    }

    private Object loginHandler(Request req, Response res) throws DataAccessException {
        String body = req.body();
        LoginRequest loginRequest = new LoginRequest(getParameter(body, "username"), getParameter(body, "password"));
        LoginService ls = new LoginService(users, tokens);
        LoginResponse lr = ls.login(loginRequest);
        res.status(lr.getResponseCode());
        return gson.toJson(lr);
    }

    private static String getAuthToken(Request req) {
        return req.headers("authorization");
    }

    private Object logoutHandler(Request req, Response res) throws DataAccessException {
        String token = (getAuthToken(req));
        LogoutRequest logoutRequest = new LogoutRequest(tokens.findUsernameFromToken(token), token);
        LogoutService ls = new LogoutService(users, games, tokens);
        LogoutResponse lr = ls.logout(logoutRequest);
        res.status(lr.getStatusCode());
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
        res.status(rr.getStatusCode());
        return gson.toJson(rr);
    }

    private String getParameter(String body, String parameter) {
        Map<String, String> pain = gson.fromJson(body, Map.class);
        if (pain.containsKey(parameter)) {
            return pain.get(parameter);
        }
        return null;
    }

    private int getIntParameter(String body, String parameter) {
        Map<String, Double> pain = gson.fromJson(body, Map.class);
        Double out = pain.get(parameter);
        return out.intValue();
    }


}