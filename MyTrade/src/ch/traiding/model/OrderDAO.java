package ch.traiding.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author cme
 */
public class OrderDAO {

	private Connection connection;

	public void createOrder(Order order, int menge) throws SQLException {
		if (connection == null) {
			throw new IllegalArgumentException("You must call useConnection before interacting with the database");
		}

		for (int i = 0; i < menge; i++) {
			String insert = "INSERT INTO verkauf (Symbol, VerkÃ¤ufer_ID, Preis) VALUES(?,?,?)";
			PreparedStatement statement = null;
			try {
				System.out.println(order.getProduct().getSymbol());
				statement = connection.prepareStatement(insert);
				statement.setString(1, order.getProduct().getSymbol());
				statement.setInt(2, 1);
				statement.setDouble(3, order.getPrice());
				statement.executeUpdate();

			} finally {
				statement.close();
			}
		}
	}

	public void createOrderUser(Order order, int menge) throws SQLException {
		if (connection == null) {
			throw new IllegalArgumentException("You must call useConnection before interacting with the database");
		}

		for (int i = 0; i < menge; i++) {
			String insert = "INSERT INTO verkauf (Symbol, VerkÃ¤ufer_ID, Preis) VALUES(?,?,?)";
			PreparedStatement statement = null;
			try {
				System.out.println(order.getProduct().getSymbol() + " test");
				statement = connection.prepareStatement(insert);
				System.out.println(order.getProduct().getSymbol());
				statement.setString(1, order.getProduct().getSymbol());
				System.out.println(order.getSeller().getId());
				statement.setInt(2, order.getSeller().getId());
				System.out.println(order.getPrice());
				statement.setDouble(3, order.getPrice());
				statement.executeUpdate();
				connection.commit();

			} finally {
				statement.close();
			}
		}
	}

	public void finishOrder() {

	}

	public ArrayList<Order> getAllOrder() {
		if (connection == null) {
			throw new IllegalArgumentException("You must call useConnection before interacting with the database");
		}
		ArrayList<Order> allOrders = new ArrayList<Order>();
		Order order;
		String insert = "SELECT  verkauf.Symbol, verkauf.Preis, verkauf.Verkäufer_ID, aktien.Bezeichnung FROM verkauf INNER JOIN aktien WHERE verkauf.symbol=aktien.symbol;";
		PreparedStatement statement = null;
		
		try {
				statement = connection.prepareStatement(insert);
			
			ResultSet result = statement.executeQuery();
			while (result.next()){
				order = new Order();
				order.setPrice(result.getDouble("Preis"));
				order.getSeller().setId(result.getInt("Verkäufer_ID"));
				order.getProduct().setSymbol(result.getString("Symbol"));
				order.getProduct().setName(result.getString("Bezeichnung"));
				allOrders.add(order);
			}
				statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allOrders;
}

	public void useConnection(Connection connection) {
		this.connection = connection;
	}
}
