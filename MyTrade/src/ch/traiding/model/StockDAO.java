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

        String insert = "INSERT INTO aktien (Symbol, Bezeichnung, NormalPreis, Preis, Dividende) VALUES(?,?,?,?,?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(insert);
            statement.setString(1, product.getSymbol());
            statement.setString(2, product.getName());
            statement.setDouble(3, product.getNominalPrice());
            statement.setDouble(4, product.getPrice());
            statement.setDouble(5, product.getDividend());
            statement.executeUpdate();
        } finally {
            statement.close();
        }
    }

    public void useConnection(Connection connection) {
        this.connection = connection;
    }
}
