package ch.traiding.Bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean(name = "ausschuettung")
@ApplicationScoped
public class DividendenAusschuettung {

	public void dividendeAusschuetten() {
		addMessage("Dividende", "Die Dividende wurde erfolgreich ausgeschüttet");
	}

	public void addMessage(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
}
