package ch.traiding.model;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NoPermissionException;

import java.sql.*;
import ch.traiding.*;
import ch.traiding.util.ConnectionPoolingImplementation;
import ch.traiding.util.Dividendenaenderung;

/**
* @author  Joshua	Blöchliger
* @version 1.1
*/
public class TradingService {
	//String connectionURL = "jdbc:mysql://localhost/myTrade";
	ConnectionPoolingImplementation connectionPool;
	
    public TradingService() {

		try {
			connectionPool = ConnectionPoolingImplementation.getInstance();
		} catch (NoPermissionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void storno(Order order){
    	Connection connection = connectionPool.getConnection();
    	
    	try {
			connection.setAutoCommit(false);
			OrderDAO orderDAO = new OrderDAO();
			orderDAO.useConnection(connection);
			orderDAO.deletFromVerkauf(order.getId());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public synchronized User login(String user, String password) {
        Connection connection = connectionPool.getConnection();

        try {
            UserDAO userDAO = new UserDAO();
            userDAO.useConnection(connection);
            User u = userDAO.login(user, password);
            u.setAktien(userDAO.getAllAktienforUser(u.getId()));
            System.out.println("BENUTZER: " + u); // TODO remove sysout Statements

            return u;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
        	connectionPool.putConnection(connection);
        }

    }
    
    public synchronized ArrayList<Stock> getAllAktien(int user_ID) {
        Connection connection = connectionPool.getConnection();

        try {
            UserDAO userDAO = new UserDAO();
            userDAO.useConnection(connection);
            ArrayList<Stock> aktienList = userDAO.getAllAktienforUser(user_ID);
            return aktienList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
        	connectionPool.putConnection(connection);
        }

    }
    
    public synchronized void newUser(User user) throws SQLException{
    	Connection connection = connectionPool.getConnection();

    	UserDAO userDAO = new UserDAO();
    	userDAO.useConnection(connection);
    	userDAO.insert(user);
    }
    
    public synchronized void payDividend() {

    	
		PreparedStatement prepStmt;
		PreparedStatement whilePrepStmt;
		Connection connection = connectionPool.getConnection();

		try {
		//Dividene aller Aktien neu setzen
		String getDivQuery = "SELECT * FROM mytrade.aktien";
		
			prepStmt = connection.prepareStatement(getDivQuery);

		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			double tempDiv;
			tempDiv = Dividendenaenderung.neueDividende(rs.getDouble("Dividende"), Dividendenaenderung.MITTLERE_STREUUNG,
					1, 50);

			String divUpdateQuery = "UPDATE aktien SET aktien.Dividende=" + tempDiv + " WHERE aktien.Symbol = '"
									+ rs.getString("Symbol") + "';";
			whilePrepStmt = connection.prepareStatement(divUpdateQuery);
			whilePrepStmt.executeUpdate();
			whilePrepStmt.close();
		}
		rs.close();
		prepStmt.close();
		//AccountBalance berechnen und Updaten
		String sqlAktQuery = "Select * FROM mytrade.useraktien JOIN aktien ON useraktien.Symbol=aktien.Symbol "
							+ "JOIN user ON useraktien.User_ID = user.User_ID;";

		prepStmt = connection.prepareStatement(sqlAktQuery);
		rs = prepStmt.executeQuery();
		

		while (rs.next()) {
			int tempVoucher;
			double tempBalance;
			tempVoucher = rs.getInt("Menge") * rs.getInt("Dividende");

			tempBalance = rs.getDouble("AccountBalance") + tempVoucher;

			String balanceUpdateQuery = "UPDATE user SET user.AccountBalance=" + tempBalance
										+ " WHERE User_ID = '" + rs.getInt("User_ID") + "';";
			whilePrepStmt = connection.prepareStatement(balanceUpdateQuery);
			whilePrepStmt.executeUpdate();
			whilePrepStmt.close();

			connectionPool.putConnection(connection);


		
		}
		rs.close();
		prepStmt.close();
		} catch (SQLException e) {
			System.out.println("FEEEEEEEEEEEEEEEEEEEEEEEEEEEEHLER beim berechnen der Dividene");
			e.printStackTrace();
		}
}

    public void createProduct(Stock product, int stock) {
    	Connection connection = connectionPool.getConnection();
        try {
            connection.setAutoCommit(false);
            StockDAO productDAO = new StockDAO();
            productDAO.useConnection(connection);
            product.setPrice(product.getNominalPrice());
            productDAO.insert(product);
            
            OrderDAO orderDAO = new OrderDAO();
            Order order= new Order();
            order.setProduct(product);
            order.setPrice(product.getNominalPrice());
            orderDAO.useConnection(connection);
            orderDAO.createOrder(order, stock);

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
        	connectionPool.putConnection(connection);
        }

    }

    public synchronized void buy(Order order, User user) {
    	Connection connection = connectionPool.getConnection();
    	
    	try {
			connection.setAutoCommit(false);
			OrderDAO orderDAO = new OrderDAO();
			orderDAO.useConnection(connection);
			orderDAO.finishOrder(order, user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public synchronized void sell(Order order, int menge) {
    	Connection connection = connectionPool.getConnection();
    	
    	try {
			connection.setAutoCommit(false);
			OrderDAO orderDAO = new OrderDAO();
			orderDAO.useConnection(connection);
			orderDAO.createOrderUser(order, menge);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }

    public synchronized ArrayList<Order> getOrderList() {
        // TODO implement DB code to read all open orders and return them, this is only a fake result here

    	Connection connection = connectionPool.getConnection();

        try {
            OrderDAO orderDAO = new OrderDAO();
            orderDAO.useConnection(connection);
            ArrayList<Order> allOrders = orderDAO.getAllOrder();
            
            return allOrders;

        } finally {
        	connectionPool.putConnection(connection);
        }
    }

    public void execute(Long orderId) {
    }

//    public void setupSystem() {
//        // Drop tables, create tables..
//
//    	ConnectionPoolingImplementation connectionPool = ConnectionPoolingImplementation.getInstance(1,20);
//        Connection connection = connectionPool.getConnection();
//        try {
//            connection.setAutoCommit(false);
//            Statement statement = connection.createStatement();
//
//            // Lösche, ob existiert oder nicht. 
//            dropTable("T_ORDER", statement);
//            dropTable("T_USER_STOCK", statement);
//            dropTable("T_USER", statement);
//            dropTable("T_STOCK", statement);
//
//            // SQL inkl. vielen wichtigen Constraints
//            String createProduct = "CREATE TABLE T_STOCK (ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT PK_STOCK PRIMARY KEY, NAME VARCHAR(50), NOMINAL DOUBLE, PRICE DOUBLE, DIVIDEND DOUBLE)";
//            String createUser = "CREATE TABLE T_USER (ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT PK_USER PRIMARY KEY, LOGIN VARCHAR(50), PASSWORD VARCHAR(50), ROLE VARCHAR(50), BALANCE DOUBLE)";
//            String createOrder = "CREATE TABLE T_ORDER (ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT PK_ORDER PRIMARY KEY, STOCK_ID INTEGER CONSTRAINT FK_STOCK REFERENCES T_STOCK, BUYER_ID INTEGER CONSTRAINT FK_BUYER REFERENCES T_USER, SELLER_ID INTEGER CONSTRAINT FK_SELLER REFERENCES T_USER, PRICE DOUBLE, EXECUTED SMALLINT)";
//            String createUserProduct = "CREATE TABLE T_USER_STOCK (USER_ID INTEGER CONSTRAINT FK_USER REFERENCES T_USER, STOCK_ID INTEGER CONSTRAINT FK_STOCK2 REFERENCES T_STOCK)";
//
//            statement.execute(createProduct);
//            statement.execute(createUser);
//            statement.execute(createOrder);
//            statement.execute(createUserProduct);
//
//            // create Administrator
//            User user = new User();
//            user.setUser("admin");
//            user.setPassword("admin");
//            user.setRole(User.ROLE_ADMIN);
//            user.setAccountBalance(0);
//
//            UserDAO userDAO = new UserDAO();
//            userDAO.useConnection(connection);
//
//            userDAO.insert(user);
//
//
//            connection.commit();
//
//            statement.close();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        } finally {
//
//            connectionPool.returnConnection(connection);
//        }
//    }
//
//    private void dropTable(String table, Statement statement) {
//        try {
//            statement.execute(SQLUtil.dropTable(table));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
