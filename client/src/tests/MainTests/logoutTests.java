package MainTests;

import Models.AuthData;
import org.junit.jupiter.api.Test;
import ui.client;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

public class logoutTests {
    @Test
    public void logoutWorkingTest() {
        int i = 0;
        try {
            TestFactory.ClearDB();
            AuthData ad = TestFactory.SetupUser();
            int id = TestFactory.addGame(ad);
            i = 1;
            i = null == client.logoutFunct(ad) ? 0 : 1;

        } catch (URISyntaxException e) {
            i = 1;
        } catch (IOException e) {
            i = 1;
        }
        assertEquals(0, i);
    }

    @Test
    public void logoutWithIncorrectAuthDataInCache() {
        boolean i = false;
        try {
            TestFactory.ClearDB();
            AuthData ad = TestFactory.SetupUser();
            int id = TestFactory.addGame(ad);
            i = !client.observeFunct(new AuthData("not", "user"), id);
            assertNotNull(ad);
            //yeah. I'm not really sure how to "test" that you didn't log out? Like, you still have the thing there. so...
        } catch (URISyntaxException e) {
            i = true;
        } catch (IOException e) {
            i = true;
        }
        assertTrue(i);
    }
}
