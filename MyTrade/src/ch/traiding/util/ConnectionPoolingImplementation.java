package ch.traiding.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.naming.NoPermissionException;

/**
 * Connection Pool implementation.
 * MIN_CONNECTIONS are always built: Typically 1 (or 0: lazy instantiation).
 * More than MAX_CONNECTIONS will never be initialized.
 * @version 0.1 (May 27, 2015)
 * @author Philipp Gressly Freimann 
 *         (philipp.gressly@santis.ch)
 */
public class ConnectionPoolingImplementation implements ConnectionPooling {

	private final int MIN_CONNECTIONS;
	private final int MAX_CONNECTIONS;

	private List<EnhancedConnection> freePool;
	private List<EnhancedConnection> busyPool;

	// TODO: Dependency inject (Property file)
	final String treiberName   = "com.mysql.jdbc.Driver";
	// TODO: Property file
	final String connectionURL = "jdbc:mysql://127.0.0.1:3306/mytrade"; 

	// TODO: Security: store username and pwd externally
	private String myUserName    = "myTrade";
	private String myPassword    = "Lernende123";


	private ConnectionPoolingImplementation(int min, int max) throws ClassNotFoundException, SQLException {
		Class.forName(treiberName);

		MIN_CONNECTIONS = min;
		MAX_CONNECTIONS = max;
		initConnections();
	}


	private void initConnections() throws SQLException {
		freePool = new LinkedList<EnhancedConnection>();
		busyPool = new LinkedList<EnhancedConnection>();
		for(int i = 0; i < MIN_CONNECTIONS; i++) {
			EnhancedConnection eCon;
			eCon = createConnection();
			freePool.add(eCon);
		}
	}


	/**
	 * Create a SQL-Connection object
	 */
	static char id = 'A';
	private EnhancedConnection createConnection() throws SQLException {
		EnhancedConnection eCon;
		Connection         con ;
		
		con = DriverManager.getConnection(connectionURL, 
		    myUserName, myPassword);
		eCon = new EnhancedConnection();
		eCon.setConnection(con);
		eCon.setName(id++);
		eCon.setCheckOutTimeStamp(0);
		eCon.setCreationTimeStamp(System.currentTimeMillis());

		return eCon;
	}

/**
 * Singleton-Pattern
 */
	private static ConnectionPoolingImplementation theSingleton = null;


	public static synchronized ConnectionPoolingImplementation getInstance(int min, int max) throws NoPermissionException {
		if(null != theSingleton) {
			throw new NoPermissionException("Only the first call to getInstance must use min and max parameters");
		}
		if(null == theSingleton) {
			try {
				theSingleton = new ConnectionPoolingImplementation(min, max);
			} catch (ClassNotFoundException|SQLException e) {
				System.err.println("Exception in getInstance: " + e);
				e.printStackTrace();
				return null;
			}
		}
		return theSingleton;
	}


	public static synchronized ConnectionPoolingImplementation getInstance() throws NoPermissionException {
		if(null == theSingleton) {
			throw new NoPermissionException("Call getInstance first with min and max parameters");
		}
		return theSingleton;
	}
	


	@Override
	public synchronized Connection getConnection() {
		EnhancedConnection eCon = null;
		if(0 < freePool.size()) {
			eCon = freePool.remove(0);
			eCon.setCheckOutTimeStamp(System.currentTimeMillis());
			busyPool.add(eCon);
			return eCon.getConnection();
		}
		// no more free connections:
		if(totalConnectionCount() >= MAX_CONNECTIONS) {
			System.err.println("No more space in the pools. MAX (" +
		                     MAX_CONNECTIONS + ") exceeded.");
			return null;
		}
		try {
			eCon = createConnection();
		} catch (SQLException e) {
			System.err.println("Error (ConnectionPoolImplementation.getConnection()): " + e);
			e.printStackTrace();
			return null;
		}
		busyPool.add(eCon);
		return eCon.getConnection();
	}


	@Override
	public synchronized void putConnection(Connection con) {
		if(null == con) {
			throw new NullPointerException("putConnection called with 'null'!");
		}
		EnhancedConnection eCon = findConnectionInBusyPool(con);
		if(busyPool.remove(eCon)) {
			eCon.setCheckOutTimeStamp(0);
			freePool.add(eCon);
			return;
		}
		System.err.println("Error: Connection was not `busy`.");
	}


	/**
	 * Locate all busy enhanced connections
	 * @param con
	 * @return
	 */
	private EnhancedConnection findConnectionInBusyPool(Connection con) {
		for(EnhancedConnection ec : busyPool) {
			if(ec.getConnection() == con) {
				return ec;
			}
		}
		return null;
	}


	private synchronized int totalConnectionCount() {
		return freePool.size() + busyPool.size();
	}

	/**
	 * Debug purpuse. 
	 */
	@Override
	public String toString() {
		return "ConnectionPooling. Free: " + freePool.size() +
		       " Busy: " + busyPool.size();
	}
}
 // end of class ConnectionPoolingImplementation