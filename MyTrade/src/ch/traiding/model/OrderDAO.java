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

    public void createOrder(Order order, int menge) throws SQLException {
        if (connection == null) {
            throw new IllegalArgumentException("You must call useConnection before interacting with the database");
        }
        
        for(int i = 0; i < menge; i++){
	        String insert = "INSERT INTO verkauf (Symbol, Verkäufer_ID, Preis) VALUES(?,?,?)";
	        PreparedStatement statement = null;
	        try {
	            statement = connection.prepareStatement(insert);
	            statement.setString(1, order.getProduct().getSymbol());
	            if(order.getSeller().getId() == null){
	            	statement.setInt(2, 1);
	            }else{
	                statement.setInt(2, order.getSeller().getId());	
	            }
	            statement.setDouble(3, order.getPrice());
	            statement.executeUpdate();
	        
	        } finally {
	            statement.close();
	        }
        }
    }
    
    public void finishOrder(){
    	
    }

    public void useConnection(Connection connection) {
        this.connection = connection;
    }
}
