package ch.traiding.Bean;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import ch.traiding.model.*;


@ManagedBean(name = "offeneAuftraegeBean")
@ApplicationScoped
public class OffeneAuftraegeBean {
	private ArrayList<Order> allOrders;
	private TradingService tService;
	
	public OffeneAuftraegeBean(){
		tService = new TradingService();
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
