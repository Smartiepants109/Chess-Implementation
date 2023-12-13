package MainTests;

import Models.AuthData;
import ui.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

public class TestFactory {

    public static final String username = "user";
    public static final String email = "email";
    public static final String password = "password";

    public static AuthData SetupUser() {
        AuthData data = new AuthData("invalid", "string");
        try {
            return client.registerFunct(username, email, password, data);
        } catch (URISyntaxException e) {

        } catch (IOException e) {


        }
        return null;
    }

    public static void ClearDB() throws URISyntaxException, IOException {
        HttpURLConnection http1;
        {
            URI yuri = new URI("http://localhost:8080/db");
            http1 = (HttpURLConnection) yuri.toURL().openConnection();
            http1.setRequestMethod("DELETE");

            http1.connect();

            switch (http1.getResponseCode()) {
                case 200:
                    System.out.println("DB successfully cleared.");
                    break;
                default:
                    break;

            }
            http1.disconnect();
        }
    }

    public static int addGame(AuthData ad) throws URISyntaxException, IOException {
        return client.createGameFunct(ad, "genericGame");
    }
}
