package Server;

import Models.AuthData;
import Models.Game;
import Requests.*;
import Results.*;
import Server.DataAccess.AuthDAO;
import Server.DataAccess.GameDAO;
import Server.DataAccess.UserDAO;
import Server.Services.*;
import chess.ChessGame;
import chessGame.BoardAdapter;
import chessGame.ChessGameImp;
import chessGame.ChessPositionImp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataAccess.DataAccessException;
import dataAccess.Database;
import messages.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.Request;
import spark.Response;
import spark.Spark;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;

class conInfo {
    Session sess;
    String authToken;
    int gameID;
}

@WebSocket
public class Server {
    Database db = new Database();
    AuthDAO tokens = new AuthDAO(db);
    GameDAO games = new GameDAO(db);
    UserDAO users = new UserDAO(db);
    Gson gson = new Gson();
    Vector<conInfo> connections = new Vector<conInfo>();

    public static void main(String[] args) {
        new Server().run();
    }

    private void run() {
        // Specify the port you want the server to listen on
        Spark.port(8080);
        //check if database exists. If not, create it.
        try {
            configureDB();
        } catch (DataAccessException e) {
            System.out.println("this shouldn't be happening. fail on startup.");
            throw new RuntimeException();
        }

        Spark.webSocket("/connect", Server.class);

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
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(UserGameCommand.class, new UserMessageAdapter());
        UserGameCommand sm = gb.create().fromJson(message, UserGameCommand.class);
        switch (sm.getCommandType()) {
            case JOIN_PLAYER -> {
                JoinPlayer jp = (JoinPlayer) sm;
                if (tokens.findUsernameFromToken(jp.getAuthString()) != null) {
                    addSession(session, jp.getAuthString(), jp.gameID);
                    //nothing, they already joined. anything here to upgrade connection?
                    notificationMessage nm = new notificationMessage("Player has joined the server on team " + jp.playerColor.toString());
                    broadcast(gb.create().toJson(nm), jp.gameID, jp.getAuthString());
                    Game g = games.findGame(jp.gameID);
                    if (g == null) {
                        invalidGameIDMsgSend(session, gb);
                    }
                    if (jp.playerColor == ChessGame.TeamColor.WHITE && g.getWhiteUsername() == null) {
                        DatabaseNotUpdatedProperlyErrorSend(session, gb);
                    } else if (jp.playerColor == ChessGame.TeamColor.BLACK && g.getBlackUsername() == null) {
                        DatabaseNotUpdatedProperlyErrorSend(session, gb);
                    } else if (jp.playerColor == ChessGame.TeamColor.WHITE && !g.getWhiteUsername().equals(tokens.findUsernameFromToken(jp.getAuthString()))) {
                        PlayerSlotTakenMessageSend(session, gb);
                    } else if (jp.playerColor == ChessGame.TeamColor.BLACK && !g.getBlackUsername().equals(tokens.findUsernameFromToken(jp.getAuthString()))) {
                        PlayerSlotTakenMessageSend(session, gb);
                    } else {
                        gb.registerTypeAdapter(ChessGameImp.class, new BoardAdapter());
                        loadGameMessage lg = new loadGameMessage(gb.create().toJson(g.getGame(), ChessGameImp.class));
                        session.getRemote().sendString(gb.create().toJson(lg));
                    }
                } else {
                    invalidAuthTokenMessageSend(session, gb);
                }
            }
            case JOIN_OBSERVER -> {
                joinObserver jp = (joinObserver) sm;
                addSession(session, jp.getAuthString(), jp.gameID);
                //nothing, they already joined. anything here to upgrade connection?
                if (tokens.findUsernameFromToken(jp.getAuthString()) != null) {
                    addSession(session, jp.getAuthString(), jp.gameID);
                    //nothing, they already joined. anything here to upgrade connection?
                    notificationMessage nm = new notificationMessage("Observer has joined the server");
                    broadcast(gb.create().toJson(nm), jp.gameID, jp.getAuthString());
                    Game g = games.findGame(jp.gameID);
                    if (g == null) {
                        invalidGameIDMsgSend(session, gb);
                    } else {
                        gb.registerTypeAdapter(ChessGameImp.class, new BoardAdapter());
                        loadGameMessage lg = new loadGameMessage(gb.create().toJson(g.getGame(), ChessGameImp.class));
                        session.getRemote().sendString(gb.create().toJson(lg));
                    }
                } else {
                    invalidAuthTokenMessageSend(session, gb);
                }
            }
            case MAKE_MOVE -> {
                makeMove mm = (makeMove) sm;
                if (tokens.findUsernameFromToken(mm.getAuthString()) != null) {
                    Game g = games.findGame(mm.gameID);
                    ChessPositionImp startpos = (ChessPositionImp) mm.move.getStartPosition();
                    if (g.getGame().validMoves(startpos).contains(mm.move)) {
                        g.getGame().makeMove(mm.move);
                        gb.registerTypeAdapter(ChessGameImp.class, new BoardAdapter());
                        games.updateData("game", mm.gameID, gb.create().toJson(g.getGame(), ChessGameImp.class));
                        loadGameMessage lg = new loadGameMessage(gb.create().toJson(g.getGame(), ChessGameImp.class));
                        broadcast(gb.create().toJson(lg), mm.gameID, null);
                        broadcast(gb.create().toJson(new notificationMessage("Player has made move" + mm.move.toString())), mm.gameID, mm.getAuthString());
                    } else {
                        errorMessage em = new errorMessage("invalid move");
                        gb.registerTypeAdapter(ServerMessage.class, new ServerMessageAdapter());
                        session.getRemote().sendString(gb.create().toJson(em));
                    }


                } else {
                    invalidAuthTokenMessageSend(session, gb);
                }
            }
            case LEAVE -> {
                leave l = (leave) sm;
                if (tokens.findUsernameFromToken(l.getAuthString()) != null) {
                    //TODO: Notify all that need to be notified.
                } else {
                    invalidAuthTokenMessageSend(session, gb);
                }
            }
            case RESIGN -> {
                resign r = (resign) sm;
                if (tokens.findUsernameFromToken(r.getAuthString()) != null) {
                    //TODO: Notify all that need to be notified.
                } else {
                    invalidAuthTokenMessageSend(session, gb);
                }
            }
        }
        System.out.printf("Received: %s", message);
        session.getRemote().sendString("WebSocket response: " + message);
    }

