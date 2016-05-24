package ch.traiding.Bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import ch.traiding.model.TradingService;

@ManagedBean(name = "ausschuettung")
@ApplicationScoped
public class DividendenAusschuettung {
	private TradingService tService = new TradingService();
	
	public void dividendeAusschuetten() {
		addMessage("Dividende", "Die Dividende wurde erfolgreich ausgeschüttet");
		tService.payDividend();
	}

	public void addMessage(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public TradingService gettService() {
		return tService;
	}

	public void settService(TradingService tService) {
		this.tService = tService;
	}
}
