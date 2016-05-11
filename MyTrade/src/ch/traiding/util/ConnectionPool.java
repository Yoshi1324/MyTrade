package ch.traiding.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cme
 */
public class ConnectionPool {

    private static final ConnectionPool CONNECTION_POOL = new ConnectionPool(5);

    private List availableConnections;

    private List usedConnections;

    public ConnectionPool(int poolSize) {
        try {

            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            availableConnections = new ArrayList();
            usedConnections = new ArrayList();

            String url = "jdbc:derby:/mydb;create=true";
            for (int i = 0; i < poolSize; i++) {
                Connection connection = DriverManager.getConnection(url);
                availableConnections.add(connection);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized Connection getConnection() {
        if (availableConnections.isEmpty()) {
            throw new RuntimeException("Keine Verbindung mehr verfügbar");
        }

        Connection connection = (Connection) availableConnections.get(0);
        
        // TODO prüfen, ob die Verbindung noch funktioniert. Wenn nicht, lösche diese und erstelle eine neue!
        // die neue Verbindung muss selbstverständlich auch wieder zurückgegeben werden.

        availableConnections.remove(connection);
        usedConnections.add(connection);
        return connection;
    }

    public synchronized void returnConnection(Connection connection) {
        
        try {
            connection.rollback();
            connection.setAutoCommit(true);
        } catch(Exception e) {
            e.printStackTrace();
        }
        availableConnections.add(connection);
        usedConnections.remove(connection);
    }

    public static ConnectionPool getConnectionPool() {
        return CONNECTION_POOL;
    }
}
