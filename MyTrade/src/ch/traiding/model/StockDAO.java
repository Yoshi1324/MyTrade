package ch.traiding.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author cme
 */
public class StockDAO {

    private Connection connection;

    public void insert(Stock product) throws SQLException {
        if (connection == null) {
            throw new IllegalArgumentException("You must call useConnection before interacting with the database");
        }

        String insert = "INSERT INTO T_STOCK (NAME, NOMINAL, PRICE, DIVIDEND) VALUES(?,?,?,?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(insert);
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getNominalPrice());
            statement.setDouble(3, product.getPrice());
            statement.setDouble(4, product.getDividend());
            statement.executeUpdate();
        } finally {
            statement.close();
        }
    }

    public void useConnection(Connection connection) {
        this.connection = connection;
    }
}
