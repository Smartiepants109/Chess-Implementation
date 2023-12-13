package MainTests;

import Models.AuthData;
import org.junit.jupiter.api.Test;
import ui.client;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

public class login {
    @Test
    public void loginWorkingTest() {
        int i = 0;
        try {
            TestFactory.ClearDB();
            AuthData ad = TestFactory.SetupUser();
            int id = TestFactory.addGame(ad);
            i = 1;

            AuthData authData = client.loginFuct(TestFactory.username, TestFactory.password, ad);
            boolean b = !(null == authData);
            i = b ? 0 : 1;

        } catch (URISyntaxException e) {
            i = 1;
        } catch (IOException e) {
            i = 1;
        }
        assertEquals(0, i);
    }

    @Test
    public void loginWithIncorrectAuthDataInCache() {
        boolean i = false;
        try {
            TestFactory.ClearDB();
            AuthData ad = TestFactory.SetupUser();
            int id = TestFactory.addGame(ad);
            i = null == client.loginFuct("incorrect", "credentials", ad);
        } catch (URISyntaxException e) {
            i = true;
        } catch (IOException e) {
            i = true;
        }
        assertTrue(i);
    }
}
