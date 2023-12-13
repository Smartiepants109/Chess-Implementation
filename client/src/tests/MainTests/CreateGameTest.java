package MainTests;

import Models.AuthData;
import org.junit.jupiter.api.Test;
import ui.client;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateGameTest {
    @Test
    public void createWorkingTest() {
        int i = -1;
        try {
            TestFactory.ClearDB();
            AuthData ad = TestFactory.SetupUser();
            i = client.createGameFunct(ad, "str");

        } catch (URISyntaxException e) {
            i = -1;
        } catch (IOException e) {
            i = -1;
        }
        assertNotEquals(-1, i);
    }

    @Test
    public void createWithIncorrectAuthDataInCache() {
        boolean i = false;
        try {
            TestFactory.ClearDB();
            AuthData ad = TestFactory.SetupUser();
            i = -1 == client.createGameFunct(new AuthData("wrong user", "lol"), "legit name");
        } catch (URISyntaxException e) {
            i = true;
        } catch (IOException e) {
            i = true;
        }
        assertTrue(i);
    }
}
