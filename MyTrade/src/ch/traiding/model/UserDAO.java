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
        String insert = "SELECT User_ID, Benutzername, Name, Vorname, Passwort, Rolle, AccountBalance FROM user WHERE Benutzername=? AND Passwort=?";
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(insert);
            statement.setString(1, user);
            statement.setString(2, password);
            ResultSet result = statement.executeQuery();

            // OK, es hat ein Ergebnis
            if (result.next()) {
                User u = new User();
                u.setId(Integer.valueOf(result.getInt(1)));
                u.setUsername(result.getString(2));
                u.setName(result.getString(3));
                u.setVorname(result.getString(4));
                u.setPassword(result.getString(5));
                u.setRole(result.getInt(6));
                u.setAccountBalance(result.getDouble(7));
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
    	String insert = "SELECT aktien.Symbol, aktien.Bezeichnung, aktien.NormalPreis, aktien.Dividende FROM useraktien"
    			+ " LEFT JOIN aktien ON useraktien.User_ID = " + user.getId() + " AND useraktien.Symbol = aktien.Symbol";
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
                aktienList.add(aktie);
            } 
            return aktienList;
        } finally {
            statement.close();
        }
    }

    public void insert(User user) throws SQLException {
        String insert = "INSERT INTO T_USER (Benutzername, Name, Vorname, Passwort, Rolle, AccountBalance) VALUES(?,?,?,?,?,?)";
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
