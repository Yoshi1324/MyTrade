package ch.traiding.Bean;

import ch.traiding.model.*;
public class HomeBean {
	private User u = new User();
	private String user;
	private String password;
	private TradingService tService;
	
	public HomeBean(){
		tService = new TradingService();
		u = tService.login(user, password);
	}

	public User getU() {
		return u;
	}

	public void setU(User u) {
		this.u = u;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public TradingService gettService() {
		return tService;
	}

	public void settService(TradingService tService) {
		this.tService = tService;
	}
	
}