    private void DatabaseNotUpdatedProperlyErrorSend(Session session, GsonBuilder gb) throws IOException {
        errorMessage em = new errorMessage("I have no idea how this happened. internet magic, I suppose. Try again!");
        gb.registerTypeAdapter(ServerMessage.class, new ServerMessageAdapter());
        session.getRemote().sendString(gb.create().toJson(em));
    }

    private void invalidGameIDMsgSend(Session session, GsonBuilder gb) throws IOException {
        errorMessage em = new errorMessage("invalid game ID.");
        gb.registerTypeAdapter(ServerMessage.class, new ServerMessageAdapter());
        session.getRemote().sendString(gb.create().toJson(em));
    }

    private void PlayerSlotTakenMessageSend(Session session, GsonBuilder gb) throws IOException {
        errorMessage em = new errorMessage("player slot filled already.");
        gb.registerTypeAdapter(ServerMessage.class, new ServerMessageAdapter());
        session.getRemote().sendString(gb.create().toJson(em));
    }

    private static void invalidAuthTokenMessageSend(Session session, GsonBuilder gb) throws IOException {
        errorMessage em = new errorMessage("invalid auth token");
        gb.registerTypeAdapter(ServerMessage.class, new ServerMessageAdapter());
        session.getRemote().sendString(gb.create().toJson(em));
    }

    private void addSession(Session session, String authString, int gameID) {
        boolean add = true;
        for (int i = 0; i < connections.size(); i++) {
            var con = connections.elementAt(i);
            if (con.gameID == gameID) {
                if (con.authToken.equals(authString)) {
                    add = false;
                    if (con.sess.equals(session)) {
                        add = true;
                        add = false;
                    }
                }
            }
        }
        if (add) {
            conInfo ci = new conInfo();
            ci.sess = session;
            ci.authToken = authString;
            ci.gameID = gameID;
            connections.add(ci);
        }
    }

    private void broadcast(String message, int gameID, String tokenToAvoid) {
        for (int i = 0; i < connections.size(); i++) {
            var con = connections.elementAt(i);
            if (con.gameID == gameID) {
                if (!con.authToken.equals(tokenToAvoid)) {
                    try {
                        con.sess.getRemote().sendString(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private void configureDB() throws DataAccessException {
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
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error: db is corrupt");
        }

    }

    private Object joinGameHandler(Request request, Response response) throws DataAccessException {
        AuthData authData = getAuthData(request);

        ChessGame.TeamColor color = null;
        String body = request.body();
        String colorSTR = getParameter(body, "playerColor");
        if (colorSTR == null || colorSTR.equals("null")) {
            color = null;
        } else {
            if (colorSTR.toUpperCase().equals("WHITE")) {
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

    private AuthData getAuthData(Request request) throws DataAccessException {
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