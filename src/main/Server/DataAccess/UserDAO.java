package Server.DataAccess;

import Server.Models.User;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserUnitTests {
    @Test
    public void clearWorksTest() throws DataAccessException {
        Database db = new Database();
        UserDAO users = new UserDAO(db);
        users.clear();
        users.insertUser(new User("user", "pw", "em"));
        assertEquals(1, getSizeOfUsers(db));
        users.clear();
        assertEquals(0, getSizeOfUsers(db));
    }

    private int getSizeOfUsers(Database db) throws DataAccessException {
        try (var conn = db.getConnection()) {
            var qury = conn.prepareStatement("""
                                    
                        SELECT COUNT(*) FROM users
                    """);
            var rs = qury.executeQuery();
            rs.next();
            return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("DB error");
        }
    }

    @Test
    public void insertWorksTest() throws DataAccessException {
        Database db = new Database();
        UserDAO users = new UserDAO(db);
        users.clear();
        assertNull(users.findToken("u"));
        users.insertUser(new User("u", "p", "e"));
        assertNotNull(users.findToken("u"));
    }

    @Test
    public void insertFailsTest() throws DataAccessException {
        Database db = new Database();
        UserDAO users = new UserDAO(db);
        assertNull(users.findToken("u"));
        users.insertUser(new User("u", "p", "e"));
        boolean b = false;
        try {
            assertFalse(users.insertUser(new User("u", "p", "e")));
        } catch (Exception e) {
            b = true;
        }
        assertTrue(b);
    }

    @Test
    public void findSuccTest() throws DataAccessException {
        Database db = new Database();
        UserDAO users = new UserDAO(db);
        users.clear();
        users.insertUser(new User("u", "p", "e"));
        assertNotNull(users.findToken("u"));
    }

    @Test
    public void findFailTest() throws DataAccessException {
        Database db = new Database();
        UserDAO users = new UserDAO(db);
        users.clear();
        assertNull(users.findToken("u"));
    }
}


/**
 * Allows a user to interact with the database of users.
 */
public class UserDAO {
    /**
     * placeholder for database.
     */
    Database db;

    /**
     * creates the database access object.
     */
    public UserDAO(Database db) {
        this.db = db;
    }

    /**
     * attempts to insert a user to the database.
     *
     * @param user user to be added.
     * @return if the insertion was successful.
     * @throws DataAccessException if the database is unable to be accessed.
     */
    public boolean insertUser(User user) throws DataAccessException {
        try (var conn = db.getConnection()) {
            var preparedStatement = conn.prepareStatement("INSERT INTO users (username, pw, email) VALUES (?,?,?)");
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPw());
            preparedStatement.setString(3, user.getEmail());
            var rs = preparedStatement.executeUpdate();
            return !(rs == 0);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error: db is corrupt");
        }
    }

    /**
     * attempts to find the user with the username provided.
     *
     * @param username username of account that is being found.
     * @return null if user wasn't found, otherwise returns User object.
     * @throws DataAccessException when the database is unable to be accessed.
     */
    public User findToken(String username) throws DataAccessException {//username? Email? Find out which.
        try (var conn = db.getConnection()) {
            var preparedStatement = conn.prepareStatement("SELECT pw, email FROM users WHERE username=?");
            preparedStatement.setString(1, username);
            var rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String pw = rs.getString("pw");
                String email = rs.getString("email");
                return new User(username, pw, email);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error: db is corrupt");
        }
    }

    public void clear() throws DataAccessException {
        try (var conn = db.getConnection()) {
            var preparedStatement = conn.prepareStatement("TRUNCATE TABLE users");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error: db is corrupt");
        }
    }
}
