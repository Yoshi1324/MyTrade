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
public class StockDAO {

    private Connection connection;

    public void insert(Stock product) throws SQLException {
        if (connection == null) {
            throw new IllegalArgumentException("You must call useConnection before interacting with the database");
        }

        String insert = "INSERT INTO aktien (Symbol, Bezeichnung, NormalPreis, Preis, Dividende) VALUES(?,?,?,?,?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(insert);
            statement.setString(1, product.getSymbol());
            statement.setString(2, product.getName());
            statement.setDouble(3, product.getNominalPrice());
            statement.setDouble(4, product.getPrice());
            statement.setDouble(5, product.getDividend());
            statement.executeUpdate();
        } finally {
            statement.close();
        }
    }
    
    public void updateAktie(String symbol, double newDividende) throws SQLException{
        if (connection == null) {
            throw new IllegalArgumentException("You must call useConnection before interacting with the database");
        }

        String insert = "Update aktien (Dividende) VALUES(?) where Symbol=?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(insert);
            statement.setDouble(1, newDividende);
            statement.setString(2, symbol);
            statement.executeUpdate();
        } finally {
            statement.close();
        }
    }

    public ArrayList<Stock> getAllAktien() throws SQLException{
    	ArrayList<Stock> aktienList = new ArrayList<Stock>();
    	Stock aktie;
    	String insert = "SELECT Symbol, Bezeichnung, NormalPreis, Preis, Dividende FROM aktien";
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(insert);

            ResultSet result = statement.executeQuery();

            // OK, es hat ein Ergebnis
            while (result.next()) {
                aktie = new Stock();
                aktie.setSymbol(result.getString("Symbol"));
                aktie.setName(result.getString("Bezeichnung"));
                aktie.setNominalPrice(result.getDouble("NormalPreis"));
                aktie.setPrice(result.getDouble("Preis"));
                aktie.setDividend(result.getDouble("Dividende"));
                System.out.println(aktie.getSymbol());
                aktienList.add(aktie);
            } 
            return aktienList;
        } finally {
            statement.close();
        }
    }
    
    public void useConnection(Connection connection) {
        this.connection = connection;
    }
}
