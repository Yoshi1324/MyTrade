package ch.traiding.Bean;


import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import ch.traiding.model.*;

@ManagedBean(name = "homeBean")
@ApplicationScoped
public class HomeBean {
	private User u = new User();
	private String user;
	private String password;
	private TradingService tService;

	public HomeBean(){
		tService = new TradingService();
	}
	
	public String login(){
		u = tService.login(user, password);
		if(u.getRole().equals(1)){
			return "admin/Admin?faces-redirect=true";
		}else if(u.getRole().equals(2)){
			return "haendler/Index?faces-redirect=true";
		}else{
			return "";
		}
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
