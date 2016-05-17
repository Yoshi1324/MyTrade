package ch.traiding.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author cme
 */
public class OrderDAO {

    private Connection connection;

    public void createOrder(Order order) throws SQLException {
        if (connection == null) {
            throw new IllegalArgumentException("You must call useConnection before interacting with the database");
        }

        String insert = "INSERT INTO T_STOCK (NAME, NOMINAL, PRICE, DIVIDEND) VALUES(?,?,?,?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(insert);
            statement.setString(1, order.getProduct().getSymbol());
            statement.setInt(2, order.getSeller().getId());
            statement.setDouble(3, order.getPrice());
            statement.executeUpdate();
        } finally {
            statement.close();
        }
    }
    
    public void finishOrder()

    public void useConnection(Connection connection) {
        this.connection = connection;
    }
}
