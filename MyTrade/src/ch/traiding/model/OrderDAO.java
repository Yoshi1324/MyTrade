package ch.traiding.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ch.traiding.Bean.HomeBean;

/**
* @author  Joshua	Blöchliger
* @version 1.1
*/
public class OrderDAO {

	private Connection connection;

	public void createOrder(Order order, int menge) throws SQLException {
		if (connection == null) {
			throw new IllegalArgumentException("You must call useConnection before interacting with the database");
		}

		for (int i = 0; i < menge; i++) {
			String insert = "INSERT INTO verkauf (Symbol, Verkäufer_ID, Preis) VALUES(?,?,?)";
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
			String insert = "INSERT INTO verkauf (Symbol, Verkäufer_ID, Preis) VALUES(?,?,?)";
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

	public void finishOrder(Order order, User user) {
		if (connection == null) {
			throw new IllegalArgumentException("You must call useConnection before interacting with the database");
		}
		deletFromVerkauf(order.getId());
		if(order.getSeller().getId() != 1){
			deletFromUserAktien(order);
		}
		addUseraktie(order, user);
		
	}

	public void deletFromVerkauf(int order_id) {
		String insert = "DELETE FROM `mytrade`.`verkauf` WHERE `Verkauf_ID`=?;";
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement(insert);
			statement.setInt(1, order_id);
			statement.executeUpdate();
			connection.commit();
			statement.close();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void deletFromUserAktien(Order order) {
		String select = "Select Menge FROM `mytrade`.`useraktien` WHERE `User_ID`=? AND `Symbol`=?;";
		PreparedStatement statement = null;
		ResultSet result;
		int aktienMenge = 0;
		Double accountBalance = 0.0;
		try {
			statement = connection.prepareStatement(select);
			statement.setInt(1, order.getSeller().getId());
			statement.setString(2, order.getProduct().getSymbol());
			result = statement.executeQuery();
			if(result.next()){
				aktienMenge = result.getInt("Menge");
				aktienMenge = aktienMenge - 1;
			}
			result.close();		
		
			
			String delete = "DELETE FROM `mytrade`.`useraktien` WHERE `User_ID`=? AND `Symbol`=?;";
			statement = null;

			statement = connection.prepareStatement(delete);
			statement.setInt(1, order.getSeller().getId());
			statement.setString(2, order.getProduct().getSymbol());
			statement.executeUpdate();
			
			
			String insert = "INSERT INTO `mytrade`.`useraktien` (`Symbol`, `User_ID`, `Menge`) VALUES (?, ?, ?);";
			statement = null;

			statement = connection.prepareStatement(insert);
			statement.setString(1, order.getProduct().getSymbol());
			statement.setInt(2, order.getSeller().getId());
			statement.setInt(3, aktienMenge);
			statement.executeUpdate();
			
			
			select = "Select AccountBalance FROM `mytrade`.`user` WHERE `User_ID`=?;";
			statement = null;
			
			statement = connection.prepareStatement(select);
			statement.setInt(1, order.getSeller().getId());
			result = statement.executeQuery();
			if(result.next()){
				accountBalance = result.getDouble("AccountBalance");
				accountBalance = accountBalance + order.getPrice();
			}
			result.close();
			
			
			String update = "UPDATE `mytrade`.`user` SET `AccountBalance`=? WHERE `User_ID`=?";
			statement = connection.prepareStatement(update);
			statement.setDouble(1, accountBalance);
			statement.setInt(2, order.getSeller().getId());
			statement.executeUpdate();

			result.close();
			statement.close();
			connection.commit();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addUseraktie(Order order, User user){
		String select = "Select Menge FROM `mytrade`.`useraktien` WHERE `User_ID`=? AND `Symbol`=?;";
		PreparedStatement statement = null;
		ResultSet result;
		int aktienMenge = 0;
		try {
			statement = connection.prepareStatement(select);
			statement.setInt(1, user.getId());
			statement.setString(2, order.getProduct().getSymbol());
			result = statement.executeQuery();
			if(result.next()){
				aktienMenge = result.getInt("Menge");
				statement.close();
				String update = "UPDATE `mytrade`.`useraktien` SET `Menge`=? WHERE `User_ID`=? AND `Symbol`=?;";
				
				statement = connection.prepareStatement(update);
				statement.setInt(1, aktienMenge);
				statement.setInt(2, user.getId());
				statement.setString(3, order.getProduct().getSymbol());
				statement.executeUpdate();
			}else{
				statement.close();
				String insert = "INSERT INTO `mytrade`.`useraktien` (`Symbol`, `User_ID`, `Menge`) VALUES (?, ?, ?);";

				statement = connection.prepareStatement(insert);
				statement.setString(1, order.getProduct().getSymbol());
				statement.setInt(2, user.getId());
				statement.setInt(3, 1);
				statement.executeUpdate();
			}
			result.close();
			statement.close();
			connection.commit();
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<Order> getAllOrder() {
		if (connection == null) {
			throw new IllegalArgumentException("You must call useConnection before interacting with the database");
		}
		ArrayList<Order> allOrders = new ArrayList<Order>();
		Order order;
		String insert = "SELECT  verkauf.Verkauf_ID, verkauf.Symbol, verkauf.Preis, verkauf.Verkäufer_ID, aktien.Bezeichnung FROM verkauf INNER JOIN aktien WHERE verkauf.symbol=aktien.symbol;";
		PreparedStatement statement = null;
		
		try {
				statement = connection.prepareStatement(insert);
			
			ResultSet result = statement.executeQuery();
			while (result.next()){
				order = new Order();
				order.setId(result.getInt("Verkauf_ID"));
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
