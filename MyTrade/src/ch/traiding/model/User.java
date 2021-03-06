package ch.traiding.model;

import java.util.ArrayList;

/**
* @author  Joshua	Blöchliger
* @version 1.1
*/
public class User {

    public static final String ROLE_TRADFER = "2";

    public static final String ROLE_ADMIN = "1";

    private Integer id;

    private String username;
    
    private String name;
    
    private String vorname;

    private String password;

    private Integer role;

    private double accountBalance;
    
    private TradingService tService = new TradingService();
    
    private ArrayList<Stock> Aktien = new ArrayList<Stock>();
    
    public ArrayList<Stock> getAktien() {
		return tService.getAllAktien(this.id);
	}

	public void setAktien(ArrayList<Stock> aktien) {
		Aktien = aktien;
	}
	
	public int getAktienMenge(String symbol){
		for(int i = 0;i < Aktien.size(); i++){
			if(Aktien.get(i).equals(symbol)){
				return Aktien.get(i).getMenge();
			}
		}
		return 0;
	}

	public User(){
    	
    	accountBalance = 10000.00;
    
    }
    
    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
