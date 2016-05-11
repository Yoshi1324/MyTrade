package ch.traiding.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author cme
 */
public class UserDAO {

    private Connection connection;

    public User login(String user, String password) throws SQLException {


        if (connection == null) {
            throw new IllegalArgumentException("You must call useConnection before interacting with the database");
        }

        String insert = "SELECT ID, LOGIN, PASSWORD, ROLE, BALANCE FROM T_USER WHERE LOGIN=? AND PASSWORD=?";
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(insert);
            statement.setString(1, user);
            statement.setString(2, password);
            ResultSet result = statement.executeQuery();

            // OK, es hat ein Ergebnis
            if (result.next()) {
                User u = new User();
                u.setId(Integer.valueOf(result.getInt(1)));
                u.setUser(result.getString(2));

                String role = result.getString(4);
                // TODO check if the role is valid, then set it
                u.setRole(role);

                u.setAccountBalance(result.getDouble(5));
                return u;
            } else {
                return null; // Kein passendes Login gefunden
            }
        } finally {
            statement.close();
        }


    }

    public void useConnection(Connection connection) {
        this.connection = connection;
    }

    public void insert(User user) throws SQLException {
        if (connection == null) {
            throw new IllegalArgumentException("You must call useConnection before interacting with the database");
        }

        String insert = "INSERT INTO T_USER (LOGIN, PASSWORD, ROLE, BALANCE) VALUES(?,?,?,?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(insert);
            statement.setString(1, user.getUser());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole());
            statement.setDouble(4, user.getAccountBalance());
            statement.executeUpdate();
        } finally {
            statement.close();
        }
    }
}
