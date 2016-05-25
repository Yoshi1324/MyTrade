package ch.traiding.Bean;


import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.NoPermissionException;

import ch.traiding.model.*;
import ch.traiding.util.ConnectionPoolingImplementation;

@ManagedBean(name = "homeBean")
@ApplicationScoped
public class HomeBean {
	private User u = new User();
	private String user;
	private String password;
	private TradingService tService;
	private String fullname;

	public HomeBean(){
		try {
			ConnectionPoolingImplementation.getInstance();
		} catch (NoPermissionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tService = new TradingService();
	}
	
	public String login(){
		u = tService.login(user, password);
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		externalContext.getSessionMap().put("benutzer", u);
		
	System.out.println(u.getRole());
		if(u.getRole() == 1){
			System.out.println("test1");
			return "/faces/private/admin/Admin?faces-redirect=true";
		}else if(u.getRole() == 2){
			System.out.println("test2");
			return "/faces/private/haendler/Portfolio?faces-redirect=true";
		}else{
			System.out.println("test3");
			return "";
		}
	}

	public String getFullname() {
		return u.getVorname() + " " + u.getName();
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
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
