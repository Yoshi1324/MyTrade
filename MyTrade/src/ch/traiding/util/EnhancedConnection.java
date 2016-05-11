package ch.traiding.util;

import java.sql.Connection;
/**
 * Connection enhanced for the connection pooling.
 * a) give it a name
 * b) give a creation timestamp
 * c) give a checkout timestamp
 * @version 0.1 (Jul 10, 2015)
 * @author Philipp Gressly Freimann 
 *         (philipp.gressly@santis.ch)
 */
public class EnhancedConnection {

	private Connection connection       ;
	private long       creationTimeStamp;
	private long       checkOutTimeStamp;
	private String     name             ;


	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public long getCreationTimeStamp() {
		return creationTimeStamp;
	}
	public void setCreationTimeStamp(long creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}

	public long getCheckOutTimeStamp() {
		return checkOutTimeStamp;
	}
	public void setCheckOutTimeStamp(long checkOutTimeStamp) {
		this.checkOutTimeStamp = checkOutTimeStamp;
	}

	public void setName(char c) {
		this.name = "" + c;
	}
	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		long since = 0;
		StringBuffer description = new StringBuffer("Connection " + getName());
		if(0 == getCheckOutTimeStamp()) {
			description.append(" - free - ");
		} else { 
			description.append(" - busy - ");
			since = System.currentTimeMillis() - getCheckOutTimeStamp();
			description.append(" (since: " + ((int) (since/1000)) + ")");
		}
		return description.toString();
	}

} // end of class EnhancedConnection