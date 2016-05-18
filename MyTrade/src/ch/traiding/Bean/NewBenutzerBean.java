package ch.traiding.Bean;

import java.sql.SQLException;
import java.util.HashMap;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import ch.traiding.model.*;

@ManagedBean(name = "newBenutzerBean")
@ApplicationScoped
public class NewBenutzerBean {
	private User user = new User();
    private TradingService tradingService = new TradingService();
    
    public String next(){
    	return "/faces/admin/Benutzerbestaetigung?faces-redirect=true";
    }
    
    public String saveUser() throws SQLException{
    	tradingService.newUser(user);
    	return "/faces/admin/Admin?faces-redirect=true";
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TradingService getTradingService() {
		return tradingService;
	}

	public void setTradingService(TradingService tradingService) {
		this.tradingService = tradingService;
	}
}
