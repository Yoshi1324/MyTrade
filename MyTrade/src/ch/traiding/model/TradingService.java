package ch.traiding.model;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NoPermissionException;

import java.sql.*;
import ch.traiding.*;
import ch.traiding.util.ConnectionPoolingImplementation;

/**
 *
 * @author cme
 */
public class TradingService {
	String connectionURL = "jdbc:mysql://localhost/myTrade";
	ConnectionPoolingImplementation connectionPool;
	
    public TradingService() {

		try {
			connectionPool = ConnectionPoolingImplementation.getInstance(1,5);
		} catch (NoPermissionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public User login(String user, String password) {
        Connection connection = connectionPool.getConnection();

        try {
            UserDAO userDAO = new UserDAO();
            userDAO.useConnection(connection);
            User u = userDAO.login(user, password);
            u.setAktien(userDAO.getAllAktienforUser(u));
            System.out.println("BENUTZER: " + u); // TODO remove sysout Statements

            return u;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
        	connectionPool.putConnection(connection);
        }

    }
    
    public void payDividend() {
    }

    public void createProduct(Stock product, int stock) {
        Connection connection =connectionPool .getConnection();

        try {
            connection.setAutoCommit(false);
            StockDAO productDAO = new StockDAO();
            productDAO.useConnection(connection);

            productDAO.insert(product);

            for (int i = 0; i < stock; i++) {
                // Create initial orders...
            }

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
        	connectionPool.putConnection(connection);;
        }

    }

    public void buy(Long orderId) {
    }

    public void sell(Long productId, double price) {
    }

    public List getOrderList() {
        // TODO implement DB code to read all open orders and return them, this is only a fake result here

        List orders = new ArrayList();
        for (int i = 0; i < 10; i++) {
            Order o = new Order();
            o.setId(Integer.valueOf(i));
            o.setPrice(i);
            orders.add(o);
        }
        return orders;
    }

    public List getProducts(Long userId) {

        // TODO implement DB code to read all products the user owns
        return null;
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
