package ch.traiding.Bean;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import ch.traiding.model.*;

@ManagedBean(name = "newAuftragBean")
@ApplicationScoped
public class NewAuftragBean {
    private Order order = new Order();
    private int menge;
    private TradingService tradingService = new TradingService();
    
	public NewAuftragBean(){
		
	}

	public String varkaufen(){
		tradingService.sell(order, menge);
		return "";
	}
	
	public String next(){
		return "/faces/haendler/Portfolio?faces-redirect=true";
	}
	
	public int getMenge() {
		return menge;
	}

	public void setMenge(int menge) {
		this.menge = menge;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public TradingService getTradingService() {
		return tradingService;
	}

	public void setTradingService(TradingService tradingService) {
		this.tradingService = tradingService;
	}
}
