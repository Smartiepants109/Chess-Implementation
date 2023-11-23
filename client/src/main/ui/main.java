package ui;

import Models.AuthData;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class main {
    static Scanner userInput;

    private static Map readResponseBody(HttpURLConnection http) throws IOException {
        Map responseBody;
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            responseBody = new Gson().fromJson(inputStreamReader, Map.class);
        }
        return responseBody;
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
                        println(EscapeSequences.RESET_TEXT_COLOR);
                        break;
                    case "login":
                        String usernme = userInput.next();
                        String pw = userInput.next();
                        checkRemainInput();
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
                        break;
                    case "register":
                        String username = userInput.next();
                        String paw = userInput.next();
                        String eml = userInput.next();
                        checkRemainInput();
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
                        break;
                    //postlogin commands
                    case "logout":
                        checkRemainInput();
                        if (sessionInfo == null) {
                            chastiseForNoSession();
                        } else {
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
                        }
                        break;
                    case "create":
                        String name = userInput.next();
                        checkRemainInput();
                        if (sessionInfo == null) {
                            chastiseForNoSession();
                        } else {
                            uri = new URI("http://localhost:8080/game");
                            http1 = (HttpURLConnection) uri.toURL().openConnection();
                            http1.setRequestMethod("POST");
                            http1.setDoOutput(true);
                            http1.addRequestProperty("authorization", sessionInfo.getAuthToken());
                            ostrme = http1.getOutputStream();
                            addBodyParameter(ostrme, "gameName", name, true, true);
                            http1.connect();

                            switch (http1.getResponseCode()) {
                                case 200:
                                    try (InputStream is = http1.getInputStream()) {
                                        InputStreamReader isr = new InputStreamReader(is);
                                        Map obj = readResponseBody(http1);
                                        String b = new BigDecimal((Double) obj.get("gameID")).toPlainString();
                                        println(EscapeSequences.SUCCESS_FONT, "Created game with name: \"", name, "\" and game ID of \"", b, "\"", EscapeSequences.RESET_ALL);
                                        System.out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
                                    }
                                    break;
                                default:
                                    println(EscapeSequences.SET_TEXT_ERROR, "Error ", String.valueOf(http1.getResponseCode()), ": ", http1.getResponseMessage());
                                    break;

                            }
                            http1.disconnect();
                        }
                        break;
                    case "list":
                        checkRemainInput();
                        if (sessionInfo == null) {
                            chastiseForNoSession();
                        } else {

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
                                    }
                                    break;
                                default:
                                    println(EscapeSequences.SET_TEXT_ERROR, "Error ", String.valueOf(http1.getResponseCode()), ": ", http1.getResponseMessage());
                                    break;

                            }
                            http1.disconnect();

                        }
                        break;
                    case "join":
                        int id = userInput.nextInt();
                        String color = userInput.next();
                        if (color.toLowerCase().equals("null") | color.toLowerCase().equals("empty")) {
                            color = null;
                        }
                        checkRemainInput();
                        if (sessionInfo == null) {
                            chastiseForNoSession();
                        } else {
                            URI yuri = new URI("http://localhost:8080/game");
                            http1 = (HttpURLConnection) yuri.toURL().openConnection();
                            http1.setRequestMethod("PUT");
                            http1.setDoOutput(true);
                            http1.addRequestProperty("authorization", sessionInfo.getAuthToken());
                            ostrme = http1.getOutputStream();
                            addBodyParameter(ostrme, "PlayerColor", color, true, false);
                            addBodyParameter(ostrme, "gameID", id, false, true);
                            http1.connect();

                            switch (http1.getResponseCode()) {
                                case 200:
                                    try (InputStream is = http1.getInputStream()) {
                                        InputStreamReader isr = new InputStreamReader(is);
                                        println(EscapeSequences.SUCCESS_FONT, "successful join. Current state of the board is below:", EscapeSequences.RESET_ALL);
                                        System.out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
                                        println(EscapeSequences.CHESS_FONT);
                                        Map<String, Object> obj = readResponseBody(http1);
                                        //TODO take linkedtreemap and convert it into something the user can see.
                                        

                                        println(EscapeSequences.RESET_ALL);

                                    }
                                    break;
                                default:
                                    println(EscapeSequences.SET_TEXT_ERROR, "Error ", String.valueOf(http1.getResponseCode()), ": ", http1.getResponseMessage());
                                    break;

                            }
                            http1.disconnect();
                        }
                        break;
                    case "observe":
                        int id2 = userInput.nextInt();
                        checkRemainInput();
                        //TODO observe a game.
                        break;
                    default:

                        println(EscapeSequences.RESET_ALL, EscapeSequences.SET_TEXT_COLOR_RED, "Error: ", command, " is not a valid command word. Type \"help\" to get a list of valid commands and their parameters.");
                        //TODO invalid command script.
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
            }
            sb.append("       EMPTY");
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
}
