package Server.DataAccess;

import Server.Models.User;
import dataAccess.DataAccessException;
import dataAccess.Database;

import java.sql.SQLException;



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
