package ch.traiding.Bean;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import ch.traiding.model.*;


@ManagedBean(name = "offeneAuftraegeBean")
@ApplicationScoped
public class OffeneAuftraegeBean {
	private ArrayList<Order> allOrders;
	private TradingService tService;
	
	public OffeneAuftraegeBean(){
		tService = new TradingService();
	}
	
	public void kaufen(Order order, User user){
		addMessage("Aktien Kauf", "Die Aktie" + order.getProduct().getName() + " wurde gekauft.");
		tService.buy(order, user);
	}
	
	public void addMessage(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public ArrayList<Order> getAllOrders() {
		return allOrders = tService.getOrderList();
	}

	public void setAllOrders(ArrayList<Order> allOrders) {
		this.allOrders = allOrders;
	}

	public TradingService gettService() {
		return tService;
	}
	public void settService(TradingService tService) {
		this.tService = tService;
	}
	
	
}
