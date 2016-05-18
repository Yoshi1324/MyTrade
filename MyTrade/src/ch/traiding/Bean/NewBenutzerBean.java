package ch.traiding.Bean;

import java.sql.SQLException;

import ch.traiding.model.*;

public class NewBenutzerBean {
	private User user = new User();
    private TradingService tradingService = new TradingService();
    
    private String back(){
    	return "/faces/admin/Admin?faces-redirect=true";
    }
    
    private String back2(){
    	return "/faces/admin/Benutzererfassen?faces-redirect=true";
    }
    
    private String next(){
    	return "/faces/admin/Benutzerbestaetigung?faces-redirect=true";
    }
    
    private String saveUser() throws SQLException{
    	tradingService.newUser(user);
    	return "/faces/admin/Admin?faces-redirect=true";
    }
}
