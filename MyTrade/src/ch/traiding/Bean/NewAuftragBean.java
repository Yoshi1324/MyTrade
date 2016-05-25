package ch.traiding.Bean;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import ch.traiding.model.*;
/**
* @author  Joshua	Blöchliger
* @version 1.1
*/
@ManagedBean(name = "newAuftragBean")
@ApplicationScoped
public class NewAuftragBean {
    private Order order = new Order();
    private int menge;
    private TradingService tradingService = new TradingService();
    
	public NewAuftragBean(){
		
	}

	public String verkaufen(){
		tradingService.sell(order, menge);
		return "/faces/private/haendler/Portfolio?faces-redirect=true";
	}
	
	public String next(){
		return "/faces/private/haendler/Auftragerfassenbestaetigung?faces-redirect=true";
	}
	
	public String StartVerkauf(Stock aktie, User u){
		order.setSeller(u);
		order.setProduct(aktie);
		return "/faces/private/haendler/Auftragerfassen?faces-redirect=true";
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
