package ui;

import Models.AuthData;
import Requests.EscapeSequences;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chessGame.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import messages.*;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class client extends Endpoint {
    static Scanner userInput;
    static boolean isPlayer;
    static String playerColor;
    static ChessGameImp currentBoardState;

    public client() throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                ServerMessage sm;
                GsonBuilder gb = new GsonBuilder();
                gb.registerTypeAdapter(ServerMessage.class, new ServerMessageAdapter());
                sm = gb.create().fromJson(message, ServerMessage.class);
                DoTaskBasedOnType(sm);
            }
        });
    }

    private void DoTaskBasedOnType(ServerMessage sm) {
        switch (sm.getServerMessageType()) {
            case LOAD_GAME:
                loadGameMessage lgm = (loadGameMessage) sm;
                currentBoardState = new ChessGameImp((Map) lgm);
                if (!isPlayer || playerColor.toLowerCase().equals("white")) {
                    PrintBoardInGame(currentBoardState);
                }
                PrintBoardInGameButUpsideDownThisTime(currentBoardState);
                if (isPlayer && playerColor.toLowerCase().equals("black")) {
                    PrintBoardInGame(currentBoardState);
                }
                break;
            case ERROR:
                println(EscapeSequences.SET_TEXT_ERROR, "Error: ", ((errorMessage) sm).errorMessage, EscapeSequences.RESET_ALL);
                break;
            case NOTIFICATION:
                println("Info: ", ((notificationMessage) sm).message);
        }
    }

    public static Session session;

    private static Map readResponseBody(HttpURLConnection http) throws IOException {
        Map responseBody;
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            responseBody = new Gson().fromJson(inputStreamReader, Map.class);
        }
        return responseBody;
    }

    public void send(String msg) {
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        userInput = new Scanner(System.in);
        AuthData sessionInfo = null;
        //do nothing at the moment
        println(EscapeSequences.WHITE_QUEEN, "Welcome to CS 240 chess! Type \"help\" to get started!", EscapeSequences.BLACK_QUEEN);
        String command = userInput.next();
        try {
            URI uri;
            while (!(command.toLowerCase().equals("quit") || command.toLowerCase().equals("exit"))) {
                switch (command.toLowerCase()) {
                    case "help":
                        helpFunct(sessionInfo);
                        break;
                    case "login":
                        String usernme = userInput.next();
                        String pw = userInput.next();
                        checkRemainInput();
                        sessionInfo = loginFuct(usernme, pw, sessionInfo);
                        break;
                    case "register":
                        String username = userInput.next();
                        String paw = userInput.next();
                        String eml = userInput.next();
                        checkRemainInput();
                        sessionInfo = registerFunct(username, eml, paw, sessionInfo);
                        HttpURLConnection http1;
                        OutputStream ostrme;
                        break;
                    //postlogin commands
                    case "logout":
                        checkRemainInput();
                        if (sessionInfo == null) {
                            chastiseForNoSession();
                        } else {
                            sessionInfo = logoutFunct(sessionInfo);
                        }
                        break;
                    case "create":
                        String name = userInput.next();
                        checkRemainInput();
                        if (sessionInfo == null) {
                            chastiseForNoSession();
                        } else {
                            createGameFunct(sessionInfo, name);
                        }
                        break;
                    case "list":
                        checkRemainInput();
                        if (sessionInfo == null) {
                            chastiseForNoSession();
                        } else {

                            listAllGamesFunct(sessionInfo);

                        }
                        break;
                    case "join":
                        int id = userInput.nextInt();
                        String color = userInput.next();
                        if (color.toLowerCase().equals("null") | color.toLowerCase().equals("empty")) {
                            color = null;
                        }
                        checkRemainInput();
                        joinFunct(sessionInfo, color, id);
                        break;
                    case "observe":
                        int id2 = userInput.nextInt();
                        checkRemainInput();
                        observeFunct(sessionInfo, id2);
                        break;
                    default:
                        println(EscapeSequences.RESET_ALL, EscapeSequences.SET_TEXT_COLOR_RED, "Error: ", command, " is not a valid command word. Type \"help\" to get a list of valid commands and their parameters.");
                        break;
                }
                userInput.reset();
                System.out.print(EscapeSequences.RESET_ALL);
                command = userInput.next();
            }

        } catch (IllegalStateException |
                 NoSuchElementException e) {
            //system in was closed. closing program.
            System.out.println("System.in was closed. Somehow. You done goofed. This was probably closed at the same time. good luck.");
        } catch (
                Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


    public static boolean observeFunct(AuthData sessionInfo, int id2) throws URISyntaxException, IOException {
        HttpURLConnection http1;
        boolean output = false;
        OutputStream ostrme;
        if (sessionInfo == null) {
            chastiseForNoSession();
        } else {
            URI yuri = new URI("http://localhost:8080/game");
            http1 = (HttpURLConnection) yuri.toURL().openConnection();
            http1.setRequestMethod("PUT");
            http1.setDoOutput(true);
            http1.addRequestProperty("authorization", sessionInfo.getAuthToken());
            ostrme = http1.getOutputStream();
            addBodyParameter(ostrme, "playerColor", null, true, false);
            addBodyParameter(ostrme, "gameID", id2, false, true);
            http1.connect();

            switch (http1.getResponseCode()) {
                case 200:
                    try (InputStream is = http1.getInputStream()) {
                        InputStreamReader isr = new InputStreamReader(is);
                        println(EscapeSequences.SUCCESS_FONT, "successful join. Current state of the board is below:", EscapeSequences.RESET_ALL);
                        System.out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
                        var obj = readResponseBody(http1);

                        Map map2 = (Map) obj.get("game");
                        ChessGameImp cgi = new ChessGameImp(map2);
                        PrintBoardInGame(cgi);
                        PrintBoardInGameButUpsideDownThisTime(cgi);
                        println(EscapeSequences.RESET_ALL);
                        output = true;
                        var ws = new client();
                        connectedLoop(ws, false, sessionInfo, null, id2, userInput, cgi);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    println(EscapeSequences.SET_TEXT_ERROR, "Error ", String.valueOf(http1.getResponseCode()), ": ", http1.getResponseMessage());
                    output = false;
                    break;

            }
            http1.disconnect();
        }
        return output;
    }


    public static boolean joinFunct(AuthData sessionInfo, String color, int id) throws URISyntaxException, IOException {
        HttpURLConnection http1;
        if (!color.toUpperCase().equals("BLACK") && !color.toUpperCase().equals("WHITE") && !color.toUpperCase().equals("NULL")) {
            println(EscapeSequences.SET_TEXT_ERROR, "INVALID COLOR. BLACK, WHITE, OR NULL ONLY. TRY AGAIN.", EscapeSequences.RESET_ALL);
            return false;
        }
        boolean output = false;
        OutputStream ostrme;
        if (sessionInfo == null) {
            chastiseForNoSession();
        } else {
            URI yuri = new URI("http://localhost:8080/game");
            http1 = (HttpURLConnection) yuri.toURL().openConnection();
            http1.setRequestMethod("PUT");
            http1.setDoOutput(true);
            http1.addRequestProperty("authorization", sessionInfo.getAuthToken());
            ostrme = http1.getOutputStream();
            addBodyParameter(ostrme, "playerColor", color, true, false);
            addBodyParameter(ostrme, "gameID", id, false, true);
            http1.connect();

            switch (http1.getResponseCode()) {
                case 200:
                    output = true;
                    try (InputStream is = http1.getInputStream()) {
                        InputStreamReader isr = new InputStreamReader(is);
                        println(EscapeSequences.SUCCESS_FONT, "successful join. Current state of the board is below:", EscapeSequences.RESET_ALL);
                        System.out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
                        var obj = readResponseBody(http1);

                        Map map2 = (Map) obj.get("game");
                        ChessGameImp cgi = new ChessGameImp(map2);
                        if (color.toUpperCase().equals("WHITE") || color.toUpperCase().equals("NULL") || color.toUpperCase().equals("EMPTY")) {
                            PrintBoardInGame(cgi);
                        }
                        PrintBoardInGameButUpsideDownThisTime(cgi);
                        if (color.toUpperCase().equals("BLACK")) {
                            PrintBoardInGame(cgi);
                        }
                        println(EscapeSequences.RESET_ALL);
                        var ws = new client();
                        connectedLoop(ws, true, sessionInfo, color, id, userInput, cgi);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    println(EscapeSequences.SET_TEXT_ERROR, "Error ", String.valueOf(http1.getResponseCode()), ": ", http1.getResponseMessage());
                    break;

            }
            http1.disconnect();
        }
        return output;
    }

    private static void connectedLoop(client ws, boolean player, AuthData sessionInfo, String color, int id, Scanner input, ChessGameImp cgi) {
        println(EscapeSequences.WHITE_QUEEN, "Welcome to CS 240 chess! Type \"help\" to get started!", EscapeSequences.BLACK_QUEEN);
        isPlayer = player;
        playerColor = color;
        String command = userInput.next();
        currentBoardState = cgi;
        try {
            URI uri;
            while (!(command.toLowerCase().equals("leave") || command.toLowerCase().equals("exit"))) {
                switch (command.toLowerCase()) {
                    case "help":
                        printHelp("help", "displays all valid commands");
                        printHelp("redraw", "draws an updated version of the chess board on your screen");
                        if (player) {
                            printHelp("make", "a move. Must be formatted like \"make A1:A3\", where A1 is" +
                                    " the starting position and A3 is the ending position. Letters are rows, numbers are columns." +
                                    "If the move would promote a pawn, we ask that you type that singly spaced afterward.");
                            printHelp("resign", "give up on a match. We'll ask for confirmation, don't worry.");
                            printHelp("highlight", "all legal moves. we'll show you what moves are legal at the moment.");
                        }
                        break;
                    case "redraw":
                        if (!player || color.toLowerCase().equals("white")) {
                            PrintBoardInGame(currentBoardState);
                        }
                        PrintBoardInGameButUpsideDownThisTime(currentBoardState);
                        if (player && color.toLowerCase().equals("black")) {
                            PrintBoardInGame(currentBoardState);
                        }
                        break;
                    case "make":
                        if (player) {
                            String move = userInput.next();
                            int startX = move.toLowerCase().charAt(0) - 'a';
                            int startY = move.toLowerCase().charAt(1) - '1';
                            int endX = move.toLowerCase().charAt(3) - 'a';
                            int endY = move.toLowerCase().charAt(4) - '1';

                            ChessMoveImp cmi = new ChessMoveImp(new ChessPositionImp(startX + 1, startY + 1), new ChessPositionImp(endX + 1, endY + 1), ChessPiece.PieceType.valueOf(userInput.next().toUpperCase()));
                            makeMove mm = new makeMove(sessionInfo.getAuthToken(), id, cmi);
                            ws.send(new Gson().toJson(mm, makeMove.class));
                        } else {
                            printHelp("Please", "make sure that you are connected as a player before doing that.");
                        }
                        break;
                    case "resign":
                        if (player) {
                            println("Are you sure you want to quit? Y/N");
                            if (userInput.next().toLowerCase().equals("y")) {
                                resign res = new resign(sessionInfo.getAuthToken(), id);
                                ws.send(new Gson().toJson(res, resign.class));
                            }
                        } else {
                            printHelp("Please", "make sure that you are connected as a player before doing that.");
                        }
                        break;
                    case "highlight":
                        println("What piece do you want to highlight all valid moves for? Use chess notation to signify the position of the piece.");
                        String pos = userInput.next();
                        int x = pos.toLowerCase().charAt(0) - 'a';
                        int y = pos.toLowerCase().charAt(1) - '1';
                        ChessPositionImp cpi = new ChessPositionImp(x + 1, y + 1);
                        Vector<ChessMove> validMoves = (Vector<ChessMove>) cgi.validMoves(cpi);
                        Vector<ChessPositionImp> validPos = new Vector<>();
                        for (int i = 0; i < validMoves.size(); i++) {
                            validPos.add((ChessPositionImp) validMoves.get(i).getEndPosition());
                        }
                        if (!player || color.toLowerCase().equals("white")) {
                            PrintBoardInGame(currentBoardState, validPos);
                        }
                        PrintBoardInGameButUpsideDownThisTime(currentBoardState, validPos);
                        if (player && color.toLowerCase().equals("black")) {
                            PrintBoardInGame(currentBoardState, validPos);
                        }
                        break;
                    default:
                        println(EscapeSequences.RESET_ALL, EscapeSequences.SET_TEXT_COLOR_RED, "Error: ", command, " is not a valid command word. Type \"help\" to get a list of valid commands and their parameters.");
                        break;
                }
                userInput.reset();
                System.out.print(EscapeSequences.RESET_ALL);
                command = userInput.next();
            }
            leave lv = new leave(sessionInfo.getAuthToken(), id);
            ws.send(new Gson().toJson(lv, lv.getClass()));

        } catch (IllegalStateException |
                 NoSuchElementException e) {
            //system in was closed. closing program.
            System.out.println("System.in was closed. Somehow. You done goofed. This was probably closed at the same time. good luck.");
        } catch (
                Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public static int listAllGamesFunct(AuthData sessionInfo) throws URISyntaxException, IOException {
        URI uri;
        HttpURLConnection http1;
        int output = -1;
        uri = new URI("http://localhost:8080/game");
        http1 = null;
        http1 = (HttpURLConnection) uri.toURL().openConnection();
        http1.setRequestMethod("GET");
        http1.setDoOutput(true);
        http1.addRequestProperty("authorization", sessionInfo.getAuthToken());
        http1.connect();

        switch (http1.getResponseCode()) {
            case 200:
                try (InputStream is = http1.getInputStream()) {
                    InputStreamReader isr = new InputStreamReader(is);
                    Map<String, Object> obj = readResponseBody(http1);
                    List l = (List) obj.get("games");
                    println(EscapeSequences.SUCCESS_FONT, printData(l), EscapeSequences.RESET_ALL);
                    System.out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
                    output = l.size();
                }
                break;
            default:
                println(EscapeSequences.SET_TEXT_ERROR, "Error ", String.valueOf(http1.getResponseCode()), ": ", http1.getResponseMessage());
                break;

        }
        http1.disconnect();
        return output;
    }

    public static int createGameFunct(AuthData sessionInfo, String name) throws URISyntaxException, IOException {
        HttpURLConnection http1;
        URI uri;
        OutputStream ostrme;
        uri = new URI("http://localhost:8080/game");
        http1 = (HttpURLConnection) uri.toURL().openConnection();
        http1.setRequestMethod("POST");
        http1.setDoOutput(true);
        String b = "-1";
        http1.addRequestProperty("authorization", sessionInfo.getAuthToken());
        ostrme = http1.getOutputStream();
        addBodyParameter(ostrme, "gameName", name, true, true);
        http1.connect();

        switch (http1.getResponseCode()) {
            case 200:
                try (InputStream is = http1.getInputStream()) {
                    InputStreamReader isr = new InputStreamReader(is);
                    Map obj = readResponseBody(http1);
                    b = new BigDecimal((Double) obj.get("gameID")).toPlainString();
                    println(EscapeSequences.SUCCESS_FONT, "Created game with name: \"", name, "\" and game ID of \"", b, "\"", EscapeSequences.RESET_ALL);
                    System.out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
                }
                break;
            default:
                println(EscapeSequences.SET_TEXT_ERROR, "Error ", String.valueOf(http1.getResponseCode()), ": ", http1.getResponseMessage());
                break;

        }
        http1.disconnect();
        return Integer.valueOf(b);
    }

    public static AuthData logoutFunct(AuthData sessionInfo) throws URISyntaxException, IOException {
        HttpURLConnection http1;
        URI uri;
        uri = new URI("http://localhost:8080/session");
        http1 = null;
        http1 = (HttpURLConnection) uri.toURL().openConnection();
        http1.setRequestMethod("DELETE");
        http1.setDoOutput(true);
        http1.addRequestProperty("authorization", sessionInfo.getAuthToken());
        http1.connect();

        switch (http1.getResponseCode()) {
            case 200:
                try (InputStream is = http1.getInputStream()) {
                    InputStreamReader isr = new InputStreamReader(is);
                    println(EscapeSequences.SUCCESS_FONT, "successful log out.", EscapeSequences.RESET_ALL);
                    System.out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
                    sessionInfo = null;
                }
                http1.disconnect();
                break;
            default:
                println(EscapeSequences.SET_TEXT_ERROR, "Error ", String.valueOf(http1.getResponseCode()), ": ", http1.getResponseMessage());
                break;

        }
        return sessionInfo;
    }

    public static AuthData registerFunct(String username, String eml, String paw, AuthData sessionInfo) throws URISyntaxException, IOException {
        URI uri;
        uri = new URI("http://localhost:8080/user");
        HttpURLConnection http1 = (HttpURLConnection) uri.toURL().openConnection();
        http1.setRequestMethod("POST");
        http1.setDoOutput(true);
        var ostrme = http1.getOutputStream();
        addBodyParameter(ostrme, "username", username, true, false);
        addBodyParameter(ostrme, "email", eml);
        addBodyParameter(ostrme, "password", paw, false, true);
        http1.connect();

        switch (http1.getResponseCode()) {
            case 200:
                try (InputStream is = http1.getInputStream()) {
                    InputStreamReader isr = new InputStreamReader(is);
                    println(EscapeSequences.SUCCESS_FONT, "Logged in as \"", username, "\"", EscapeSequences.RESET_ALL);
                    System.out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
                    Map obj = readResponseBody(http1);
                    sessionInfo = new AuthData(obj.get("username").toString(), obj.get("authToken").toString());
                }
                break;
            default:
                println(EscapeSequences.SET_TEXT_ERROR, "Error ", String.valueOf(http1.getResponseCode()), ": ", http1.getResponseMessage());
                break;

        }
        http1.disconnect();
        return sessionInfo;
    }

    public static AuthData loginFuct(String usernme, String pw, AuthData sessionInfo) throws URISyntaxException, IOException {
        URI uri;
        sessionInfo = null;
        uri = new URI("http://localhost:8080/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        var ostrm = http.getOutputStream();
        addBodyParameter(ostrm, "username", usernme, true, false);
        addBodyParameter(ostrm, "password", pw, false, true);
        http.connect();

        switch (http.getResponseCode()) {
            case 200:
                try (InputStream is = http.getInputStream()) {
                    InputStreamReader isr = new InputStreamReader(is);
                    println(EscapeSequences.SUCCESS_FONT, "Logged in as \"", usernme, "\"", EscapeSequences.RESET_ALL);
                    System.out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
                    Map obj = readResponseBody(http);
                    sessionInfo = new AuthData(obj.get("username").toString(), obj.get("authToken").toString());
                }
                break;
            default:
                println(EscapeSequences.SET_TEXT_ERROR, "Error ", String.valueOf(http.getResponseCode()), ": ", http.getResponseMessage());
                break;
        }
        http.disconnect();
        return sessionInfo;
    }

    public static void helpFunct(AuthData sessionInfo) {
        if (sessionInfo == null) {
            printHelp("register <USERNAME> <PASSWORD> <EMAIL>", " - to create an account");
            printHelp("login <USERNAME> <PASSWORD>", " - to login to a previously created account.");

        } else {
            printHelp("create <NAME> ", "- create a game with name Name");
            printHelp("list", " - list all games on the server.");
            printHelp("join <ID> [WHITE|BLACK|<empty>]", " - a game. Nothing in the White/Black slot becomes an Observe request.");
            printHelp("observe <ID>", " - a game");
            printHelp("logout", "remove your session credentials from the server.");
        }
        printHelp("quit", " to close the application");
        printHelp("help", " to see a context-sensitive list of commands.");
        println(EscapeSequences.RESET_ALL);
    }

    private static void PrintBoardInGame(ChessGameImp cgi) {
        ChessBoardImp cbi = (ChessBoardImp) cgi.getBoard();
        String topBottom = "    a  b   c   d  e   f   g   h ";
        println(EscapeSequences.SET_TEXT_OUTSIDE, topBottom, EscapeSequences.SET_TEXT_OUTSIDE);
        boolean BoardColorWhite = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(EscapeSequences.SET_TEXT_OUTSIDE).append(i + 1).append(" ").append(EscapeSequences.SET_TEXT_OUTSIDE);
            BoardColorWhite = !BoardColorWhite;
            for (int j = 0; j < 8; j++) {
                BoardColorWhite = isBoardColorWhite(cbi, BoardColorWhite, sb, i, j);
            }
            sb.append(EscapeSequences.SET_TEXT_OUTSIDE).append(" ").append(i + 1).append("\n");
        }

        println(sb.toString(), EscapeSequences.SET_TEXT_OUTSIDE, topBottom);
    }

    private static void PrintBoardInGame(ChessGameImp cgi, Vector<ChessPositionImp> validPos) {
        ChessBoardImp cbi = (ChessBoardImp) cgi.getBoard();
        String topBottom = "    a  b   c   d  e   f   g   h ";
        println(EscapeSequences.SET_TEXT_OUTSIDE, topBottom, EscapeSequences.SET_TEXT_OUTSIDE);
        boolean BoardColorWhite = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(EscapeSequences.SET_TEXT_OUTSIDE).append(i + 1).append(" ").append(EscapeSequences.SET_TEXT_OUTSIDE);
            BoardColorWhite = !BoardColorWhite;
            for (int j = 0; j < 8; j++) {
                BoardColorWhite = isBCGreen(validPos, cbi, BoardColorWhite, sb, i, j);
            }
            sb.append(EscapeSequences.SET_TEXT_OUTSIDE).append(" ").append(i + 1).append("\n");
        }

        println(sb.toString(), EscapeSequences.SET_TEXT_OUTSIDE, topBottom);
    }

    private static void PrintBoardInGameButUpsideDownThisTime(ChessGameImp cgi, Vector<ChessPositionImp> validPos) {
        ChessBoardImp cbi = (ChessBoardImp) cgi.getBoard();
        String topBottom = "    h  g   f   e  d   c   b   a ";
        println(EscapeSequences.SET_TEXT_OUTSIDE, topBottom, EscapeSequences.SET_TEXT_OUTSIDE);
        boolean BoardColorWhite = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            sb.append(EscapeSequences.SET_TEXT_OUTSIDE).append(i + 1).append(" ").append(EscapeSequences.SET_TEXT_OUTSIDE);
            BoardColorWhite = !BoardColorWhite;
            for (int j = 7; j >= 0; j--) {

                BoardColorWhite = isBCGreen(validPos, cbi, BoardColorWhite, sb, i, j);
            }
            sb.append(EscapeSequences.SET_TEXT_OUTSIDE).append(" ").append(i + 1).append("\n");
        }

        println(sb.toString(), EscapeSequences.SET_TEXT_OUTSIDE, topBottom);
    }

    private static boolean isBCGreen(Vector<ChessPositionImp> validPos, ChessBoardImp cbi, boolean boardColorWhite, StringBuilder sb, int i, int j) {
        if (validPos.contains(new ChessPositionImp(i + 1, j + 1))) {
            ChessPieceImp piece = (ChessPieceImp) cbi.getPiece(new ChessPositionImp(i + 1, j + 1));
            if (piece != null) {
                if (piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                    sb.append(EscapeSequences.TEXT_CHESS_WHITEPC);
                } else {
                    sb.append(EscapeSequences.TEXT_CHESS_BLACKPC);
                }
                sb.append(EscapeSequences.SET_BG_COLOR_GREEN);
                sb.append(piece.getPieceTypeAsString());

            } else {
                sb.append(EscapeSequences.EMPTY);
            }
            boardColorWhite = !boardColorWhite;
        } else {
            boardColorWhite = isBoardColorWhite(cbi, boardColorWhite, sb, i, j);
        }
        return boardColorWhite;
    }

    private static void PrintBoardInGameButUpsideDownThisTime(ChessGameImp cgi) {
        ChessBoardImp cbi = (ChessBoardImp) cgi.getBoard();
        String topBottom = "    h  g   f   e  d   c   b   a ";
        println(EscapeSequences.SET_TEXT_OUTSIDE, topBottom, EscapeSequences.SET_TEXT_OUTSIDE);
        boolean BoardColorWhite = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            sb.append(EscapeSequences.SET_TEXT_OUTSIDE).append(i + 1).append(" ").append(EscapeSequences.SET_TEXT_OUTSIDE);
            BoardColorWhite = !BoardColorWhite;
            for (int j = 7; j >= 0; j--) {

                BoardColorWhite = isBoardColorWhite(cbi, BoardColorWhite, sb, i, j);
            }
            sb.append(EscapeSequences.SET_TEXT_OUTSIDE).append(" ").append(i + 1).append("\n");
        }

        println(sb.toString(), EscapeSequences.SET_TEXT_OUTSIDE, topBottom);
    }

    private static boolean isBoardColorWhite(ChessBoardImp cbi, boolean boardColorWhite, StringBuilder sb, int i, int j) {
        if (boardColorWhite) {
            sb.append(EscapeSequences.TEXT_CHESS_WHITEBG);
        } else {
            sb.append(EscapeSequences.TEXT_CHESS_BLACKBG);
        }
        ChessPieceImp piece = (ChessPieceImp) cbi.getPiece(new ChessPositionImp(i + 1, j + 1));
        if (piece != null) {
            if (piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                sb.append(EscapeSequences.TEXT_CHESS_WHITEPC);
            } else {
                sb.append(EscapeSequences.TEXT_CHESS_BLACKPC);
            }
            sb.append(piece.getPieceTypeAsString());

        } else {
            sb.append(EscapeSequences.EMPTY);
        }
        boardColorWhite = !boardColorWhite;
        return boardColorWhite;
    }

    private static void addBodyParameter(OutputStream os, String gameID, int id, boolean first, boolean last) throws IOException {
        if (first) {
            os.write("{\n".getBytes());
        }
        os.write(("  \"" + gameID + "\": " + id + "").getBytes());
        if (last) {
            os.write("\n}".getBytes());
        } else {
            os.write(",\n".getBytes());
        }
    }

    private static void chastiseForNoSession() {
        println(EscapeSequences.SET_TEXT_ERROR, "Please log in before performing actions that require you to be logged in.", EscapeSequences.RESET_ALL);
    }

    private static String printData(List l) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < l.size(); i++) {
            Map m = (Map) l.get(i);
            sb.append("Game ID: ");
            sb.append(String.format("%10d", ((Double) m.get("gameID")).intValue()));
            sb.append(";     Lobby Title: ");
            sb.append(String.format("%-8.8s", m.get("gameName")));
            sb.append("     Players: (W): ");
            if (m.get("whiteUsername") != null) {
                sb.append(String.format("%-12.12s", m.get("whiteUsername")));
            } else {
                sb.append("       EMPTY");
            }
            sb.append(", (B): ");
            if (m.get("blackUsername") != null) {
                sb.append(String.format("%-12.12s", m.get("blackUsername")));
            } else {
                sb.append("       EMPTY");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static void addBodyParameter(OutputStream os, String parameterName, String parameterContent) throws IOException {
        os.write(("  \"" + parameterName + "\": \"" + parameterContent + "\",\n").getBytes());
    }

    private static void addBodyParameter(OutputStream os, String parameterName, String parameterContent, boolean first, boolean last) throws IOException {
        if (first) {
            os.write("{\n".getBytes());
        }
        os.write(("  \"" + parameterName + "\": \"" + parameterContent + "\"").getBytes());
        if (last) {
            os.write("\n}".getBytes());
        } else {
            os.write(",\n".getBytes());
        }
    }

    private static void checkRemainInput() {
        if (!userInput.nextLine().isEmpty()) {
            println("There were too many inputs. Whitespaces are not allowed for any command or parameter. If this command fails, please try again, with whitespace used only to seperate individual parameters from each other and their hierarchical command.");
        }

    }

    private static void printHelp(String arg, String desc) {
        println(EscapeSequences.SET_TEXT_COLOR_BLUE, EscapeSequences.SET_TEXT_BOLD, arg, EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY, EscapeSequences.RESET_TEXT_BOLD_FAINT, desc);
    }


    private static void println(String... args) {
        for (String arg : args) {
            System.out.print(arg);
        }
        System.out.println();
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
