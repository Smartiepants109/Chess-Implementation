package MainTests;

import Models.AuthData;
import org.junit.jupiter.api.Test;
import ui.client;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

public class listGamesTest {

    @Test
    public void listWorkingTest() {
        int i = -1;
        try {
            TestFactory.ClearDB();
            AuthData ad = TestFactory.SetupUser();
            TestFactory.addGame(ad);
            i = client.listAllGamesFunct(ad);
            assertEquals(1, i);
            TestFactory.addGame(ad);
            i = client.listAllGamesFunct(ad);
            assertEquals(2, i);
            TestFactory.addGame(ad);
            i = client.listAllGamesFunct(ad);
            assertEquals(3, i);
            TestFactory.addGame(ad);
            i = client.listAllGamesFunct(ad);
            assertEquals(4, i);
        } catch (URISyntaxException e) {
            i = -1;
        } catch (IOException e) {
            i = -1;
        }
        assertFalse(i == -1);
    }

    @Test
    public void listWithIncorrectAuthDataInCache() {
        boolean i = false;
        try {
            TestFactory.ClearDB();
            AuthData ad = TestFactory.SetupUser();
            TestFactory.addGame(ad);
            TestFactory.addGame(ad);
            TestFactory.addGame(ad);
            i = 0 > client.listAllGamesFunct(new AuthData("incorrect", "data"));

        } catch (URISyntaxException e) {
            i = true;
        } catch (IOException e) {
            i = true;
        }
        assertTrue(i);
    }
}
