package MainTests;

import Models.AuthData;
import org.junit.jupiter.api.Test;
import ui.client;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class registerTest {
    @Test
    public void regiWorkingTest() {
        int i = 0;
        try {
            TestFactory.ClearDB();
            AuthData ad = new AuthData("lol", "naw");
            ad = client.registerFunct("a", "b", "a", ad);
            assertTrue(client.observeFunct(ad, TestFactory.addGame(ad)));
        } catch (URISyntaxException e) {
            i = 1;
        } catch (IOException e) {
            i = 1;
        }
        assertEquals(0, i);
    }

    @Test
    public void doubleRegi() {
        int i = 0;
        try {
            TestFactory.ClearDB();
            AuthData ad = new AuthData("lol", "naw");
            ad = client.registerFunct("a", "b", "a", ad);
            ad = client.registerFunct("a", "different", "otherInfo", ad);
            ad = client.loginFuct("a", "otherInfo", ad);
            assertTrue(ad == null);
        } catch (URISyntaxException e) {
            i = 1;
        } catch (IOException e) {
            i = 1;
        }
    }
}
