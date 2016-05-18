package ch.traiding.Bean;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import ch.traiding.model.*;

@ManagedBean(name = "newAktienBean")
@ApplicationScoped
public class NewAktienBean {
    private Stock aktie = new Stock();
    private int menge;
    private TradingService tradingService = new TradingService();
    
	public NewAktienBean(){
		
	}
    
	public Stock getAktie() {
		return aktie;
	}

	public void setAktie(Stock aktie) {
		this.aktie = aktie;
	}


	public TradingService getTradingService() {
		return tradingService;
	}

	public void setTradingService(TradingService tradingService) {
		this.tradingService = tradingService;
	}	
	
	public int getMenge() {
		return menge;
	}

	public void setMenge(int menge) {
		this.menge = menge;
	}

	public String SaveAktie(){
		tradingService.createProduct(aktie, menge);
		return "/faces/admin/Admin?faces-redirect=true";
	}
}
