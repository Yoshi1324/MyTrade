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
public class UserDAO {

    private Connection connection;

    public UserDAO(){
    	
    }
    
    public User login(String user, String password) throws SQLException {
        String insert = "SELECT User_ID, Benutzername, Name, Vorname, Passwort_Hash, Rolle, AccountBalance FROM user WHERE Benutzername=? AND Passwort_Hash=?";
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(insert);
            statement.setString(1, user);
            statement.setString(2, password);
            ResultSet result = statement.executeQuery();

            // OK, es hat ein Ergebnis
            if (result.next()) {
                User u = new User();
                u.setId(Integer.valueOf(result.getInt("User_ID")));
                u.setUsername(result.getString("Benutzername"));
                u.setName(result.getString("Name"));
                u.setVorname(result.getString("Vorname"));
                u.setPassword(result.getString("Passwort_Hash"));
                u.setRole(result.getInt("Rolle"));
                u.setAccountBalance(result.getDouble("AccountBalance"));
                return u;
            } else {
                return null; // Kein passendes Login gefunden
            }
        } finally {
            statement.close();
        }


    }

    public void useConnection(Connection connection) {
        this.connection = connection;
    }
    
    public ArrayList<Stock> getAllAktienforUser(User user) throws SQLException{
    	Stock aktie;
    	ArrayList<Stock> aktienList = new ArrayList<Stock>();
    	String insert = "SELECT aktien.Symbol, aktien.Bezeichnung, aktien.NormalPreis, aktien.Dividende, useraktien.Menge FROM useraktien"
    			+ " INNER JOIN aktien ON useraktien.User_ID = " + user.getId() + " AND useraktien.Symbol = aktien.Symbol";
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(insert);

            ResultSet result = statement.executeQuery();

            // OK, es hat ein Ergebnis
            while (result.next()) {
                aktie = new Stock();
                aktie.setSymbol(result.getString("aktien.Symbol"));
                aktie.setName(result.getString("Bezeichnung"));
                aktie.setNominalPrice(result.getDouble("aktien.NormalPreis"));
                aktie.setPrice(result.getDouble("aktien.NormalPreis"));
                aktie.setDividend(result.getDouble("aktien.Dividende"));
                aktie.setMenge(result.getInt("useraktien.Menge"));
                System.out.println(aktie.getSymbol());
                aktienList.add(aktie);
            } 
            return aktienList;
        } finally {
            statement.close();
        }
    }

    public void insert(User user) throws SQLException {
        String insert = "INSERT INTO user (Benutzername, Name, Vorname, Passwort_Hash, Rolle, AccountBalance) VALUES(?,?,?,?,?,?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(insert);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getName());
            statement.setString(3, user.getVorname());
            statement.setString(4, user.getPassword());
            statement.setInt(5, user.getRole());
            statement.setDouble(6, user.getAccountBalance());
            statement.executeUpdate();
        } finally {
            statement.close();
        }
    }
}
