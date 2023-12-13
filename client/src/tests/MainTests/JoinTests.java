package MainTests;

import Models.AuthData;
import org.junit.jupiter.api.Test;
import ui.client;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JoinTests {
    @Test
    public void joinWorkingTest() {
        boolean i = true;
        try {
            TestFactory.ClearDB();
            AuthData ad = TestFactory.SetupUser();
            int id = TestFactory.addGame(ad);
            if (!client.joinFunct(ad, "WHITE", id)) {
                i = false;
            }
            if (!client.joinFunct(ad, "BLACK", id)) {
                i = false;
            }
        } catch (URISyntaxException e) {
            i = false;
        } catch (IOException e) {
            i = false;
        }
        assertTrue(i);
    }
    @Test
    public void joinIncorrectInput() {
        boolean i = true;
        try {
            TestFactory.ClearDB();
            AuthData ad = TestFactory.SetupUser();
            int id = TestFactory.addGame(ad);
            if (client.joinFunct(ad, "not correct", id)) {
                i = false;
            }
            if (client.joinFunct(ad, "pineapple", id)) {
                i = false;
            }
        } catch (URISyntaxException e) {
            i = true;
        } catch (IOException e) {
            i = true;
        }
        assertTrue(i);
    }
    @Test
    public void joinCapitalizationIncorrectInputTest() {
        boolean i = true;
        try {
            TestFactory.ClearDB();
            AuthData ad = TestFactory.SetupUser();
            int id = TestFactory.addGame(ad);
            if (!client.joinFunct(ad, "wHite", id)) {
                i = false;
                //should work still
            }
            if (!client.joinFunct(ad, "BlAcK", id)) {
                i = false;
                //once again, should still work.
            }
        } catch (URISyntaxException e) {
            i = false;
        } catch (IOException e) {
            i = false;
        }
        assertTrue(i);
    }

    @Test
    public void joinWithIncorrectAuthDataInCache() {
        boolean i = false;
        try {
            TestFactory.ClearDB();
            AuthData ad = TestFactory.SetupUser();
            int id = TestFactory.addGame(ad);
            i = !client.joinFunct(new AuthData("not", "user"),"WHITE", id);

        } catch (URISyntaxException e) {
            i = true;
        } catch (IOException e) {
            i = true;
        }
        assertTrue(i);
    }

    @Test
    public void joinWithIncorrectIDProvided() {
        boolean i = false;
        try {
            TestFactory.ClearDB();
            AuthData ad = TestFactory.SetupUser();
            int id = TestFactory.addGame(ad);
            i = !client.joinFunct(ad,"WHITE", 8008);

        } catch (URISyntaxException e) {
            i = true;
        } catch (IOException e) {
            i = true;
        }
        assertTrue(i);
    }
}
