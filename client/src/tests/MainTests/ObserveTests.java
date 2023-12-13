package MainTests;

import Models.AuthData;
import org.junit.jupiter.api.*;
import ui.client;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

public class ObserveTests {
    @Test
    public void observeWorkingTest(){
        int i = 0;
        try {
            TestFactory.ClearDB();
            AuthData ad = TestFactory.SetupUser();
            int id = TestFactory.addGame(ad);
            client.observeFunct(ad, id);

        } catch (URISyntaxException e) {
            i = 1;
        } catch (IOException e) {
            i = 1;
        }
        assertEquals(0,i);
    }
    @Test
    public void observeWithIncorrectAuthDataInCache(){
        boolean i = false;
        try {
            TestFactory.ClearDB();
            AuthData ad = TestFactory.SetupUser();
            int id = TestFactory.addGame(ad);
            i = !client.observeFunct(new AuthData("not", "user"), id);

        } catch (URISyntaxException e) {
            i = true;
        } catch (IOException e) {
            i = true;
        }
        assertTrue(i);
    }
    @Test
    public void observeWithIncorrectIDProvided(){
        boolean i = false;
        try {
            TestFactory.ClearDB();
            AuthData ad = TestFactory.SetupUser();
            int id = TestFactory.addGame(ad);
            i = !client.observeFunct(ad, 8008);

        } catch (URISyntaxException e) {
            i = true;
        } catch (IOException e) {
            i = true;
        }
        assertTrue(i);
    }

}
