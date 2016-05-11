package ch.traiding.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class Test {

	public static void main(String[] args) {
		try {
			new Test().top();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	void top() throws Exception {
		ConnectionPooling pooling;
		pooling = ConnectionPoolingImplementation.getInstance(1, 5);
		System.out.println(pooling);
		
		Connection c1 = pooling.getConnection();
		Connection c2 = pooling.getConnection();
		Connection c3 = pooling.getConnection();
		
		// Test singleton:
		pooling = ConnectionPoolingImplementation.getInstance();
		
		pooling.putConnection(c2);
		pooling.putConnection(c3);
		System.out.println(pooling);

		// erster Fehler: Connection zurückgeben, welche nicht busy war:
		pooling.putConnection(c2);

		// zweiter Fehler: zu viele Connecitons beansprucht:
		pooling.getConnection();
		pooling.getConnection();
		pooling.getConnection();
		pooling.getConnection();
		pooling.getConnection(); // mehr als 5 sind nicht möglich

		//remark: c1 is still active
		Statement st = c1.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM `Ort`");
		while(rs.next()) {
			System.out.println(rs.getString("Ortsname"));
		}
	}
	
}
 // end of class Test